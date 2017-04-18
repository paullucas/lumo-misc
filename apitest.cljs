#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def http (nodejs/require "http"))

(defn promise [executor]
  (new js/Promise executor))

(defn sleep [ms]
  (promise #(js/setTimeout % ms)))

(defn post-req [url data]
  (promise
   (fn [resolve]
     (let [json (.stringify js/JSON (clj->js data))
           options (clj->js {:hostname "127.0.0.1"
                             :port 3000
                             :path url
                             :method "POST"
                             :headers {:Content-Type "application/json"
                                       :Content-Length (.byteLength js/Buffer json)}})
           req (.request http
                         options
                         #(.on % "data" resolve))]
       (.write req json)
       (.end req)))))

(defn get-req [url]
  (promise
   (fn [resolve]
     (.get http
           (str "http://localhost:3000" url)
           #(.on % "data" resolve)))))

(defn run-test [test-name post-url post-data test-url test-fn]
  (-> (post-req post-url post-data)
      (.then #(sleep 1000))
      (.then #(get-req test-url))
      (.then (fn [get-res]
               (let [test-result (test-fn (js->clj (.parse js/JSON get-res) :keywordize-keys true))]
                 (println (str test-name (if test-result
                                           " test passed"
                                           " test failed"))))))))
