(ns advent-2021.day21-test
  (:require [clojure.test :refer [deftest is]]
            [advent-2021.util :as util]
            [advent-2021.day21 :as d]))

(deftest sanity
  (is (= 5 (+ 2 3))))

(deftest modulo
  (is (= 10 (d/mod-add 4 6 10)))
  (is (= 1 (d/mod-add 4 7 10))))
