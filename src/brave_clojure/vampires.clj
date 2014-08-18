;; Notes on "Core Functions" chapter.

(ns brave-clojure.vampires
  (:require
   [clojure.string :as s]
   [clojure.tools.trace  :refer :all]))

;; Infinate sequences

(defn even-numbers
  "Consing a lazy list which includes a recipe for the next element"
  ([] (even-numbers 0))
  ([n] (cons n (lazy-seq (even-numbers (+ n 2))))))

(defn my-conj
  "Define conj in terms of into."
  [target & additions]
  (into target additions))

;; Functions that accept and return functions

(defn my-into
  "define into in terms of conj and apply"
  [target additions]
  (apply conj target additions))

(defn my-partial
  "Define a partial in terms of apply and into"
  [partialized-fn & args]
  (fn [& more-args]
    (apply partialized-fn (into args more-args))))

;; (def add30 (my-partial + 30))
;; (add30 3)

(defn my-complement
  [fun]
  (fn [& args]
    (not (apply fun args))))

;; FWPD

(def filename "suspects.csv")

(def headers->keywords {"Name" :name
                        "glitter-index" :glitter-index})

(defn str->int
  [str]
  (Integer. str))

(def conversions {:name identity
                    :glitter-index str->int})

(defn parse
  "Convert a csv into rows of columns"
  [string]
  (map #(s/split % #",")
       (s/split string #"\n")))

(defn mapify
  "Return a seq of maps like {:name \"Edward\" :glitter-index 10}"
  [rows]
  (let [headers (map #(get headers->keywords %) (first rows))
        unmapped-rows (rest rows)]
    (map (fn [unmapped-row]
           (into {}
                 (map (fn [header column]
                        ;; TODO: Fix NullPointerException
                        [header ((get conversions header) column)])
                      headers
                      unmapped-row)))
         unmapped-rows)))

(defn mapify-row
  [headers unmapped-row]
  (map (fn [header column]
         [header ((get conversions header) column)])
       headers
       unmapped-row))

(defn glitter-filter
  [minimum-glitter records]
  (filter #(>= (:glitter-index %) minimum-glitter) records))

;; (mapify (parse (slurp (clojure.java.io/resource "../resources/suspects.csv"))))


















