(ns advent-2021.day02
  (:require [advent-2021.util :as util]
            [clojure.string :as str]))

(def inputf "data/day02-input.txt")
(def testf "data/day02-test.txt")

(defn read-directions
  "Read and parse the directions"
  ;; read-directions :: IO String -> List (Vector String Int)
  [f]
  (->> f
    util/import-data
    (map #(str/split % #"\s+"))
    (map (fn [[dir dist]] (vector dir (Integer/parseInt dist))))))

(defn move-1
  "Update the state according to the instruction"
  ;; move-1 :: State -> (Vector String Int) -> State
  [state [dir dist]]
  (case dir
    "down"      (update state :v (partial + dist))
    "up"        (update state :v (partial util/swap - dist))
    "forward"   (update state :h (partial + dist))
    state))

(defn move-2
  "Update the state according to the instruction"
  ;; move-2 :: State -> (Vector String Int) -> State
  [state [dir dist]]
  (case dir
    "down"      (update state :aim (partial + dist))
    "up"        (update state :aim (partial util/swap - dist))
    "forward"   (-> state
                    (update :h (partial + dist))
                    (update :v (partial + (* dist (:aim state)))))
    state))

(defn part1
  [f]
  (->> f
       read-directions
       (reduce move-1 {:h 0 :v 0})
       (#(* (:h %) (:v %)))))

(defn part2
  [f]
  (->> f
       read-directions
       (reduce move-2 {:h 0 :v 0 :aim 0})
       (#(* (:h %) (:v %)))))

;; The End
