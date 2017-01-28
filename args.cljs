#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def args (subvec (js->clj js/process.argv) 3))
(println args)
