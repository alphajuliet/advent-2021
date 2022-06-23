(ns advent-2021.day08
  (:require [advent-2021.util :as util]
            [clojure.string :as str]
            [clojure.core.match :as match]
            [clojure.set :as set]))

;;--------------------------------
(def inputf "data/day08-input.txt")
(def testf  "data/day08-test.txt")

(defn read-patterns
  [f]
  (->> f
    (util/import-data)
    (map #(str/split % #"\s+\|\s+"))
    (util/mapmap #(str/split % #"\s+"))))

;; The map from digits 0-9 (rows) to segments a-g (columns)
(def R [[1 1 1 0 1 1 1] ;0 => 6 1's
        [0 0 1 0 0 1 0] ;1 => 2
        [1 0 1 1 1 0 1] ;2 => 5
        [1 0 1 1 0 1 1] ;3 => 5
        [0 1 1 1 0 1 0] ;4 => 4
        [1 1 0 1 0 1 1] ;5 => 5
        [1 1 0 1 1 1 1] ;6 => 6
        [1 0 1 0 0 1 0] ;7 => 3
        [1 1 1 1 1 1 1] ;8 => 7
        [1 1 1 1 0 1 1]]) ;9 => 6

;; Test data result, to experiment with.
(def X [[1 1 1 1 1 1 1]
        [0 1 1 1 1 1 0]
        [1 0 1 1 0 1 1]
        [1 1 1 1 0 1 0]
        [1 1 0 1 0 0 0]
        [1 1 1 1 1 1 0]
        [0 1 1 1 1 1 1]
        [1 1 0 0 1 1 0]
        [1 1 1 1 1 0 1]
        [1 1 0 0 0 0 0]])

(defn part1
  [f]
  (->> f
      read-patterns
      (util/mapmap (partial map count))
      (map second)
      (apply concat)
      frequencies
      (util/swap select-keys [2 3 4 7])
      vals
      (apply +)))

(defn- filterf
  "Return the first match from the filter f"
  [f xs]
  (->> xs
       (filter f)
       first))

(defn- covers?
  "Does a cover b? => does all of b appear in a?"
  [a b]
  (set/superset? (set b) (set a)))

(defn find-mappings
  "Find the mapping for each line of digits"
  [line]
  (let [d (first line)]
    (-> {}
        (assoc 1 (filterf #(= 2 (count %)) d))
        (assoc 7 (filterf #(= 3 (count %)) d))
        (assoc 4 (filterf #(= 4 (count %)) d))
        (assoc 8 (filterf #(= 7 (count %)) d))
        ((fn [m] (assoc m 3 (filterf #(and (= 5 (count %))
                                          (covers? (get m 1) %)) d))))
        ((fn [m] (assoc m 9 (filterf #(and (= 6 (count %))
                                          (covers? (get m 3) %)) d))))
        ((fn [m] (assoc m 0 (filterf #(and (= 6 (count %))
                                          (covers? (get m 1) %)
                                          (covers? (get m 7) %)
                                          (not= % (get m 9))) d))))
        ((fn [m] (assoc m 6 (filterf #(and (= 6 (count %))
                                          (not= % (get m 0))
                                          (not= % (get m 9))) d))))
        ((fn [m] (assoc m 5 (filterf #(and (= 5 (count %))
                                          (covers? % (get m 6))) d))))
        ((fn [m] (assoc m 2 (filterf #(and (= 5 (count %))
                                          (not= % (get m 3))
                                          (not= % (get m 5))) d)))))))

(defn decode-with-map
  "Decode strs with m"
  [strs m]
  (let [m' (util/map-key set (set/map-invert m))]
    (->> strs
        (map set)
        (map #(get m' %)))))

(defn part2
  "Solve part 2 using the technique here... https://todd.ginsberg.com/post/advent-of-code/2021/day8/"
  [f]
  (let [p (read-patterns f)
        ms (map find-mappings p)
        ds (map second p)]
    (->> ms
         (map decode-with-map ds)
         (map (fn [e] (reduce #(+ (* 10 %1) %2) e)))
         (apply +))))

;; The End
