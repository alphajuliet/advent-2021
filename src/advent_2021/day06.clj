(ns advent-2021.day06
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day06-input.txt")
(def testf "data/day06-test.txt")

(defn read-fish
  "Read in the initial vector of fish states."
  [f]
  (as-> f <>
    (util/import-data <>)
    (first <>)
    (str/split <> #",")
    (mapv #(byte (Integer/parseInt %)) <>)))

(defn evolve
  "Evolve the population of counts by one tick."
  [v]
  (let [nspawn (first v)
        v' (conj (vec (rest v)) nspawn)]
    (update v' 6 (partial + nspawn))))

(defn run-evolution
  [init-state days]
  (apply +
         (reduce (fn [v _] (evolve v))
             init-state
             (range days))))

(defn create-counts
  "Create a vector of counts across the various values."
  [v]
  (->> (frequencies v)
       (into (zipmap (range 9) (repeat 0)))
       (sort-by key <)
       vals
       vec))

(defn part1
  [f]
  (-> f
      read-fish
      create-counts
      (run-evolution 80)))

(defn part2
  [f]
  (-> f
      read-fish
      create-counts
      (run-evolution 256)))

;; The End
