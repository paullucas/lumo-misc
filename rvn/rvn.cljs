(require '[cljs.nodejs :as nodejs])
(def process (nodejs/require "process"))
(def child-process (nodejs/require "child_process"))
(def dgram (nodejs/require "dgram"))
(def oscmsg (nodejs/require "osc-msg"))
(def socket (.createSocket dgram "udp4"))
(def current-node (atom 2))

(defn send-msg [msg]
  (let [buf (.encode oscmsg (clj->js msg))]
    (.send socket buf 0 (.-length buf) "57110" "127.0.0.1")))

(defn nest []
  (send-msg {:address "/g_new"
             :args [{:type "integer" :value 1}
                    {:type "integer" :value 1}
                    {:type "integer" :value 0}]})
  "Building a nest on a distant power line...")

(defn lay [synthdef]
  (swap! current-node inc)
  (send-msg {:address "/s_new"
             :args [{:type "string" :value synthdef}
                    {:type "integer" :value @current-node}
                    {:type "integer" :value 1}
                    {:type "integer" :value 1}]})
  "An egg is hatching...")

(defn croak [node k v]
  (send-msg {:address "/n_set"
             :args [{:type "integer" :value node}
                    {:type "string" :value k}
                    {:type "integer" :value v}]})
  "Croaking...")

(defn fly []
  (send-msg {:address "/n_free"
             :args [{:type "integer" :value 1}]})
  "Abandoning nest...")

(defn compile []
  (child-process.exec "tr -d '\n' < synthDefs.scd | sclang"))

(defn load-defs []
  (send-msg {:address "/d_loadDir"
             :args [{:type "string"
                     :value (str (.cwd process) "/synths/")}]}))

(compile)
(load-defs)
