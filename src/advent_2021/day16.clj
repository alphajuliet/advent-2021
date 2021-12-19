(ns advent-2021.day16
  (:require [advent-2021.util :as util]
            [instaparse.core :as insta]
            [clojure.zip :as zip]
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
  (insta/parser
   "S := Packet Padding?
    Packet := Ver (Lit | Op)
    Ver := #'[01]{3}'

    Op := <OpType> <Len> Packet*
    <OpType> := '000' | '001' | '010' | '011' | '101' | '110' | '111'
    <Len> := ('0' #'[01]{15}') | ('1' #'[01]{11}')

    Lit := <LitType> LitNF* LitF
    <LitType> := '100'
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

(defn part1
  [f]
  (->> f
       read-packet
       packet
       sum-versions))

;; The End
