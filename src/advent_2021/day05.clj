(ns advent-2021.day05
  (:require [advent-2021.util :as util]
            [clojure.math.combinatorics :as combo]
            [clojure.string :as str]
            [clojure.set :as set]))

;;--------------------------------
(def inputf "data/day05-input.txt")
(def testf "data/day05-test.txt")

(defn read-segments
  [line]
  (as-> line <>
      (str/split <> #"\D+")
      (map #(Integer/parseInt %) <>)))

(defn read-vents
  "Read in all the segment endpoints."
  [f]
  (->> f
       util/import-data
       (map read-segments)
       (map #(partition 2 %))))

(defn only-hv
  "True if the two endpoints form a horizontal or vertical segment."
  [[[x1 y1] [x2 y2]]]
  (or (= x1 x2)
      (= y1 y2)))

#_(defn sort-pair
  "Quick pair sort"
  [[x y]]
  (if (> x y) [y x] [x y]))

#_(defn overlap
  "Determine the overlap of two linear ranges"
  [r1 r2]
  (let [[a1 a2] (sort-pair r1)
        [b1 b2] (sort-pair r2)]
    (vec (range (max a1 b1) (inc (min a2 b2))))))

(defn enumerate
  "List all the integers between a and b inclusive."
  [a b]
  (if (<= a b)
    (range a (inc b))
    (range a (dec b) -1)))

(defn points
  "Enumerate all the points between P1 and P2"
  [[x1 y1] [x2 y2]]
  (cond
    (= x1 x2) (map vector (repeat x1) (enumerate y1 y2))
    (= y1 y2) (map vector (enumerate x1 x2) (repeat y1))
    :else (map vector (enumerate x1 x2) (enumerate y1 y2))))

(defn overlap-new
  "Find the overlap between two segments."
  [[p1 p2] [p3 p4]]
  (let [line-a (points p1 p2)
        line-b (points p3 p4)]
    (vec (set/intersection (set line-a) (set line-b)))))

#_(defn overlap-2d
  "Return all the overlapping points between two H or V line segments."
  [[[x1 y1] [x2 y2]] [[x3 y3] [x4 y4]]]
  (let [ov-x (overlap [x1 x2] [x3 x4])
        ov-y (overlap [y1 y2] [y3 y4])]
    (if (or (empty? ov-x) (empty? ov-y))
      false
      (combo/cartesian-product ov-x ov-y))))

(defn count-overlaps
  "Count the number of overlapping points across a list of segments."
  [segs]
  (->> (for [s1 segs
             s2 segs
             :when (not= s1 s2)]
         (overlap-new s1 s2))
       (remove false?)
       (apply concat)
       distinct
       count))

(defn part1
  [f]
  (->> f
       read-vents
       (filter only-hv)
       count-overlaps))

(defn part2
  [f]
  (->> f
       read-vents
       count-overlaps))

;; The End
