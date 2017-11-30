#!/usr/bin/env lumo
(require '[lumo.core]
         '[child_process :refer [exec]]
         '[fs :refer [readdirSync]])

(def files (readdirSync ""))

(defn unzip [file]
  (exec (str "unzip " file) println))

(dorun (map unzip files))
