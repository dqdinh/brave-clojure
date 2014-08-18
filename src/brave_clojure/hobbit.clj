;; Notes on "Do Things" chapter.

(ns brave-clojure.hobbit
  (:require
   [clojure.tools.trace  :refer :all]))

(def asym-hobbit-body-parts [{:name "head" :size 3}
                             {:name "left-eye" :size 1}
                             {:name "left-ear" :size 1}
                             {:name "mouth" :size 1}
                             {:name "nose" :size 1}
                             {:name "neck" :size 2}
                             {:name "left-shoulder" :size 3}
                             {:name "left-upper-arm" :size 3}
                             {:name "chest" :size 10}
                             {:name "back" :size 10}
                             {:name "left-forearm" :size 3}
                             {:name "abdomen" :size 6}
                             {:name "left-kidney" :size 1}
                             {:name "left-hand" :size 2}
                             {:name "left-knee" :size 2}
                             {:name "left-thigh" :size 4}
                             {:name "left-lower-leg" :size 3}
                             {:name "left-achilles" :size 1}
                             {:name "left-foot" :size 2}])

(defn has-matching-part?
  [part]
  (re-find #"^left-" (:name part)))

(defn matching-part
  [part]
  {:name (clojure.string/replace (:name part) #"^left-" "right-")
   :size (:size part)})

(defn symmetrize-body-parts-without-using-let
  "Expects a seq of maps which have a :name and :size. Written without using let."
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
        (if (has-matching-part? (first remaining-asym-parts))
          (recur (rest remaining-asym-parts)
                 (conj (conj final-body-parts (first remaining-asym-parts))
                       (matching-part (first remaining-asym-parts))))
          (recur (rest remaining-asym-parts)
                 (conj final-body-parts (first remaining-asym-parts)))))))

(defn symmetrize-body-parts
  "Expects a seq of maps which have a :name and :size"
  [asym-body-parts]
  (loop [remaining-asym-parts asym-body-parts
         final-body-parts []]
    (if (empty? remaining-asym-parts)
      final-body-parts
        (let [[part & remaining] remaining-asym-parts
              final-body-parts (conj final-body-parts part)]
          (if (has-matching-part? part)
            (recur remaining (conj final-body-parts (matching-part part)))
            (recur remaining final-body-parts))))))

(defn better-symmetrize-body-parts
  "Expects a seq of maps which have a :name and :size. Refactored."
  [asym-body-parts]
  (reduce (fn [final-body-parts part]
            (let [final-body-parts (conj final-body-parts part)]
              (if (has-matching-part? part)
                (conj final-body-parts (matching-part part))
                final-body-parts)))
          []
          asym-body-parts))

(defn my-reduce
  "This is a reimplementation of reduce to understand how reduce works."
  ([f initial coll]
   (loop [result initial
          remaining coll]
     (let [[current & rest] remaining]
       (if (empty? remaining)
         result
         (recur (f result current) rest)))))
   ([f [head & tail]]
    (my-reduce f (f head (first tail)) (rest tail))))

(defn hit
  "Determine which part of the hobbit gets hit."
  [asym-body-parts]
  (let [sym-parts (better-symmetrize-body-parts asym-body-parts)
        body-part-size-sum (reduce + 0 (map :size sym-parts))
        target (inc (rand body-part-size-sum))]
    (loop [[part & rest] sym-parts
           accumulated-size (:size part)]
      (if (> accumulated-size target)
        part
        (recur rest (+ accumulated-size (:size part)))))))

