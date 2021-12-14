(ns advent-2021.day13
  (:require [advent-2021.util :as util]
            [ubergraph.core :as uber]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day13-input.txt")
(def testf  "data/day13-test.txt")


(defn convert-fold
  [s]
  (let [s1 (str/split (subs s 11) #"=")
        axis (first s1)
        val (Integer/parseInt (second s1))]
    (if (= axis "x")
      {:fold [val 0]}
      {:fold [0 val]})))

(defn convert-line
  "Convert the line into data."
  [line]
  (cond
    (= line "") nil
    (str/starts-with? line "fold") (convert-fold line)
    :else {:point (mapv #(Integer/parseInt %) (str/split line #","))}))

(defn read-instructions
  [f]
  (->> f
      util/import-data
      (map convert-line)
      (remove nil?)))

(defn fold
  "Fold [x y] in the line defined by [a b] where one of a or b = 0"
  [[x y] [a b]]
  (if (zero? b)
    [(- a (Math/abs (- a x))) y]
    [x (- b (Math/abs (- b y)))]))

(defn process
  "Process the data points through the folds."
  [data]
  (let [points (remove nil? (map :point data))
        folds (remove nil? (map :fold data))]
    (map #(fold % (first folds)) points)))

#_(defn process-2
  [data]
  (let [points (remove nil? (map :point data))
        folds (remove nil? (map :fold data))]
    (reduce (fn [p f]
              (map #(fold % f) p))
            points
            folds)))

(defn part1
  [f]
  (->> f
       read-instructions
       process
       distinct
       count))

;; The End
