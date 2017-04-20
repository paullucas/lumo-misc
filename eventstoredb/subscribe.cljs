(require '[cljs.nodejs :as nodejs])
(def es-client (nodejs/require "eventstore-node"))
(def user-credentials (nodejs/require "eventstore-node"))
(def uuid (nodejs/require "uuid"))

(def credentials (es-client.UserCredentials. "admin" "changeit"))
(def es-connection (.createConnection es-client
                                      (clj->js {})
                                      (clj->js {:host "localhost" :port 1113})))

(defn subscription-dropped [subscription reason error]
  (if error
    (js/console.log error)
    (js/console.log "Subscription dropped.")))

(defn live-processing-started []
  (js/console.log "Caught up with previously stored events. Listening for new events."))

(defn event-appeared [stream event]
  (let [original-event (.-originalEvent event)]
    (js/console.log (.-eventStreamId original-event))
    (js/console.log (.-eventId original-event))
    (js/console.log (.-eventType original-event))))

(.connect es-connection)
(.once es-connection
       "connected"
       (fn [tcp-end-point]
         (js/console.log (str "Connected to eventstore at "
                              (.-host tcp-end-point)
                              ":"
                              (.-port tcp-end-point)))
         (.subscribeToAllFrom es-connection
                              nil
                              true
                              event-appeared
                              live-processing-started
                              subscription-dropped
                              credentials)))

(.on es-connection "error"
     (fn [err]
       (js/console.log (str "Error occurred on connection: " err))))

(.on es-connection "closed"
     (fn [reason]
       (js/console.log (str "Connection closed, reason: " reason))))
