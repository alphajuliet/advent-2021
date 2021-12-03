(ns advent-2021.day03
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day03-input.txt")
(def testf "data/day03-test.txt")

;;--------------------------------
(defn read-report
  "Read the diagnostic report"
  [f]
  (->> f
       util/import-data
       (mapv (fn [line]
               (mapv #(Integer/parseInt %)
                     (str/split line #""))))))

(defn flip-bit
  "Return the complement of b"
  [b]
  (- 1 b))

(defn most-common-value
  "Determine the most common bit value. Default to 1 on a tie."
  [v]
  (let [len (count v)
        n (apply + v)]
    (if (>= n (/ len 2)) 1 0)))

(def least-common-value (comp flip-bit most-common-value))

(defn power-consumption
  "Calculate the power consumption as gamma * epsilon"
  [v]
  (let [gamma (util/binv->dec v)
        epsilon (util/binv->dec (util/ones-complement v))]
    (* gamma epsilon)))

(defn filter-on-bit
  "Filter elements of m that have the most common bit in position i"
  [index mod-fn m]
  (let [b (mod-fn (nth (util/T m) index))]
    (filterv #(= b (nth % index)) m)))

(defn rating
  "Determine the rating depending on the bit ranking function."
  [ranking-fn m]
  (let [word-len (count (first m))]
    (as-> m <>
      (reduce (fn [acc index]
                (if (= 1 (count acc))
                  (reduced acc)
                  (filter-on-bit index ranking-fn acc)))
              <>
              (range word-len))
      (first <>)
      (util/binv->dec <>))))

(def oxygen-gen-rating
  (partial rating most-common-value))

(def co2-scrubber-rating
  (partial rating least-common-value))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-report
       util/T ; transpose
       (mapv most-common-value)
       power-consumption))

(defn part2
  [f]
  (let [m (read-report f)]
    (* (oxygen-gen-rating m)
       (co2-scrubber-rating m))))

;; The End
