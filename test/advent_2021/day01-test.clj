(ns advent-2021.util-test
  (:require [clojure.test :refer [deftest is]]
            [advent-2021.util :as util]))

(deftest sanity
  (is (= 5 (+ 2 3))))

(deftest numeric
  (is (= 4 (util/number-of-bits 15)))
  (is (zero? (util/number-of-bits -5))))

(deftest predicates
  (let [x (range 5)]
    (is (= 3 (util/count-if even? x)))
    (is (= [0 2 4] (util/filter-if [true false true false true] x)))))

(deftest strings
  (is (= 123 (util/string->int "123")))
  (is (= -456 (util/string->int "-456")))
  (is (= " 1" (util/left-pad "1" 2)))
  (is (= "001" (util/left-pad "1" 3 "0")))
  (is (= "bca" (util/rotate-string 1 "abc")))
  (is (= "cab" (util/rotate-string -1 "abc"))))

(deftest collections
  (is (= [2 3 1] (util/rotate 1 [1 2 3])))
  (is (= [1 4 3 2] (util/swap-elements [1 2 3 4] 1 3)))
  (is (= [1 3] (util/delete [1 2 3 2] 2))))

;; The End
