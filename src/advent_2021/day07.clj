(ns advent-2021.day07
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day07-input.txt")
(def testf  "data/day07-test.txt")

(defn read-positions
  "Read in the initial crab sub 2D positions."
  [f]
  (as-> f <>
    (util/import-data <>)
    (first <>)
    (str/split <> #",")
    (mapv #(Integer/parseInt %) <>)))

(defn count-fuel-1
  "Calculate the fuel to move all elements to location i."
  [n v]
  (apply + (mapv #(Math/abs (- % n)) v)))

(defn part1
  [f]
  (let [v (read-positions f)
        max-v (apply max v)]
    (apply min
           (map #(count-fuel-1 % v) (range (inc max-v))))))

;; The End
