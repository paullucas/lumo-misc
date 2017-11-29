(ns args.core
  (:require lumo.core))

(defn ^:export -main [& args]
  (println cljs.core/*command-line-args*))

(set! *main-cli-fn* -main)
