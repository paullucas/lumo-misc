#!/usr/bin/env lumo
(require '[lumo.core]
         '[child_process :refer [exec]])

(def urls [])

(defn wget [url]
  (exec (str "wget -nv " url) println))

(dorun (map wget urls))
