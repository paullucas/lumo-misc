#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def http (nodejs/require "http"))

(defn sleep [ms]
  (new js/Promise
       (fn [resolve]
         (js/setTimeout resolve ms))))

(defn post-req [url data]
  (new js/Promise
       (fn [resolve]
         (let [req (.request http
                             (clj->js
                              {:hostname "127.0.0.1"
                               :port 3000
                               :path url
                               :method "POST"
                               :headers {:Content-Type "application/json"
                                         :Content-Length (.byteLength js/Buffer data)}})
                             (fn [res]
                               (.on res "data" resolve)))]
           (.write req data)
           (.end req)))))

(defn get-req [url]
  (new js/Promise
       (fn [resolve]
         (.get http
               (str "http://localhost:3000" url)
               (fn [res]
                 (.on res "data" resolve))))))

(defn run-test [test-name post-url post-data test-url test-fn]
  (-> (post-req post-url
                (.stringify js/JSON (clj->js post-data)))
      (.then (fn [] (sleep 1000)))
      (.then (fn [] (get-req test-url)))
      (.then (fn [get-res]
               (println
                (str test-name
                     (if (test-fn (js->clj (.parse js/JSON get-res) :keywordize-keys true))
                       " test passed"
                       " test failed")))))))
