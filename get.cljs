#!/usr/bin/env lumo
(require 'lumo.core 'https)

(def url
  (or (first cljs.core/*command-line-args*)
      "https://duckduckgo.com"))

(https/get url (fn [res]
                 (.on res "data"
                      #(println (.toString %)))))
