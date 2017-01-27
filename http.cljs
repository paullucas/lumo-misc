#!/usr/bin/env lumo 
(require '[cljs.nodejs :as nodejs])
(def fs (nodejs/require "fs"))
(def http (nodejs/require "http"))
(def html (.readFileSync fs "index.html"))

(.listen
 (->>
  (fn [req res]
    (.writeHead res 200 #js {"Content-Type" "text/html"})
    (.end res html))
  (.createServer http))
 3000 "127.0.0.1")

(println "Now listening on localhost:3000")
