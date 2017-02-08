#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def https (nodejs/require "https"))

(defn get-url []
  (let [args (subvec (js->clj js/process.argv) 3)]
    (if (> (count args) 0)
      (str "https://ipinfo.io/" (first args) "/json")
      "https://ipinfo.io/json")))

(defn create-template [json]
  (let [data (js->clj (.parse js/JSON json) :keywordize-keys true)]
    [{:name "IP Address" :value (:ip data)}
     {:name "Hostname" :value (:hostname data)}
     {:name "City" :value (:city data)}
     {:name "Region" :value (:region data)}
     {:name "Country" :value (:country data)}
     {:name "Location" :value (:loc data)}
     {:name "Organization" :value (:org data)}
     {:name "Postal" :value (:postal data)}]))

(defn print-datum [data]
  (println (str (:name data) ": " (:value data))))

(defn print-data [json]
  (dorun
   (map print-datum (create-template json))))

(defn get-res [url]
  (.get https url #(.on % "data" print-data)))

(get-res (get-url))
