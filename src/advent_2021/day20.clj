(ns advent-2021.day20
  (:require [advent-2021.util :as util]
            [clojure.string :as str]
            [clojure.core.matrix :as m]))

;;--------------------------------
(def inputf "data/day20-input.txt")
(def testf  "data/day20-test.txt")

(defn read-data
  "Read in and separate the data into algorithm and input data"
  [f]
  (let [d (util/import-data f)]
    {:algo (first d)
     :input (drop 2 d)}))

(defn char->digit
  "Convert ./# to 0/1"
  ;; char->digit :: Char -> Int
  [c]
  (case c
    \. 0
    \# 1
    ;; else
    -1))

(defn safe-mget
  "If [i j] is out of bounds then return the default value"
  [m i j default]
  (let [[r c] (m/shape m)]
    (if (or (neg? i) (neg? j) (>= i r) (>= j c))
      default
      (m/mget m i j))))

(defn neighbours
  "Return the values of the 8 neighbours of the cell at [r c] in m."
  ;; neighbours :: [[Int]] -> [Int] -> [Int]
  [m [r c]]
  (let [nn [[-1 -1] [-1 0] [-1 1]
            [ 0 -1] [ 0 0] [ 0 1]
            [ 1 -1] [ 1 0] [ 1 1]]
        default (m/mget m r c)]
    (vec
     (for [[i j] nn]
       (safe-mget m (+ r i) (+ c j) default)))))

(defn create-matrix
  "Create a matrix from the list of cells of characters"
  [rows]
  (util/mapmap char->digit rows))

(defn enlarge
  "Enlarge the matrix m by n on all sides"
  [n m]
  (let [[rows cols] (m/shape m)
        c' (+ cols n n)]
    (as-> m <>
      (m/join-along 1 (util/zero-matrix rows n) <>)
      (m/join-along 1 <> (util/zero-matrix rows n))
      (m/join-along 0 (util/zero-matrix n c') <>)
      (m/join-along 0 <> (util/zero-matrix n c')))))

(defn transform
  "Lookup the new cell value in the algorithm given the neighbours nn"
  [algo m rowcol]
  (->> rowcol
       (neighbours m)
       util/binv->dec
       (nth algo)
       char->digit))

(defn enhance
  "Enhance the matrix m using the supplied algorithm."
  [algo m]
  (let [[maxr maxc] (m/shape m)]
    (-> (for [i (range maxr)
              j (range maxc)]
          (transform algo m [i j]))
        vec
        (m/reshape [maxr maxc]))))

(defn enhance2
  "Use loop/recur instead of list comprehension. A little faster."
  [algo m]
  (let [[maxr maxc] (m/shape m)
        m' (m/mutable m)]
    (loop [i 0 j 0]
      (if (< i maxr)
        (if (< j maxc)
          (do
            (m/mset! m' i j (transform algo m [i j]))
            (recur i (inc j)))
          (recur (inc i) 0))
        m'))))

(defn part1
  [f]
  (let [{:keys [algo input]} (read-data f)]
    (->> input
         create-matrix
         (enlarge 4)
         (enhance algo)
         (enhance algo)
         m/to-vector
         (util/count-if pos?))))

(defn part2
  [f]
  (let [{:keys [algo input]} (read-data f)
        matrix (->> input
                    create-matrix
                    (enlarge 100))]
    (->> (reduce (fn [m _]
                   (enhance2 algo m))
                 matrix
                 (range 50))
         m/to-vector
         (util/count-if pos?))))

;; The End
