#!/usr/bin/env lumo 
(require 'lumo.core 'fs 'http)
(def html (fs/readFileSync "index.html"))

(-> (http/createServer
     (fn [req res]
       (.writeHead res 200 #js {"Content-Type" "text/html"})
       (.end res html)))
    (.listen 3000 "127.0.0.1"))

(println "Now listening on localhost:3000")
