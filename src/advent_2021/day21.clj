(ns advent-2021.day21
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

(defn mod-add
  "(a + b) mod m"
  [a b m]
  (let [x (mod (+ a b) m)]
    (if (zero? x) m x)))

(defn init-state
  "Initialise the game state"
  [start]
  {:piece start
   :score [0 0]
   :die 0
   :rolls 0
   :total 0})

(defn move-piece
  "Move piece p based on the last total"
  [st p]
  (as-> st <>
    (update-in <> [:piece p] #(mod-add (:total <>) % 10))
    (update-in <> [:score p] #(+ % (get-in <> [:piece p])))))

(defn roll-deterministic-die
  "Roll a deterministic dice"
  [st]
  (-> st
      (update :die #(inc (mod % 100)))
      (update :rolls inc)))

(defn roll-dice
  "Roll the die three times and add up the rolls"
  [st]
  (reduce (fn [s _]
            (as-> s <>
              (roll-deterministic-die <>)
              (update <> :total #(+ (:die <>) %))))
          (assoc st :total 0)
          (range 3)))

(defn play-turn
  "A single player turn"
  [st p]
  (-> st
      roll-dice
      (move-piece p)))

(defn play-game
  [start]
  (loop [st (init-state start)]
    (let [st0 (play-turn st 0)]
      (if (>= (get-in st0 [:score 0]) 1000)
        st0
        (let [st1 (play-turn st0 1)]
          (if (>= (get-in st1 [:score 1]) 1000)
            st1
            (recur st1)))))))

(defn part1
  [start]
  (let [st (play-game start)]
    (* (apply min (:score st))
       (:rolls st))))

;; The End
