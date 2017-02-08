#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def https (nodejs/require "https"))

(let [args (subvec (js->clj js/process.argv) 3)
      url (if (> (count args) 0)
            (str "https://ipinfo.io/" (first args) "/json")
            "https://ipinfo.io/json")]
  (.get https url
        (fn [res]
          (.on res "data"
               (fn [json]
                 (let [data (js->clj (.parse js/JSON json) :keywordize-keys true)]
                   (dorun (map
                           (fn [d]
                             (println (str (:name d) ": " (:value d))))
                           [{:name "IP Address" :value (:ip data)}
                            {:name "Hostname" :value (:hostname data)}
                            {:name "City" :value (:city data)}
                            {:name "Region" :value (:region data)}
                            {:name "Country" :value (:country data)}
                            {:name "Location" :value (:loc data)}
                            {:name "Organization" :value (:org data)}
                            {:name "Postal" :value (:postal data)}]))))))))
