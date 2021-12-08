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
  "Calculate the fuel to move all elements to location i in part 1."
  [n v]
  (apply + (mapv #(Math/abs (- % n)) v)))

(defn sum-to-n
  [n]
  (/ (* n (inc n)) 2))

(defn count-fuel-2
  "Calculate the fuel to move all elements to location i in part 2."
  [n v]
  (apply + (mapv #(sum-to-n (Math/abs (- % n))) v)))

(defn min-fuel
  "Calculate the minimum fuel with the given algorithm"
  [f count-fn]
  (let [v (read-positions f)
        max-v (apply max v)]
    (apply min
           (map #(count-fn % v) (range (inc max-v))))))

;;--------------------------------
(defn part1
  [f]
  (min-fuel f count-fuel-1))

(defn part2
  [f]
  (min-fuel f count-fuel-2))

;; The End
