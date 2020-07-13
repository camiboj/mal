# append

## casos
```clojure
; nil
(append '(1) nil)
; nil
(append nil '(1))
; nil
(append nil nil)
; (1 2)
(append '(1) '(2))


; *error* list expectect
(append 1 '(2))

;;; cant(parametro) != 2
;(*error* too-many-args)
(append '(1) '(2) '(3))
;(*error* too-few-args)
(append '(1))

;;; segundo no es lista
;(list '*error* 'not-implemented)
(append '(1) 2)
(append '(1) "a")

```