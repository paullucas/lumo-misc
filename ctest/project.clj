(defproject ctest "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojurescript "1.9.293"]
                 [org.clojure/core.async "0.2.395"]]
  :npm {:dependencies [[source-map-support "0.4.0"]]}
  :source-paths ["src" "target/classes"]
  :clean-targets ["out" "release"]
  :target-path "target")


