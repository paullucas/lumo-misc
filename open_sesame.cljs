#!/usr/bin/env lumo 
(require '[cljs.nodejs :as nodejs])
(def http (nodejs/require "http"))
(def child-process (nodejs/require "child_process"))

(.listen
 (.createServer
  http
  (fn [req res]
    (.writeHead res 200 #js {"Content-Type" "text/plain"})
    (.execSync child-process "FOOBAR")
    (.end res "Open sesame")))
 3000 "127.0.0.1")

(println "Now listening on localhost:3000")
