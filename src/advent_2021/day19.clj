(ns advent-2021.day19
  (:require [advent-2021.util :as util]
            [clojure.core.matrix :as m]
            [instaparse.core :as insta]))

;;--------------------------------
(def inputf "data/day19-input.txt")
(def testf  "data/day19-test.txt")

(def parse-report
  "Parse a 2D or 3D set of scanner reports"
  (insta/parser
   "S := (Scan | <CR>)+
    Scan := <HeaderStart> <Number> <HeaderEnd> Point+
    HeaderStart := '--- scanner '
    Number := #'-?\\d+'
    HeaderEnd := ' ---' CR
    Point := Number <','> Number (<','> Number)? <CR>?
    <CR> := '\n'"))

(defn convert-report
  [tree]
  (insta/transform
   {:S vector
    :Scan vector
    :Number #(Integer/parseInt %)
    :Point vector}
   tree))

(defn read-report
  [f]
  (-> f
      slurp
      parse-report
      convert-report))

(def s1
  "--- scanner 0 ---
0,2
4,1
3,3

--- scanner 1 ---
-1,-1
-5,0
-2,1")

(def delta m/sub)

(defn deltas
  "Create a delta of each pair of points.
  This will create $\frac{n (n-1)}{2}$ points"
  [points]
  (for [i (range (count points))
        j (range (count points))
        :when (< i j)]
    (delta (nth points i) (nth points j))))

(def cos #(Math/cos (Math/toRadians %)))
(def sin #(Math/sin (Math/toRadians %)))

(defn rotate
  "Rotate a 3D integer point around the axes by the given angles as multiples of 90 degrees."
  ;; Rotation matrices from https://blogs.sas.com/content/iml/2016/11/07/rotations-3d-data.html
  [[rx ry rz] p]
  (let [rx [[1 0 0] [0 (cos rx) (- (sin rx))] [0 (sin rx) (cos rx)]]
        ry [[(cos ry) 0 (- (sin ry))] [0 1 0] [(sin ry) 0 (cos ry)]]
        rz [[(cos rz) (- (sin rz)) 0] [(sin rz) (cos rz) 0] [0 0 1]]]
    (m/emap #(int (Math/round %))
            (m/mmul p rx ry rz))))

(defn all-rotations
  "Return all 24 3D rotations of a point p"
  [p]
  (let [rs [0 90 180 -90]]
    (distinct
     (for [x rs y rs z rs]
       (rotate [x y z] p)))))

(defn part1
  [f]
  (->> f
       read-report))

;; The End
