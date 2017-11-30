#!/usr/bin/env lumo
(require 'lumo.core 'https)

(def url
  (let [[ip] cljs.core/*command-line-args*]
    (if ip
      (str "https://ipinfo.io/" ip "/json")
      "https://ipinfo.io/json")))

(defn create-template [json]
  (let [{:keys [ip
                hostname
                city
                region
                country
                loc
                org
                postal]}
        (-> (.parse js/JSON json)
            (js->clj :keywordize-keys true))]
    [{:name "IP Address"   :value ip}
     {:name "Hostname"     :value hostname}
     {:name "City"         :value city}
     {:name "Region"       :value region}
     {:name "Country"      :value country}
     {:name "Location"     :value loc}
     {:name "Organization" :value org}
     {:name "Postal"       :value postal}]))

(defn print-datum [{n :name v :value}]
  (println (str n ": " v)))

(defn print-data [json]
  (dorun (map print-datum (create-template json))))

(defn get-res [url]
  (https/get url #(.on % "data" print-data)))

(get-res url)
