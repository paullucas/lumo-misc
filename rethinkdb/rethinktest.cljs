(require '[cljs.nodejs :as nodejs])
(def r (nodejs/require "rethinkdb"))
(def db-name "eventstate")

(defn get-events []
  (-> (.connect r (clj->js {:db db-name}))
      (.then (fn [connection]
               (-> r
                   (.table "events")
                   (.run connection))))
      (.then (fn [cursor]
               (.toArray cursor)))
      (.then (fn [result]
               (let [data (js->clj result :keywordize-keys true)]
                 (println data))))))

(get-events)
