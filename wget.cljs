#!/usr/bin/env lumo
(require '[lumo.core]
         '[child_process :refer [exec]])

(def urls cljs.core/*command-line-args*)

(defn wget [url]
  (exec (str "wget -nv " url) println))

(dorun (map wget urls))
