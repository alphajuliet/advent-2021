(ns advent-2021.day04
  (:require [advent-2021.util :as util]
            [clojure.string :as str]
            [clojure.core.matrix :as m]
            [clojure.set :as set]))

;;--------------------------------
(def inputf "data/day04-input.txt")
(def testf "data/day04-test.txt")

;;--------------------------------
(defn parse-numbers
  "Convert a string into a vector of numbers"
  [s]
  (if (zero? (count s))
    []
    (mapv #(Integer/parseInt %) (str/split (str/trim s) #",|\s+"))))

(defn read-boards
  "Read in a collection of boards from a series of vectors"
  [vs]
  (->> vs
       rest
       (remove empty?)
       (partition 5)))

(defn read-game
  "Read in the game data"
  [f]
  (let [vs (->> f
                util/import-data
                (map parse-numbers))]
    {:numbers (first vs)
     :boards (read-boards (rest vs))}))

(defn enumerate-matrix
  "Enumerate a matrix as a hash map of locations and elements."
  [mx]
  (->> mx
      (m/emap-indexed hash-map #_(fn [i e] [i e]))
      (apply concat)
      (apply concat)
      (into {})))

(defn find-value
  "Return the keys that hold the value."
  [m value]
  {:pre [(map? m)]}
  (->> m
       (filter #(= value (second %)))
       (map first)))

(defn check-rows
  "Count the filled rows across the boards"
  [locations nboards]
  (let [counts (for [b (range nboards)
                     r (range 5)]
                 (let [n (count (filter #(and (= b (first %))
                                              (= r (second %)))
                                        locations))]
                   {:board b :row r :count n}))
        hits (filter #(= 5 (:count %)) counts)]
    (if (empty? hits)
      false
      (first hits))))


(defn check-columns
  "Count the filled columns across the boards"
  [locations nboards]
  (let [counts (for [b (range nboards)
                     c (range 5)]
                 (let [n (count (filter #(and (= b (first %))
                                              (= c (util/third %)))
                                        locations))]
                   {:board b :col c :count n}))
        hits (filter #(= 5 (:count %)) counts)]
    (if (empty? hits)
      false
      (first hits))))

(defn check-rows-columns
  [locations nboards]
  (let [result (check-rows locations nboards)]
    (if (false? result)
      (check-columns locations nboards)
      result)))

(defn draw-numbers
  "Draw numbers and match against the boards.
  When there is a filled row or column then return the matched number, the board and row, and all the matched locations so far on that board."
  [numbers locations]
  (let [nboards (/ (count locations) 25)]
    (reduce (fn [s n]
             (let [new-s (into s (find-value locations n))]
               (if-let [locs (check-rows-columns new-s nboards)]
                 (reduced (into {:number n :matches (filter #(= (:board locs) (first %)) new-s)}
                                locs))
                 new-s)))
           []
           numbers)))

(defn score
  "Work out the score for the given match"
  [{:keys [:number :board :matches]} all-locations]
  (let [all-board (into {} (filter #(= board (first (first %1))) all-locations))
        remaining-locs (set/difference (set (keys all-board))
                                       (set matches))]
    ;; (println (select-keys all-board remaining-locs))
    (->> remaining-locs
         (select-keys all-board)
         vals
         (apply +)
         (* number))))

;;--------------------------------
(defn part1
  [f]
  (let [game (read-game f)
        all-locs (enumerate-matrix (:boards game))
        result (draw-numbers (:numbers game) all-locs)]
    ;; (println result)
    (score result all-locs)))

(defn part2
  [f])

;; The End
