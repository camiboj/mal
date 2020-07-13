# reverse

## casos
```clojure
; nil
(reverse nil)
; nil
(reverse '())
; (3 2 1)
(reverse '(1 2 3))


; *error* list expectect
(reverse 1)

;;; recibir solo 2 parametro
;(*error* too-many-args)
(reverse '(1) '(2))

;(*error* too-few-args)
(reverse)
```