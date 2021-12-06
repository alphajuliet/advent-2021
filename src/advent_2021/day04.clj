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
  "Read in the initial game data"
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
  "Count the filled rows across the given locations.
  Return the matching locations if a row is filled."
  [locations nboards]
  (let [counts (for [b (range nboards)
                     r (range 5)]
                 (let [n (count (filter #(and (= b (first %))
                                              (= r (second %)))
                                        locations))]
                   {:board b :count n}))
        hits (filter #(= 5 (:count %)) counts)]
    (if (empty? hits)
      false
      (first hits))))

(defn check-columns
  "Count the filled columns across the given locations.
  Return the matching locations if a column is filled."
  [locations nboards]
  (let [counts (for [b (range nboards)
                     c (range 5)]
                 (let [n (count (filter #(and (= b (first %))
                                              (= c (util/third %)))
                                        locations))]
                   {:board b :count n}))
        hits (filter #(= 5 (:count %)) counts)]
    (if (empty? hits)
      false
      (first hits))))

(defn check-rows-columns
  "Check all rows and columns across the given locations."
  [locations nboards]
  (let [result (check-rows locations nboards)]
    (if (false? result)
      (check-columns locations nboards)
      result)))

(defn draw-numbers
  "Draw numbers and match against the boards.
  When there is a filled row or column then return the matched number, the board and row,
  and all the matched locations so far on that board."
  [{:keys [:numbers :boards :nboards]}]
  (reduce (fn [matches n]
            (let [matches' (into matches (find-value boards n))
                  hits (check-rows-columns matches' nboards)]
              (if (false? hits)
                matches'
                (reduced (into {:number n :matches matches'} hits)))))
          []
          numbers))

(defn score
  "Work out the score for the given match"
  [{:keys [:number :board :matches]} all-locations]
  (let [all-board (into {} (filter #(= board (first (first %1))) all-locations))
        board-matches (filter #(= board (first %)) matches)
        remaining-locs (set/difference (set (keys all-board))
                                       (set board-matches))]
    (->> remaining-locs
         (select-keys all-board)
         vals
         (apply +)
         (* number))))

(defn remove-board
  "Remove the given board from the enumerated locations."
  [b enum]
  (into {} (remove #(= b (first (first %))) enum)))


(defn init-state
  [{:keys [:numbers :boards] :as state}]
  {:numbers numbers
   :boards (enumerate-matrix boards)
   :matches []
   :nboards (count boards)})

(defn remaining-boards
  "Which boards are left in the enumeration?"
  [boards]
  (->> boards
       (map #(first (first %)))
       (sort <)
       dedupe))

;;--------------------------------
(defn part1
  [f]
  (let [state (init-state (read-game f))
        result (draw-numbers state)]
    (println result)
    (score result (:boards state))))

(defn part2
  [f]
  (let [initial-state (init-state (read-game f))]
    (reduce (fn [state _]
              (let [result (draw-numbers state)
                    state (-> state
                              (update :boards #(remove-board (:board result) %))
                              (update :nboards dec)
                              (assoc :matches (:matches result)))]
                (println (:board result) (:number result))
                (if (= 1 (:nboards state))
                  (reduced (draw-numbers state))
                  state)))
            initial-state
            (:numbers initial-state))))

;; The End
