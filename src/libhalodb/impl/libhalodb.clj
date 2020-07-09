(ns libhalodb.impl.libhalodb
  (:require
   [clj-halodb.core :as halodb])
  (:gen-class
   :methods [^{:static true} [halodbOpen [] String]
             ^{:static true} [halodbPut [String String] String]
             ^{:static true} [halodbGet [String] String]]))

(set! *warn-on-reflection* true)

(System/setProperty "org.caffinitas.ohc.allocator" "unsafe")

(def dbpath
  ".halodb")

(def options
  (halodb/options
    {:max-file-size 131072
     :sync-write true}))

(def db
  (atom nil))

(defn open []
  (halodb/open dbpath options))

(defn -halodbOpen []
  (reset! db (open))
  "halodb opened.")

(defn -halodbPut [k v]
  (let [db (deref db)]
    (doall
      (halodb/put db {k v})))
  (str "halodb stored. " k ": " v))

(defn -halodbGet [k]
  (let [db (deref db)]
    (halodb/get db k)))
