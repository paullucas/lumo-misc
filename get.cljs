#!/usr/bin/env lumo 
(require '[cljs.nodejs :as nodejs])
(def https (nodejs/require "https"))

(.get
 https
 "..."
 (fn [res]
   (.on res "data"
        (fn [d]
          (println (.toString d))))))
