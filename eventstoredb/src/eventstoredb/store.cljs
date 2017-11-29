(ns eventstoredb.store
  (:require [lumo.core]
            [uuid :as u]
            [node-eventstore-client :as es]))

(defn store []
  (def stream-name "testStream")
  (def connection (es/createConnection #js {} "tcp://localhost:1113"))

  (.connect connection)
  (.once connection "connected"
         (fn [tcp-endpoint]
           (println
            (str "Connected to eventstore at " (.-host tcp-endpoint) ":" (.-port tcp-endpoint)))))

  (def event-id (u/v4))
  (def event-data (clj->js {:a (.random js/Math)
                            :b (u/v4)}))


  (def event (es/createJsonEventData event-id
                                     event-data
                                     nil
                                     "testEvent"))

  (println "Appending...")

  (.then (.appendToStream connection stream-name (.-any es/expectedVersion) event)
         (fn [result]
           (println (str "Stored event:" event-id))
           (println "Look for it at: http://localhost:2113/web/index.html#/streams/testStream")
           (.close connection))))
