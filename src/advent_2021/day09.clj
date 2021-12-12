(ns advent-2021.day09
  (:require [advent-2021.util :as util]
            [clojure.core.matrix :as m]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day09-input.txt")
(def testf  "data/day09-test.txt")

(defn read-heights
  [f]
  (->> f
       util/import-data
       (map #(str/split % #""))
       (util/mapmap #(Integer/parseInt %))))

(defn invalid-location? [[r c] [max-r max-c]]
  (or (neg? r)
      (neg? c)
      (> r (dec max-r))
      (> c (dec max-c))))

(defn neighbour-locs
  "Return the list of valid neighbour locations."
  [[r c] max-rc]
  (let [locs (vector [(dec r) c]
                     [(inc r) c]
                     [r (dec c)]
                     [r (inc c)])]
    (remove #(invalid-location? % max-rc) locs)))

(defn neighbour-values
  "Return a list of neighbour values."
  [mat loc]
  (let [max-rc (m/shape mat)
        nlocs (neighbour-locs loc max-rc)]
    (map (fn [[r c]] (m/mget mat r c)) nlocs)))

(defn local-minima?
  ([mat [r c]]
   (let [x (m/mget mat r c)]
     (< x (apply min (neighbour-values mat [r c]))))))

(defn find-local-minima
  "Find all local minima in a matrix"
  [mat]
  (let [[max-r max-c] (m/shape mat)]
    (for [r (range max-r)
          c (range max-c)]
      (if (local-minima? mat [r c])
        {:loc [r c] :value (m/mget mat r c)}
        nil))))

(defn find-basin'
  "Recursively determine the size of the local basin"
  [mat [r c] max-rc state]
  (if (and (not (contains? (:visited state) [r c]))
           (not= 9 (m/mget mat r c)))
    (let [mat' (m/mset mat r c 9)
          state' (-> state
                     (update :size inc)
                     (update :visited #(conj % [r c])))
          nlocs (neighbour-locs [r c] max-rc)]
      (reduce (fn [st loc]
                (find-basin' mat' loc max-rc st))
              state'
              nlocs))
    ;; else do nothing
    state))

(defn find-basin
  "Find the size of the basin at loc."
  [mat loc]
  (let [max-rc (m/shape mat)]
    (:size
     (find-basin' mat loc max-rc {:visited #{} :size 0}))))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-heights
       find-local-minima
       (map :value)
       (filter number?)
       (map inc)
       (apply +)))

(defn part2
  [f]
  (let [mat (read-heights f)]
    (->> mat
         find-local-minima
         (map :loc)
         (filter vector?)
         (map (partial find-basin mat))
         (sort >)
         (take 3)
         (apply *))))

;; The End
