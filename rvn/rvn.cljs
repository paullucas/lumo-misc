(require '[cljs.nodejs :as nodejs])
(def dgram (nodejs/require "dgram"))
(def oscmsg (nodejs/require "osc-msg"))
(def socket (.createSocket dgram "udp4"))

(defn g
  "Create group on node 1"
  []
  (let [buf (.encode oscmsg (clj->js
                             {:address "/g_new"
                              :args [{:type "integer" :value 1}
                                     {:type "integer" :value 1}
                                     {:type "integer" :value 0}]}))]
    (.send socket buf 0 (.-length buf) "57110" "127.0.0.1")))

(defn f
  "Free node 1"
  []
  (let [buf (.encode oscmsg (clj->js
                             {:address "/n_free"
                              :args [{:type "integer" :value 1}]}))]
    (.send socket buf 0 (.-length buf) "57110" "127.0.0.1")))

(defn f-
  "Free node x"
  [node]
  (let [buf (.encode oscmsg (clj->js
                             {:address "/n_free"
                              :args [{:type "integer" :value node}]}))]
    (.send socket buf 0 (.-length buf) "57110" "127.0.0.1")))




