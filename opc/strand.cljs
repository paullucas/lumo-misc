#!/usr/bin/env lumo
(require '[cljs.nodejs :as nodejs])
(def buf js/Buffer)

(defn get-buf
  [input]
  (if (.isBuffer buf input)
    (if (not= 0 (mod (.-length input) 3))
      (throw
       (new js/Error
            "Buffer length must be a multiple of 3"))
      input)
    (if (.isInteger js/Number input)
      (new buf (* input 3))
      (throw
       (new js/Error
            "Input must be a buffer or an integer length")))))

(defn set-pixel
  [strand index r g b]
  (def offset (* index 3))
  ;; (.writeUInt8 (.buffer strand) r offset)
  (.. strand buffer writeUInt8 r offset)
  (.. strand buffer writeUInt8 g (+ offset 1))
  (.. strand buffer writeUInt8 b (+ offset 2)))

(defn get-pixel
  [strand index]
  (def offset (* index 3))
  [(.. strand buffer writeUInt8 offset)
   (.. strand buffer writeUInt8 (+ offset 1))
   (.. strand buffer writeUInt8 (+ offset 2))])

;; exports
