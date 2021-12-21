(ns advent-2021.day17
  (:require [advent-2021.util :as util]))

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
  "Return a trajectory that hits the target, or nil otherwise."
  [init-state target]
  (loop [st [init-state]]
    (let [[x y _ _] (peek st)]
      (cond
        (in-target? [x y] target) st
        (or (> x (second target)) (< y (util/third target))) nil
        :else (recur (conj st (step (peek st))))))))

(defn solve-quad
  "Utility function to solve for vx"
  [c]
  (/ (- (Math/sqrt (+ (* 4 c) 1)) 1) 2))

(defn velocity-range-A
  "Work out the range of x and y velocities that might hit the target with max y"
  [[x0 x1 y0 y1]]
  (vector (int (Math/floor (solve-quad (* 2 x0))))
          (int (Math/ceil (solve-quad (* 2 x1))))
          0
          (- y0)))

(defn velocity-range-B
  [[x0 x1 y0 y1]]
  (let [delta-x (- x1 x0)
        vx-min (int (Math/floor (solve-quad (* 2 x0))))]
    (vector vx-min
            (int (/ x1 2))
            (int (/ (* y0 (/ x1 2)) x1))
            0)))

(defn velocity-range-C
  "Velocity range for area C"
  [[x0 x1 y0 y1]]
  (vector x0 x1 y0 y1))

(defn max-y
  "Find the maximum y value in the trajectory or return nil"
  [traj]
  (if (nil? traj)
    nil
    (->> traj
        (map second)
        (apply max))))

(defn try-trajectories
  [target]
  (let [[vx0 vx1 vy0 vy1] (velocity-range-A target)]
    (for [vx (range vx0 vx1)
          vy (range vy0 vy1)]
      (max-y (trajectory [0 0 vx vy] target)))))

(defn try-trajectories-2
  [target]
  (for [[vx0 vx1 vy0 vy1] (vector (velocity-range-A target)
                                  (velocity-range-B target)
                                  (velocity-range-C target))
        vx (range (- vx0 2) (+ 2 vx1))
        vy (range (- vy0 2) (+ 2 vy1))]
    (trajectory [0 0 vx vy] target)))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-target
       try-trajectories
       (remove nil?)
       (apply max)))

(defn part2
  [f]
  (->> f
       read-target
       try-trajectories-2
       (remove nil?)
       (map (comp (partial drop 2) first))
       distinct
       count))

;; The End
