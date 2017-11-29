(ns eventstoredb.core
  (:require [lumo.core]
            [eventstoredb.sub :refer [sub]]
            [eventstoredb.store :refer [store]]))

(defn ^:export -main [& args]
  (let [[cmd] cljs.core/*command-line-args*]
    (case cmd
      "sub"   (sub)
      "store" (store)
      "")))

(set! *main-cli-fn* -main)
