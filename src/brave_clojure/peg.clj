;; Notes on "functional-programming" chapter.

(ns brave-clojure.peg
  (:require
   [clojure.tools.trace  :refer :all]))

;; Sum fn
(defn sum1
  ([vals]
    (sum1 vals 0))
  ([vals acc]
   (if (empty? vals)
     acc
     (recur (rest vals) (+ (first vals) acc)))))
