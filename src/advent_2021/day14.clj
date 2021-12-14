(ns advent-2021.day14
  (:require [advent-2021.util :as util]
            [clojure.core.matrix :as m]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day14-input.txt")
(def testf  "data/day14-test.txt")

(defn convert-rules
  [lines]
  (->> lines
       (remove str/blank?)
       (map #(str/split % #"\s+->\s+"))
       (into {})))

(defn read-instructions
  [f]
  (let [lines (util/import-data f)]
    {:template (first lines)
     :rules (convert-rules (rest lines))}))

(defn partition-string
  "Partition a string, similar to a sequence"
  [n step s]
  (as-> s <>
      (str/split <> #"")
      (partition n step <>)
      (map str/join <>)))

(defn apply-rules
  "Apply the rules to a string if possible."
  [rules s]
  (if (contains? rules s)
    (str (get rules s) (str (last s)))
    ;;else
    s))

(defn expand-string
  "Expand a string according to the rules."
  [rules s]
  (->> s
       (partition-string 2 1)
       (map (partial apply-rules rules))
       (str/join "")
       (str (first s))))

(defn calc-score
  "Difference of the most and least occurring characters."
  [s]
  (let [f (frequencies s)
        h (apply max-key val f)
        l (apply min-key val f)]
    (- (second h) (second l))))

(defn expand
  "Expand the template n times against the rules."
  [n {:keys [rules template]}]
  (reduce (fn [acc _] (expand-string rules acc)) template (range n)))

(defn part1
  [f]
  (->> f
       read-instructions
       (expand 10)
       calc-score))

;; The End
