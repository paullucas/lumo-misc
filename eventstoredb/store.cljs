(require '[cljs.nodejs :as nodejs])
(def client (nodejs/require "eventstore-node"))
(def uuid (nodejs/require "uuid"))
(def stream-name "testStream")
(def connection (.createConnection client (clj->js {}) "tcp://localhost:1113"))

(.connect connection)
(.once connection
       "connected"
       (fn [tcp-end-point]
         (js/console.log
          (str "Connected to eventstore at " (.-host tcp-end-point) ":" (.-port tcp-end-point)))))

(def event-id (.v4 uuid))
(def event-data (clj->js
                 {:a (.random js/Math)
                  :b (.v4 uuid)}))


(def event (.createJsonEventData client
                                 event-id
                                 event-data
                                 nil
                                 "testEvent"))
(js/console.log "Appending...")

(.then (.appendToStream connection
                        stream-name
                        (.-any (.-expectedVersion client))
                        event)
       (fn [result]
         (js/console.log (str "Stored event:" event-id))
         (js/console.log "Look for it at: http://localhost:2113/web/index.html#/streams/testStream")
         (.close connection)))
