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

(defn xget
  "Get matrix value but force to 9 if out of bounds"
  [m r c max-r max-c]
  (if (or (neg? r)
          (neg? c)
          (> r (dec max-r))
          (> c (dec max-c)))
    9
    (m/mget m r c)))

(defn neighbours
  [mat [r c]]
  (let [[max-r max-c] (m/shape mat)]
    [(xget mat (dec r) c max-r max-c)
     (xget mat (inc r) c max-r max-c)
     (xget mat r (dec c) max-r max-c)
     (xget mat r (inc c) max-r max-c)]))

(defn local-minima?
  ([mat [r c]]
   (let [x (m/mget mat r c)]
     (< x (apply min (neighbours mat [r c]))))))

(defn find-local-minima
  [mat]
  (let [[max-r max-c] (m/shape mat)]
    (for [r (range max-r)
          c (range max-c)]
      (if (local-minima? mat [r c])
        (m/mget mat r c)
        nil))))

(defn part1
  [f]
  (->> f
       read-heights
       find-local-minima
       (filter number?)
       (map inc)
       (apply +)))

;; The End
