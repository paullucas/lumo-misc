#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def util (nodejs/require "util"))
(def stream (nodejs/require "stream"))

(def buf js/Buffer)
(defn opc-stream [] (.PassThrough stream))
(.inherits util opc-stream (.-PassThrough stream))

(defn create-control
  [channel command length]
  (doto (new buf 4)
    (.writeUInt8 channel 0)
    (.writeUInt8 command 1)
    (.writeUInt16BE length 2)))

(defn create-message
  [channel command data]
  (.concat
   buf
   [(create-control channel command (.length data))
    data]))

(defn write-msg
  [stream channel command data]
  (.write
   stream
   (create-message channel command data)))

(defn create-pixels-message [channel pixels]
  (create-message channel 0 pixels))

(defn write-pixels
  [stream channel pixels]
  (.write
   stream
   (create-pixels-message channel pixels)))

(defn create-set-global-color-correction-message
  [config]
  (create-message
   0 0xff
   (doto (new (buf (+ 4 (.byteLength buf json))))
     (.writeUInt16BE 0x0001 0)
     (.writeUInt16BE 0x0001 2)
     (.write (.stringify JSON config) 4))))

(defn write-color-correction
  [stream config]
  (.write
   stream
   (create-set-global-color-correction-message config)))

;; exports
