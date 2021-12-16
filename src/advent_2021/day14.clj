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

(defn augment-rule
  "Augment a transition rule to produce new pairs"
  [k v]
  [(str (subs k 0 1) v)
   (str v (subs k 1 2))])

(defn update-counts
  "Update the counts for a single rule."
  [counts pair c to]
  (if (zero? c)
    counts
    ;;else
    (let [new-pairs (augment-rule pair to)]
      (-> counts
          (update (first new-pairs) (partial + c))
          (update (second new-pairs) (partial + c))))))

(defn initialise-counts
  "Initialise the counts from the given pairs."
  [counts pairs]
  (reduce (fn [c p]
            (update c p inc))
          counts
          pairs))

(defn step-counts
  "Update the counts for each of the given pairs."
  [rules counts]
  (let [pairs (keys counts)
        empty-counts (zipmap (keys rules) (repeat 0))]
    (reduce (fn [c p]
              (update-counts c p (get counts p) (get rules p)))
            empty-counts
            pairs)))

(defn expand
  "Count the pairs as we expand the template n times."
  [n {:keys [:template :rules]}]
  (let [empty-counts (zipmap (keys rules) (repeat 0))
        init-pairs (partition-string 2 1 template)
        init-counts (initialise-counts empty-counts init-pairs) ]
    (reduce (fn [c _] (step-counts rules c))
            init-counts
            (range n))))

(defn- get-sum
  [v]
  (apply + (map second v)))

(defn calc-score
  [counts]
  (->> counts
       (group-by (comp second first))
       (map #(get-sum (second %)))
       ((fn [x] (- (apply max x) (apply min x))))))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-instructions
       ;; (expand 10)
       ;; calc-score
       (expand 10)
       calc-score))

(defn part2
  [f]
  (->> f
       read-instructions
       (expand 10)
       calc-score))

;; The End
