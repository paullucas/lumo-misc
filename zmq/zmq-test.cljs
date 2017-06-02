(require '[cljs.nodejs :as nodejs])

(def zmq      (nodejs/require "zeromq"))
(def zmq-push (atom nil))
(def zmq-pull (atom nil))

(defn init-zmq-pull [address]
  (reset! zmq-pull (.socket zmq "pull"))
  (.connect @zmq-pull address)
  (println (str "Subscribed to " address))
  (.on @zmq-pull "message" println))

(defn init-zmq-push [address]
  (reset! zmq-push (.socket zmq "push"))
  (.bindSync @zmq-push address)
  (println (str "Started publisher on port " address)))

(defn leave []
  (reset! @zmq-push (.close @zmq-push))
  (reset! @zmq-pull (.close @zmq-pull)))

(init-zmq-push "tcp://127.0.0.1:3000")
(init-zmq-pull "tcp://127.0.0.1:3000")

(.send @zmq-push ["hello"])
