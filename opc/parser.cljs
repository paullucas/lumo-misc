#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def through (nodejs/require "through2"))
(def duplexer (nodejs/require "duplexer2"))

(defn parse-msg
  [read callback]
  (def msg (clj->js {}))
  (read 1
   (fn [res1]
     (set! (.-channel msg) (.readUInt8 res1 0))
     (read 1
      (fn [res2]
        (set! (.-command msg) (.readUInt8 res2 0))
        (read 2
         (fn [res3]
           (read (.readUInt16BE res3 0)
            (fn [res4]
              (set! (.- data msg res4))
              (callback msg))))))))))


(defn parse-all-msgs
  [read callback]
  (defn next []
    (parse-msg
     read
     (fn [msg]
       (callback msg)
       (next))))
  (next))

