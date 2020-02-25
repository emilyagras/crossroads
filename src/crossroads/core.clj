(ns crossroads.core
  (:gen-class)
  (:require [ring.util.response :as ring-resp]))

(defn file-exists? [filename]
  (.exists (clojure.java.io/as-file filename)))

(defn random-flaky-fn []
  "let's pretend i work up here")

(defn db-layer-write-fn! [filename record]
  (if (file-exists? filename)
    (do (spit filename record :append true)
        (ring-resp/response "ok"))
    (ring-resp/bad-request "not ok")))

(defn just-some-pure-function [filename record]
  (println "I'm just here to be an extra layer of complexity")
  (if (= "sad thing happened" (random-flaky-fn))
    (ring-resp/status 500)
    (db-layer-write-fn! filename record)))

(defn web-handler-layer-fn [filename record]
  (just-some-pure-function filename record))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println "Hello, World!"))

