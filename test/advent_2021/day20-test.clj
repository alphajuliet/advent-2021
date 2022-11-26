(ns advent-2021.day20-test
  (:require [clojure.test :refer [deftest is]]
            [advent-2021.util :as util]
            [advent-2021.day20 :as d]))

(deftest sanity
  (is (= 5 (+ 2 3))))

(deftest utils
  (is (= 1 (d/char->digit \#)))
  (is (= 0 (d/char->digit \.))))
