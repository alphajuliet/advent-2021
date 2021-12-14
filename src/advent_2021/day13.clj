(ns advent-2021.day13
  (:require [advent-2021.util :as util]
            [clojure.core.matrix :as m]
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
  "Process the data points through the first fold."
  [data]
  (let [points (remove nil? (map :point data))
        folds (remove nil? (map :fold data))]
    (map #(fold % (first folds)) points)))

(defn process-2
  "Process the data points through all the folds."
  [data]
  (let [points (remove nil? (map :point data))
        folds (remove nil? (map :fold data))]
    (reduce (fn [p f] (map #(fold % f) p))
            points
            folds)))

(defn viz
  "Mapping of matrix to readable characters"
  [v]
  (if (zero? v) " " "#"))

(defn points->matrix
  "Set a collection of points to 1 in a zero matrix."
  [points]
  (let [max-x (inc (apply max (map first points)))
        max-y (inc (apply max (map second points)))]
    (reduce (fn [m [x y]] (m/mset m y x 1))
            (m/zero-matrix max-y max-x)
            points)))

(defn viz-matrix
  "Visualise a binary matrix."
  [m]
  (let [m1 (util/mapmap viz m)
        m2 (map (partial str/join "") m1)]
    (doseq [x m2]
      (println x))))

(defn part1
  [f]
  (->> f
       read-instructions
       process
       distinct
       count))

(defn part2
  [f]
  (->> f
       read-instructions
       process-2
       distinct
       points->matrix
       viz-matrix))

;; The End
