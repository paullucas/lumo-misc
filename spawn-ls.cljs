#!/usr/bin/env lumo
(require '[lumo.core]
         '[child_process :refer [spawn]])

(def ls (spawn "ls" ["-lh" "/usr"]))

(.. ls -stdout
    (on "data" println))
