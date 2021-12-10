(ns advent-2021.day08
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day08-input.txt")
(def testf  "data/day08-test.txt")

(defn read-patterns
  [f]
  (->> f
    (util/import-data)
    (map #(str/split % #"\s+\|\s+"))
    (util/mapmap #(str/split % #"\s+"))))

;; The map from digits 0-9 (rows) to segments a-g (columns)
(def R [[1 1 1 0 1 1 1] ;0
        [0 0 1 0 0 1 0] ;1
        [1 0 1 1 1 0 1] ;2
        [1 0 1 1 0 1 1] ;3
        [0 1 1 1 0 1 0] ;4
        [1 1 0 1 0 1 1] ;5
        [1 1 0 1 1 1 1] ;6
        [1 0 1 0 0 1 0] ;7
        [1 1 1 1 1 1 1] ;8
        [1 1 1 1 0 1 1]]) ;9

;; Test data result, to experiment with.
(def X [[1 1 1 1 1 1 1]
        [0 1 1 1 1 1 0]
        [1 0 1 1 0 1 1]
        [1 1 1 1 0 1 0]
        [1 1 0 1 0 0 0]
        [1 1 1 1 1 1 0]
        [0 1 1 1 1 1 1]
        [1 1 0 0 1 1 0]
        [1 1 1 1 1 0 1]
        [1 1 0 0 0 0 0]])

(defn part1
  [f]
  (->> f
      read-patterns
      (util/mapmap (partial map count))
      (map second)
      (apply concat)
      frequencies
      (util/swap select-keys [2 3 4 7])
      vals
      (apply +)))

;; The End
