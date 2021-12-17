(ns advent-2021.day15
  (:require [advent-2021.util :as util]
            [clojure.core.matrix :as m]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day15-input.txt")
(def testf  "data/day15-test.txt")

(defn read-levels
  [f]
  (->> f
       util/import-data
       (map #(str/split % #""))
       (util/mapmap #(Integer/parseInt %))))

(defn neighbours
  "Find all neighbours of a given point. From Joy of Clojure.
  We only need to look to the right and down in our case."
  ([size yx]
   (neighbours [[-1 0] [1 0] [0 -1] [0 1]] size yx))
  ([deltas size yx]
   (filter (fn [new-yx]
             (every? #(< -1 % size) new-yx))
           (map #(vec (map + yx %)) deltas))))

(defn estimate-cost
  [step-cost-est size y x]
  (* step-cost-est
     (- (+ size size) y x 2)))

(defn path-cost
  [node-cost cheapest-nbr]
  (+ node-cost
     (:cost cheapest-nbr 0)))

(defn total-cost
  [newcost step-cost-est size y x]
  (+ newcost
     (estimate-cost step-cost-est size y x)))

(defn min-by
  [f coll]
  (when (seq coll)
    (reduce (fn [min this]
              (if (> (f min) (f this)) this min))
            coll)))

(defn a-star [cell-costs start-yx step-est]
  (let [size (count cell-costs)]
    (loop [steps 0
           routes (vec (repeat size (vec (repeat size nil))))
           work-todo (sorted-set [0 start-yx])]
      (if (empty? work-todo)
        [(peek (peek routes)) :steps steps]
        (let [[_ yx :as work-item] (first work-todo)
              rest-work-todo (disj work-todo work-item)
              nbr-yxs (neighbours size yx)
              cheapest-nbr (min-by :cost (keep #(get-in routes %) nbr-yxs))
              newcost (path-cost (get-in cell-costs yx) cheapest-nbr)
              oldcost (:cost (get-in routes yx))]
          (if (and oldcost (>= newcost oldcost))
            (recur (inc steps) routes rest-work-todo)
            (recur (inc steps)
                   (assoc-in routes yx
                             {:cost newcost
                              :yxs (conj (:yxs cheapest-nbr [])
                                         yx)})
                   (into rest-work-todo
                         (map (fn [[y x :as w]] [(total-cost newcost step-est size y x) w])
                              nbr-yxs)))))))))

(defn x-mod [x] (= x 10) 1 x)

(defn create-large-grid
  "Create the large grid in part 2"
  [g]
  (let [[x y] (m/shape g)]
    (m/reshape (for [i (range 5)
                     j (range 5)]
                 (m/emap #(x-mod (+ % i j)) g))
               [(* 5 x) (* 5 y)])))

;;--------------------------------
(defn part1
  [f]
  (let [grid (read-levels f)
        start-val (m/mget grid 0 0)]
    (-> grid
        (a-star [0 0] 10)
        (get-in [0 :cost])
        (- start-val))))

(defn part2
  [f]
  (let [grid (create-large-grid (read-levels f))
        start-val (m/mget grid 0 0)]
    (-> grid
        (a-star [0 0] 10)
        #_(get-in [0 :cost])
        #_(- start-val))))

;; The End
