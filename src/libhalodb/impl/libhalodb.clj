(ns libhalodb.impl.libhalodb
  (:require
   [clj-halodb.core :as halodb])
  (:gen-class
   :methods [^{:static true} [halodbOpen [String] Boolean]
             ^{:static true} [halodbClose [] Boolean]
             ^{:static true} [halodbSize [] Long]
             ^{:static true} [halodbPut [String String] Boolean]
             ^{:static true} [halodbGet [String] String]
             ^{:static true} [halodbDelete [String] Boolean]]))

(set! *warn-on-reflection* true)

(System/setProperty "org.caffinitas.ohc.allocator" "unsafe")

(def options
  (halodb/options
    {:max-file-size 131072
     :sync-write true}))

(def db
  (atom nil))

(defn open [dbpath]
  (halodb/open dbpath options))

(defn -halodbOpen [path]
  (reset! db (open path))
  true)

(defn -halodbClose []
  (let [db (deref db)]
    (when db
      (halodb/close db)))
  (reset! db nil)
  true)

(defn -halodbSize []
  (let [db (deref db)]
    (if db
      (halodb/size db)
      0)))

(defn -halodbPut [k v]
  (let [db (deref db)]
    (if db
      (do
        (doall
          (halodb/put db {k v}))
        true)
      false)))

(defn -halodbGet [k]
  (let [db (deref db)]
    (when db
      (halodb/get db k))))

(defn -halodbDelete [k]
  (let [db (deref db)]
    (if db
      (do
        (halodb/delete db k)
        true)
      false)))
