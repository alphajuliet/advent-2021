(ns advent-2021.day17
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day17-input.txt")
(def testf  "data/day17-test.txt")

(defn read-target
  "Read the extent of the target area as [x0 x1 y0 y1]"
  [f]
  (->> f
       util/import-data
       first
       (re-seq #"-?\d+")
       (map #(Integer/parseInt %))
       #_(partition 2 2)))

(defn decay
  "Model the decay of the x velocity"
  [x]
  (if (nat-int? x)
    (max 0 (dec x))
    (min 0 (inc x))))

(defn step
  "Iterate the position based on the velocity"
  [[x y vx vy]]
  (vector (+ x vx)
          (+ y vy)
          (decay vx)
          (dec vy)))

(defn in-target?
  "Is the position in the target zone?
  It assumes x0 < x1 and y0 < y1"
  [[x y] [x0 x1 y0 y1]]
  (and (<= x0 x x1)
       (<= y0 y y1)))

(defn trajectory
  "Return the points in the trajectory until it reaches the target or times out."
  [init-state target]
  (reduce (fn [st _]
            (let [[x y _ _ :as prev-st] (peek st)]
              (cond
                ;; in target => return hit!
                (in-target? [x y] target) (reduced (conj st :hit))
                ;; overshot target => stop
                (or (> x (second target)) (< y (util/third target))) (reduced st)
                ;; else add new point and keep going
                :else (conj st (step prev-st)))))
          [init-state]
          (range 10000)))

(defn solve-quad
  [c]
  (/ (- (Math/sqrt (+ (* 4 c) 1)) 1) 2))

(defn velocity-ranges
  "Work out the range of x and y velocities that might hit the target with high max y"
  [[x0 x1 y0 y1]]
  (let [min-xv (int (solve-quad (* 2 x0)))]
    (vector min-xv
           (+ 2 (int (solve-quad (* 2 x1))))
           (int (* 8 min-xv))
           (int (* 10 min-xv)))))

(defn max-y
  "Find the maximum y value in the states or return nil"
  [states]
  (if (= (last states) :hit)
    (->> states
        (filter vector?)
        (map second)
        (apply max))
    0))

(defn try-trajectories
  [target]
  (let [[vx0 vx1 vy0 vy1] (velocity-ranges target)]
    (for [vx (range vx0 vx1)
          vy (range vy0 vy1)]
      (max-y (trajectory [0 0 vx vy] target)))))

(defn part1
  [f]
  (->> f
       read-target
       try-trajectories
       (apply max)))



;; The End
