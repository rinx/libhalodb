(ns libhalodb.impl.libhalodb
  (:require
   [clj-halodb.core :as halodb])
  (:gen-class
   :methods [^{:static true} [halodbOpen [String] Integer]
             ^{:static true} [halodbClose [] Integer]
             ^{:static true} [halodbSize [] Long]
             ^{:static true} [halodbPut [String String] Integer]
             ^{:static true} [halodbGet [String] String]
             ^{:static true} [halodbDelete [String] Integer]
             ^{:static true} [halodbPauseCompaction [] Integer]
             ^{:static true} [halodbResumeCompaction [] Integer]]))

(set! *warn-on-reflection* true)

(System/setProperty "org.caffinitas.ohc.allocator" "unsafe")

(def options
  (halodb/options
    {:max-file-size (* 1024 1024)
     :sync-write false
     :use-memory-pool true
     :memory-pool-chunk-size (* 1024 (* 16 1024))
     :compaction-job-rate (* 1024 (* 50 1024))
     :fixed-key-size 256}))

(def db
  (atom nil))

(defn open [dbpath]
  (halodb/open dbpath options))

(defn -halodbOpen [path]
  (int
    (try
      (reset! db (open path))
      0
      (catch Exception e
        (println e)
        1))))

(defn -halodbClose []
  (int
    (let [hdb (deref db)]
      (if (halodb/db? hdb)
        (try
          (halodb/close hdb)
          (reset! db nil)
          0
          (catch Exception e
            (println e)
            1))
        -1))))

(defn -halodbSize []
  (let [db (deref db)]
    (if (halodb/db? db)
      (try
        (halodb/size db)
        (catch Exception e
          (println e)
          -2))
      -1)))

(defn -halodbPut [k v]
  (int
    (let [db (deref db)]
      (if (halodb/db? db)
        (try
          (doall
            (halodb/put db {k v}))
          0
          (catch Exception e
            (println e)
            1))
        -1))))

(defn -halodbGet [k]
  (let [db (deref db)]
    (if (halodb/db? db)
      (try
        (halodb/get db k)
        (catch Exception e
          (println e)
          ""))
      "")))

(defn -halodbDelete [k]
  (int
    (let [db (deref db)]
      (if (halodb/db? db)
        (try
          (halodb/delete db k)
          0
          (catch Exception e
            (println e)
            1))
        -1))))

(defn -halodbPauseCompaction []
  (int
    (let [db (deref db)]
      (if (halodb/db? db)
        (try
          (halodb/pause-compaction db)
          0
          (catch Exception e
            (println e)
            1))
        -1))))

(defn -halodbResumeCompaction []
  (int
    (let [db (deref db)]
      (if (halodb/db? db)
        (try
          (halodb/resume-compaction db)
          0
          (catch Exception e
            (println e)
            1))
        -1))))
