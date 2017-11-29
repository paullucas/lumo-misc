(ns hue.core
  (:require lumo.core http util))

(def ip "192.168.1.64")
(def username "")

(defn print-data [json]
  (-> json
      js/JSON.parse
      (js/console.dir #js {:depth nil :colors true})))

(defn url-str [endpoint]
  (str "http://" ip "/api/" username "/" endpoint))

(defn get-req [url]
  (.get http url (fn [res]
                   (let [raw-data (atom "")]
                     (.on res "data" #(reset! raw-data (str @raw-data %)))
                     (.on res "end"  #(print-data @raw-data))))))

(defn do-req [path data & [method]]
  (let [headers #js {:Content-Type "application/json"
                     :Content-Length (.byteLength js/Buffer data)}
        options #js {:hostname ip
                     :path (str "api/" username path)
                     :method (or method "PUT")
                     :headers headers}
        callback-fn (fn [res]
                      (.on res "data"
                           #(js/console.log (try
                                              (.parse js/JSON %)
                                              (catch :default e e)))))
        req (.request http options callback-fn)]
    (.write req data)
    (.end req)))

(defn rainbow []
  (do-req (url-str "lights/2/state")
          (-> #js {:on true :sat 54 :bri 54 :hue 1000}
              js/JSON.stringify))
  (do-req (url-str "lights/1/state")
          (-> #js {:on true :sat 254 :bri 254 :hue 1000}
              js/JSON.stringify)))

(defn rand-state []
  (do-req (url-str "/lights/1/state")
          (-> #js { ;; :on true
                   :sat (rand 254)
                   :bri (rand 254)
                   :hue (rand 1000)}
              js/JSON.stringify)))

(defn create-user []
  (do-req "/api"
          "{\"devicetype\":\"my_hue_app#test test\"}"
          "POST"))

(defn ^:export -main [& args]
  (let [[cmd user] cljs.core/*command-line-args*]
    (case cmd
      "info"  (get-req (url-str "lights/"))
      "light" (get-req (url-str "lights/1/"))
      "fun"   (rainbow)
      "rand"  (rand-state)
      "user"  (create-user)
      "")))

(set! *main-cli-fn* -main)
