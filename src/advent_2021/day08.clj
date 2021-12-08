(ns advent-2021.day08
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day08-input.txt")
(def testf  "data/day08-test.txt")

(defn read-patterns
  [f]
  (as-> f <>
    (util/import-data <>)
    (map #(str/split % #"\s+\|\s+") <>)
    (util/mapmap #(str/split % #"\s+") <>)))

(defn part1
  [f]
  (->> f
      read-patterns
      (util/mapmap #(map count %))
      (map second)
      (apply concat)
      frequencies
      (util/swap select-keys [2 3 4 7])
      vals
      (apply +)))

;; The End
