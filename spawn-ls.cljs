#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(nodejs/enable-util-print!)

(def child-process (nodejs/require "child_process"))
(def spawn (.-spawn child-process))
(def ls (spawn "ls" ["-lh" "/usr"]))

(.. ls -stdout
    (on "data"
        (fn [data]
          (println "stdout: " data))))
