#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(println (subvec (js->clj js/process.argv) 3))
