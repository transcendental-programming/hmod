(ns hmod.java-out
  (:require [clojure.java.io :as io])
  (:use [camel-snake-kebab.core])
  (:gen-class))

(defn g-package
  [package]
  (str "package " package ";\n"))

(defn g-imports
  []
  "import java.util.*;\n")

(defn g-begin-class
  [name]
  (str "public class " name " {\n"))

(defn g-begin-static-class
  [name]
  (str "public static class " name " {\n"))

(defn g-end-class [] "}\n")

(defn g-type-name
  [field]
  (let [base-name (get-in field [:type :name])]
    (if (:many field)
      (str "List<" base-name ">")
      base-name)))

(defn g-declaration
  [field]
  (str (g-type-name field) " " (:name field)))

(defn g-member
  [field]
  (str "private " (g-declaration field) ";\n"))

(defn g-declare-params
  [fields]
  (str "("
       (clojure.string/join ", " (map g-declaration fields))
       ")"))

(defn g-method
  [type name fields body]
  (str "public " type " " name (g-declare-params fields) " { " body " }\n"))

(defn g-get
  [field]
  (g-method (g-type-name field)
            (str "get" (->PascalCase (:name field)))
            []
            (str "return " (:name field) ";")))

(defn g-assign
  [target source]
  (str target " = " source ";"))

(defn g-assign-this
  [name]
  (g-assign (str "this." name) name))

(defn g-builder-set-general
  [field]
  (g-method "Builder"
            (:name field)
            [field]
            (str (g-assign-this (:name field))
                 " return this;")))

(defn g-builder-set
  [field]
  (case (:kind field)
    :regular (g-builder-set-general field)
    :child (g-builder-set-general field)
    :parent ""
    :id ""))


(defn list-id-fields
  [fields]
  (filterv #(= (:kind %) :id) fields))

(defn list-child-fields
  [fields]
  (filterv #(= (:kind %) :child) fields))

(defn list-not-id-fields
  [fields]
  (filterv #(not= (:kind %) :id) fields))

(defn g-builder-constructor
  [fields]
  (let [id-fields (list-id-fields fields)]
    (g-method ""
              "Builder"
              id-fields
              (apply str
                     (concat (map #(g-assign-this (:name %)) id-fields)
                             (map #(g-assign (:name %) "new ArrayList<>()")
                                  (list-child-fields fields)))))))

(defn g-list-params
  [fields]
  (str "("
       (clojure.string/join ", " (map :name fields))
       ")"))

(defn g-builder-build
  [name fields]
  (g-method name
            "build"
            []
            (str "return new " name (g-list-params fields) ";")))

(defn g-builder
  [name fields]
  (str "\n// --------------\n"
       (g-begin-static-class "Builder")
       (apply str (map g-member fields))
       (apply str (map g-builder-set fields))
       (g-builder-constructor fields)
       (g-builder-build name fields)
       (g-end-class)))

(defn g-do-attachment
  [field]
  (let [singular-type (get-in field [:type :name])
        singular-name (->camelCase singular-type)]
    (str "\nfor (" singular-type " " singular-name
         " : this." (:name field) ") { " singular-name "._attachParent(this); }\n")))

(defn g-constructor
  [name fields]
  (g-method ""
            name
            fields
            (apply str (concat
                        (map #(g-assign-this (:name %)) fields)
                        (map g-do-attachment (list-child-fields fields))))))

(defn g-attach
  [concept parent]
  (g-method (:name concept)
            "_attachParent"
            [{:name "parent" :type parent}]
            (str (g-assign-this "parent") "return this;")))




(defn g-bean-set
  [field]
  (g-method "void"
            (str "set" (->PascalCase (:name field)))
            [field]
            (g-assign-this (:name field))))

(defn g-bean-build
  [])

(defn g-bean
  [fields]
  (str "\n// -----------\n"
       (g-begin-static-class "Bean")
       (apply str (map g-member fields))
       (apply str (map g-get fields))
       (apply str (map g-bean-set fields))
       (g-end-class)))


(defn g-class
  [package concept]
  (let [name (:name concept)
        fields (:fields concept)]
    (str (g-package package)
         (g-imports)
         (g-begin-class name)
         (apply str (map g-member fields))
         (apply str (map g-get fields))
         (g-constructor name fields)
         (if-let [parent (:parent concept)] (g-attach concept parent))
         (g-builder name fields)
         (g-bean fields)
         (g-end-class))))



(defn g-begin-enum
  [name]
  (str "public enum " name " {\n"))

(defn g-enum-content
  [values]
  (str (clojure.string/join ", " values) "\n"))

(defn g-enum
  [package concept]
  (str (g-package package)
       (g-begin-enum (:name concept))
       (g-enum-content (:values concept))
       (g-end-class)))

(defn g-java
  [package concept]
  (if (= (:kind concept) "enum")
    (g-enum package concept)
    (g-class package concept)))

(defn save-java
  [path package concept]
  (let [directory (str path "/"
                       (clojure.string/replace package #"\." "/"))]
    (spit (str directory "/" (:name concept) ".java")
          (g-java package concept))))

(defn java-out
  [path package concepts]
  (map #(save-java path package %) concepts))
