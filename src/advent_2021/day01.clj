(ns advent-2021.day01
  (:require [advent-2021.util :as util]))

(def inputf "data/day01-input.txt")
(def testf "data/day01-test.txt")

;;--------------------------------
(defn read-depths
  "Return a vector of depths"
  ;; read-depths :: IO String -> Vector Int
  [f]
  (->> f
      util/import-data
      (mapv util/string->int)))

(defn difference
  "Return a difference vector"
  ;; difference :: Vector Num -> Vector Num
  [v]
  (mapv - (rest v) v))

(defn part1
  [f]
  (->> f
       read-depths
       difference
       (util/count-if pos?)))

(defn part2
  [f]
  (->> f
       read-depths
       (partition 3 1 [0]) ;; window of 3 with step of 1
       (mapv (partial apply +))
       difference
       (util/count-if pos?)))

;; The End
