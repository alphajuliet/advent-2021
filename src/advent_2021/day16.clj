(ns advent-2021.day16
  (:require [advent-2021.util :as util]
            [instaparse.core :as insta]
            [clojure.string :as str]))

;;--------------------------------
(def inputf "data/day16-input.txt")
(def testf  "data/day16-test.txt")

(def t1 "D2FE28")
(def t2 "38006F45291200")
(def t3 "EE00D40C823060")
(def t4 "8A004A801A8002F478")

(defn hex->bin
  "Convert a hex number to binary string"
  [h]
  (Integer/toBinaryString (Integer/parseInt h 16)))

(defn hexstr->binstr
  "Parse a string of hex into a string of binary"
  [s]
  (->> s
       (map (comp hex->bin str))
       (map #(util/left-pad % 4 "0"))
       str/join))

(defn read-packet
  "Read the packet from a file."
  [f]
  (-> f
      util/import-data
      first
      hexstr->binstr))

(def packet
  ;; Grammar for packets

  ;; <Op> := Calculate
  ;; <Calculate> := Compare | Sum | Product | Minimum | Maximum
  ;; <Compare> := GreaterThan | LessThan | EqualTo
  (insta/parser
   "S := Packet <Padding?>
    <Packet> := <Ver> (Lit | Op)
    Ver := #'[01]{3}'

    <Op> := Sum | Product | Minimum | Maximum | GreaterThan | LessThan | EqualTo
    Len := LenBits | LenSubP
    LenBits := <'0'> #'[01]{15}'
    LenSubP := <'1'> #'[01]{11}'

    Sum           := <'000'> <Len> Packet+
    Product       := <'001'> <Len> Packet+
    Minimum       := <'010'> <Len> Packet+
    Maximum       := <'011'> <Len> Packet+
    GreaterThan   := <'101'> <Len> Packet Packet
    LessThan      := <'110'> <Len> Packet Packet
    EqualTo       := <'111'> <Len> Packet Packet

    Lit := <'100'> LitNF* LitF
    <LitNF> := #'1[01]{4}+'
    <LitF>  := #'0[01]{4}'

    Padding := #'0+'"))

;; Parse a hex string
;; (parse-packet s)
;; (insta/set-default-output-format! :hiccup)
(def parse-packet (comp packet hexstr->binstr))

(defn sum-versions
  [tree]
  (->> tree
       (insta/transform {:Ver #(vector :Ver (Integer/parseInt % 2))})
       flatten
       (filter number?)
       (apply +)))

(defn bool->int [b] (if (true? b) 1 0))

(defn eval-tree
  "Evaluate the tree"
  [tree]
  (insta/transform
   {:S identity
    :Lit #(Integer/parseInt % 2)
    :Sum +
    :Product *
    :Minimum min
    :Maximum max
    :GreaterThan (comp bool->int >)
    :LessThan (comp bool->int <)
    :EqualTo (comp bool->int =)}
   tree))

(defn part1
  [f]
  (->> f
       read-packet
       packet
       sum-versions))

(defn part2
  [f]
  (->> f
       read-packet
       packet
       eval-tree))

;; The End
