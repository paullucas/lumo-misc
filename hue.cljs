#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def http (nodejs/require "http"))
(def util (nodejs/require "util"))
(def username (nodejs/require "./hueuser.js"))

(defn print-data [json]
  (let [data (.parse js/JSON json)]
    (js/console.dir data #js {:depth nil :colors true})))

(defn url-str [endpoint]
  (str "http://192.168.1.70/api/" username "/" endpoint))

(defn get-req [url]
  (.get
   http
   url
   (fn [res]
     (let [raw-data (atom "")]
       (.on res "data"
            (fn [data]
              (reset! raw-data (str @raw-data data))))
       (.on res "end"
            (fn []
              (print-data @raw-data)))))))

(defn post-req [path data]
  (let [req (.request
             http
             #js {:hostname "192.168.1.70"
                  :path path
                  :method "PUT"
                  :headers
                  #js {:Content-Type "application/json"
                       :Content-Length (.byteLength js/Buffer data)}}
             (fn [res] (.on res "data" #(js/console.log (.parse js/JSON %)))))]
   (.write req data)
   (.end req)))

(defn rainbow []
  (post-req
   "/lights/2/state"
   (.stringify
    js/JSON
    #js {:on true
         :sat 54
         :bri 54
         :hue 1000}))
  (post-req
   "/lights/1/state"
   (.stringify
    js/JSON
    #js {:on true
         :sat 254
         :bri 254
         :hue 1000})))

(defn parse-args []
  (let [args (subvec (js->clj js/process.argv) 3)]
    (case args
      ["info"] (get-req (url-str "lights/"))
      ["light" "1"] (get-req (url-str "lights/1/"))
      ["fun"] (rainbow)
      "")))

(parse-args)
