(ns eventstoredb.sub
  (:require [lumo.core]
            [node-eventstore-client :as es]))

(defn sub []
  (def credentials (es/UserCredentials. "admin" "changeit"))
  (def connection (es/createConnection #js {} #js {:host "localhost" :port 1113}))

  (defn subscription-dropped [subscription reason error]
    (if error
      (println error)
      (println "Subscription dropped.")))

  (defn live-processing-started []
    (println "Caught up with previously stored events. Listening for new events."))

  (defn event-appeared [stream event]
    (let [original-event (.-originalEvent event)]
      (println (.-eventStreamId original-event))
      (println (.-eventId original-event))
      (println (.-eventType original-event))))

  (.connect connection)
  (.once connection "connected"
         (fn [tcp-endpoint]
           (println
            (str "Connected to eventstore at " (.-host tcp-endpoint) ":" (.-port tcp-endpoint)))
           (.subscribeToAllFrom connection
                                nil
                                true
                                event-appeared
                                live-processing-started
                                subscription-dropped
                                credentials)))

  (.on connection "error"
       #(println (str "Error occurred on connection: " %)))

  (.on connection "closed"
       #(println (str "Connection closed, reason: " %))))
