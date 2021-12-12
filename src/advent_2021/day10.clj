(ns advent-2021.day10
  (:require [advent-2021.util :as util]
            [clojure.string :as str]
            [instaparse.core :as insta]))

;;--------------------------------
(def inputf "data/day10-input.txt")
(def testf  "data/day10-test.txt")

(defn read-lines
  [f]
  (-> f
      util/import-data))

(def chunker
  (insta/parser
   "S := ('<' S* '>'
       | '(' S* ')'
       | '[' S* ']'
       | '{' S* '}')+  "))

(defn parse-line
  "Parse a line against the chunk rules."
  [line]
  (let [result (chunker line)
        len (count line)]
    (if (vector? result)
      :ok
      ;;else error
      (if (or (<= len (:index result))
              (contains? (get-in result [:reason 0]) :full))
        :incomplete
        (nth line (:index result))))))

(defn score-char-1
  "Scoring table for part 1"
  [ch]
  (case ch
    \) 3
    \] 57
    \} 1197
    \> 25137
    0))

(defn score-char-2
  "Scoring table for part 2"
  [ch]
  (case ch
    ")" 1
    "]" 2
    "}" 3
    ">" 4
    0))

(defn complete-chunk
  "Complete the chunk with the missing closing brackets."
  [s]
  (let [result (chunker s)]
    (if (vector? result)
      s
      (complete-chunk (str s (get-in result [:reason 0 :expecting]))))))

(defn find-completion
  "Return just the completion of the chunk."
  [s]
  (subs (complete-chunk s) (count s)))

(defn score-completion
  "Score the completion string for part 2."
  [s]
  (reduce (fn [s ch]
            (+ (* 5 s) (score-char-2 ch)))
          0
          (str/split s #"")))

(defn middle
  "Find the middle number in the collection when sorted."
  [coll]
  (let [len (count coll)]
    (nth (sort < coll) (Math/floor (/ len 2)))))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-lines
       (map parse-line)
       (map score-char-1)
       (apply +)))

(defn part2
  [f]
  (let [lines (read-lines f)]
    (->> lines
         (map parse-line)
         (map (partial = :incomplete))
         (util/swap util/filter-if lines)
         (map find-completion)
         (map score-completion)
         middle)))

;; The End
