(ns advent-2021.day11
  (:require [advent-2021.util :as util]
            [clojure.math.combinatorics :as combo]
            [clojure.core.matrix :as m]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day11-input.txt")
(def testf  "data/day11-test.txt")

(defn vector-add
  "Add 2D vectors"
  [[x1 y1] [x2 y2]]
  [(+ x1 x2) (+ y1 y2)])

(defn read-cavern
  "Read in the matrix of initial energy states."
  [f]
  (->> f
       util/import-data
       (mapv #(str/split % #""))
       (util/mapmap #(Integer/parseInt %))))

(defn invalid-location?
  "Is this a valid location in a matrix of a given size?"
  [[r c] [max-r max-c]]
  (or (neg? r)
      (neg? c)
      (> r (dec max-r))
      (> c (dec max-c))))

(defn neighbours
  "Generate all neighbouring locations."
  [loc]
  (let [deltas (combo/cartesian-product [-1 0 1] [-1 0 1])]
    (->> deltas
         (map (partial vector-add loc))
         (util/swap util/delete loc))))

(defn mupdate
  "Map over a single matrix value."
  [m r c f]
  (m/mset m r c (f (m/mget m r c))))

(defn inc-but-zero
  "Only inc if not zero"
  [x]
  (if (zero? x) x (inc x)))

(defn all-locs
  "Generate all the locations in a matrix."
  [mat]
  (let [[r c] (m/shape mat)]
    (combo/cartesian-product (range r) (range c))))

(defn increase-neighbours
  "Increase the energy of all the immediate neighbours, unless zero."
  [mat nlocs]
  (let [max-rc (m/shape mat)]
    (reduce (fn [m [r c]]
              (if (invalid-location? [r c] max-rc)
                m
                (mupdate m r c inc-but-zero)))
            mat
            nlocs)))

(defn do-cell
  "Process a single cell."
  [{:keys [:mat] :as state} [r c] max-rc]
  (if (> (m/mget mat r c) 9)
    (let [nlocs (neighbours [r c])]
      (-> state
          (update :mat #(m/mset % r c 0))
          (update :total inc)
          (update :mat #(increase-neighbours % nlocs))))
    ;; else
    state))

(defn single-pass
  "Do a single pass across each cell."
  [state]
  (let [max-rc (m/shape (:mat state))
        ;; state' (update state :mat (partial m/emap inc))
        locs (all-locs (:mat state))]
    (reduce (fn [st loc]
              (do-cell st loc max-rc))
            state
            locs)))

(defn step
  "Keep running passes across the matrix until the total updates stops increasing."
  [state]
  (let [state' (update state :mat #(m/emap inc %))
        max-iter 20]
    (reduce (fn [st _]
              (let [st' (single-pass st)]
                (if (= (:total st') (:total st))
                  (reduced st')
                  ;;else
                  st')))
            state'
            (range max-iter))))

(defn run-steps
  "Run n steps across the matrix."
  [n mat]
  (let [state {:mat mat :total 0}]
    (reduce (fn [st i]
              (let [st' (step st)]
                (if (m/zero-matrix? (:mat st'))
                  (reduced {:step i})
                  st')))
            state
            (range n))))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-cavern
       (run-steps 100)
       :total))

(defn part2
  [f]
  (->> f
       read-cavern
       (run-steps 1000)
       :step
       inc))

;; The End
