(ns advent-2021.day12
  (:require [advent-2021.util :as util]
            [ubergraph.core :as uber]
            [clojure.string :as str]))

;;--------------------------------
(def inputf  "data/day12-input.txt")
(def test1f  "data/day12-test1.txt")
(def test2f  "data/day12-test2.txt")
(def test3f  "data/day12-test3.txt")

(defn make-queue []
  clojure.lang.PersistentQueue/EMPTY)

(defmethod print-method clojure.lang.PersistentQueue
  ;; Utility function for queues
  ;; Overload the printer for queues so they look like fish
  [q w]
  (print-method '<- w)
  (print-method (seq q) w)
  (print-method '-< w))

;;--------------------------------
(defn read-caves
  [f]
  (let [g (uber/graph)]
    (->> f
        util/import-data
        (map #(str/split % #"-"))
        (util/mapmap keyword)
        (uber/add-edges* g))))

(defn upper-case?
  [v]
  (= (str/upper-case v) (str v)))

(defn- conj-if-upper-case
  "Special matching code for part 1"
  [coll e]
  {:pre [(keyword? e)]}
  (if (upper-case? e)
    coll
    (conj coll e)))

(defn- all-paths-fn
  "Recursively find all the paths."
  [G start end state]
  (if (= start end)
    (-> state
        (update :path #(conj % end))
        (update :visited #(disj % start)))
    ;;else
    (let [state' (-> state
                     (update :visited #(conj-if-upper-case % start))
                     (update :path #(conj % start)))]
      (for [v (uber/neighbors G start)
            :when (not (contains? (:visited state') v))]
        (all-paths-fn G v end state')))))

(defn all-paths
  "Find all paths from src to dest, subject to rules."
  [G src dest]
  (let [state {:visited #{}
               :path (make-queue)}]
    (->> state
         (all-paths-fn G src dest)
         flatten
         (map (comp seq :path)))))

;;--------------------------------

(defn valid-lower-case?
  [visits node]
  (let [lower-case-nodes (into {} (remove #(upper-case? (first %)) visits))]
    (or (>= (node lower-case-nodes) 2)
        (pos? (count (filter #(= (second %) 2) lower-case-nodes))))))

(defn can-visit?
  "Can we visit this node based on current numbers of visits?"
  [visits node]
  (let [] ;[lower-case-nodes (into {} (remove #(upper-case? (first %)) visits))]
    (cond
     (upper-case? node) true
     (zero? (node visits)) true
     (contains? #{:start :end} node) (zero? (node visits))
     (valid-lower-case? visits node) false
     :else true)))

(defn- all-paths-fn-2
  "Recursively find all the paths."
  [G start end state]
  (if (= start end)
    (-> state
        (update :path #(conj % end))
        (assoc-in [:visited start] 0))
    ;;else
    (let [state' (-> state
                     (update-in [:visited start] inc)
                     (update :path #(conj % start)))]
      (for [v (uber/neighbors G start)
            :when (can-visit? (:visited state') v)]
        (all-paths-fn-2 G v end state')))))

(defn all-paths-2
  "Find all paths from src to dest, subject to rules."
  [G src dest]
  (let [state {:visited (zipmap (uber/nodes G) (repeat 0))
               :path (make-queue)}]
    (->> state
         (all-paths-fn-2 G src dest)
         flatten
         (map (comp seq :path)))))

;;--------------------------------
(defn part1
  [f]
  (-> f
      read-caves
      (all-paths :start :end)
      count))

(defn part2
  [f]
  (-> f
      read-caves
      (all-paths-2 :start :end)
      count))

;; The End
