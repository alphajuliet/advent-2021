(ns advent-2021.day18
  (:require [advent-2021.util :as util]
            [com.rpl.specter :as sp]
            ;; [clojure.core.match :as match]
            ;; [meander.epsilon :as m]
            ;; [meander.strategy.epsilon :as m*]
            [instaparse.core :as insta]))

;;--------------------------------
(def inputf "data/day18-input.txt")
(def test1f  "data/day18-test1.txt")
(def test2f  "data/day18-test2.txt")


(def pairs
  "Create a grammar for the snailfish numbers, aka binary trees"
  (insta/parser
   "<S>     := Pair
    Pair    := <'['> Left <','> Right <']'>
    Left    := Pair | Number
    Right   := Pair | Number
    Number  := #'\\d+'"))

(defn read-trees
  "Read in the binary trees as annotated vectors."
  [f]
  (->> f
       util/import-data
       (map pairs)))

(defn simplify
  "Reduce a tree to a nested vector."
  [tree]
  (first
   (insta/transform
    {:Pair #(vector %1 %2)
     :Left identity
     :Right identity
     :Number #(Integer/parseInt %)}
    tree)))

(defn magnitude
  "Calculate the magnitude of the tree."
  [tree]
  (insta/transform
   {:Pair +
    :Left (partial * 3)
    :Right (partial * 2)
    :Number #(Integer/parseInt %)}
   tree))

(def TREE-VALUES
  "See https://github.com/redplanetlabs/specter/wiki/Using-Specter-Recursively"
  (sp/recursive-path
   [] p (sp/if-path vector? [sp/ALL p] sp/STAY)))

(defn add-tree
  "Add two trees by creating a new supertree"
  [t1 t2]
  (vector t1 t2))

(defn split
  "Split operation on a tree"
  [tree]
  (sp/transform [TREE-VALUES (partial < 10)]
                #(vector (int (/ % 2)) (int (Math/ceil(/ % 2))))
                tree))

(defn tree-seq-depth
  "Returns a lazy sequence of vectors of the nodes in a tree and their
  depth as [node depth], via a depth-first walk."
  ;; from https://gist.github.com/stathissideris/1397681b9c63f09c6992
  [branch? children root]
  (let [walk (fn walk [depth node]
               (lazy-seq
                (cons [node depth]
                      (when (branch? node)
                        (if (= depth 4)
                          (comment "### explode here ###")
                          (mapcat (partial walk (inc depth))
                                  (children node)))))))]
    (walk 0 root)))

;;--------------------------------
(defn part1
  [f]
  (->> f
       read-trees
       (map pairs)))

;; The End
