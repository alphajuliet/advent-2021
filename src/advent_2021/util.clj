(ns advent-2021.util
  (:require [clojure.string :as str]
            [clojure.pprint :as pp]))

;;--------------------------------
(defn swap [f x y] (f y x))

(defn third
  [coll]
  (nth coll 2))

;; filter-if :: ∀ a. List Boolean -> List a -> List a
(defn filter-if
  "Filter c2 according to the truth of the corresponding element in c1.
  (filter-if [true false true] [1 2 3]) => [1 3]"
  [c1 c2]
  (remove nil? (map #(if %1 %2 nil) c1 c2)))

(defn delete
  "Delete all instances of elt in coll."
  [coll elt]
  (remove #(= % elt) coll))

;; Transpose a list of lists
(def T (partial apply mapv vector))

(defn binv->dec
  "Convert a binary vector to decimal"
  [v]
  (Integer/parseInt (str/join "" v) 2))

(defn ones-complement
  "Flip the bits in a binary vector"
  [v]
  (mapv #(if (= % 1) 0 1) v))

(defn number-of-bits
  "Number of bits required to store `n`."
  ;; number-of-bits :: Number -> Integer
  [n]
  (if (pos? n)
    (-> (Math/log n)
        (/ (Math/log 2))
        inc
        Math/floor
        int)
    ;; else
    0))

(defn import-data
  "Import and prepare the data"
  ;; import-data :: IO File -> List String
  [f]
  (->> f
       (slurp)
       (str/split-lines)))

(defn string->int
  [s]
  (Integer/parseInt s))

(defn left-pad
  "If S is shorter than LEN, pad it with CH on the left."
  ;; left-pad :: String -> Int -> String -> String
  ([s len]
   (left-pad s len " "))
  ([s len ch]
   (pp/cl-format nil (str "~" len ",'" ch "d") (str s))))

(defn rotate
  "Rotate collection by n to the left. If n is negative rotates to the right."
  ;; rotate :: Int -> Coll a -> Coll a
  [n coll]
  (let [shift (mod n (count coll))]
    (into [] (concat (drop shift coll)
                     (take shift coll)))))

(defn rotate-string
  "Rotate a string."
  ;; rotate-string :: Int -> String -> String
  [n s]
  (apply str (rotate n s)))

(defn swap-elements
  "Swap elements i and j in coll."
  ;; swap-elements :: Coll a -> Int -> Int -> Coll a
  [coll i j]
  (let [a (nth coll i)
        b (nth coll j)]
    (-> coll
        (assoc i b)
        (assoc j a))))

(defn count-if
  "Utility function"
  ;; count-if :: (a -> Bool) -> Coll a -> Int
  [f v]
  (count (filter f v)))

(defn take-until
  "Returns a lazy sequence of successive items from coll until
   (pred item) returns true, including that item. pred must be
   free of side-effects."
  [pred coll]
  (lazy-seq
    (when-let [s (seq coll)]
      (if (pred (first s))
        (cons (first s) nil)
        (cons (first s) (take-until pred (rest s)))))))

(defn mapmap
  "Do a deep map of 2-level structure."
  [f x]
  (mapv (fn [y] (mapv f y)) x))

(defn map-kv
  "Map f over the values of a map."
  [f coll]
  (reduce-kv (fn [m k v] (assoc m k (f v))) (empty coll) coll))

(defn map-key
  "Map g over all keys in m"
  [g m]
  (into {} (map (juxt (comp g key) val)) m))

(defn remove-keys
  "Opposite of select-keys"
  [m ks]
  (select-keys m (remove #(contains? (set ks) %) (keys m))))

(defn clamp
  "Clamp values between a and b"
  [x a b]
  (min b (max x a)))

(defn argmax
  "Return the value x in xs that maximises (f x)."
  [f xs]
  (apply max-key f xs))

(defn argmin
  "Return the value x in xs that minimises (f x)."
  [f xs]
  (apply min-key f xs))

;; The End
