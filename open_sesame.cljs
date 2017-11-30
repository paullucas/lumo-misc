#!/usr/bin/env lumo 
(require 'lumo.core 'https 'child_process)

(-> (https/createServer
     (fn [req res]
       (.writeHead res 200 #js {"Content-Type" "text/plain"})
       (child_process/execSync "FOOBAR")
       (.end res "Open sesame")))
    (.listen 3000 "127.0.0.1"))

(println "Now listening on localhost:3000")
