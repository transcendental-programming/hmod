(
 ns hmod.core
  (:require [clojure.java.io :as io]
            [hmod.java-out :refer [java-out]])
  (:use [camel-snake-kebab.core])
  (:gen-class))

;; .hmod loading

(defn ls
  [path]
  (map (fn [dir] (str path "/" dir))
       (seq (.list (io/file path)))))

(defn eval-string
  [str]
  (eval (read-string str)))

(defn load-concepts
  [path]
  (apply concat (map #(eval-string (slurp %))
                     (ls path))))

;; compiler

(defn compile-field
  [field]
  (assoc field
         :name (:name field)
         :type (:type field "String")
         :kind (:kind field :regular)))

(defn field
  [name type kind]
  {:name name :type type :kind kind})


(defn child-field
  [type-name]
  {:name (str (->camelCase type-name) "s")
   :type type-name
   :many true
   :kind :child})

(defn list-all-fields
  [concept]
  (concat (:fields concept)
          (if-let [index (:indexed-by concept)] [(field "id" index :id)])
          (if-let [parent (:parent concept)] [(field "parent" parent :parent)])
          (map child-field (:childs concept))))

(defn compile-concept
  [concept]
  (assoc concept :fields (map compile-field (list-all-fields concept))))

(defn compile-flat-childs
  [concept]
  (assoc concept :childs (map :name (:childs concept))))

(defn compile-parent-concept
  [concept]
  (compile-concept (compile-flat-childs concept)))

(defn compile-child-concept
  [concept parent-name]
  (compile-concept (assoc concept :parent parent-name)))

(defn compile-compound-concept
  [concept]
  (if (= (:kind concept) "enum")
    [concept]
    (concat [(compile-parent-concept concept)]
            (map #(compile-child-concept % (:name concept)) (:childs concept)))))

;; Linking stage

(def base-types {"String" {:name "String"}
                 "boolean" {:name "boolean"}
                 "Date" {:name "Date"}
                 "Integer" {:name "Integer"}
                 "double"  {:name "double"}
                 "int" {:name "int"}
                 "Long" {:name "Long"} })

(defn list-available-types
  [concepts]
  (merge (zipmap (map :name concepts) concepts)
         base-types))

(defn link-type
  [type-name available-types]
  (if-let [type (get available-types type-name)]
    type
    (println (str "Linking error: Type `" type-name "` not found."))))

(defn link-fields
  [concept available-types]
  (if-let [fields (:fields concept)]
    (assoc concept :fields (map #(assoc % :type
                                       (link-type (:type %) available-types))
                                fields))
    concept))

(defn link-index
  [concept available-types]
  (if-let [indexed-by (:indexed-by concept)]
    (assoc concept :indexed-by (link-type indexed-by available-types))
    concept))

(defn link-parent
  [concept available-types]
  (if-let [parent (:parent concept)]
    (assoc concept :parent (link-type parent available-types))
    concept))

(defn link-concept
  [concept available-types]
  (link-fields (link-index (link-parent concept
                                        available-types)
                           available-types)
               available-types))

(defn retrieve-concepts
  [path]
  (let [compiled-concepts (apply concat
                                 (map compile-compound-concept
                                      (load-concepts path)))]
    (let [available-types (list-available-types compiled-concepts)]
      (map #(link-concept % available-types) compiled-concepts))))
