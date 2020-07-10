# revisar-f
```clojure
; Revisa una lista que representa una funcion.
; Recibe la lista y, si esta comienza con '*error*, la retorna. Si no, retorna nil.
(defn revisar-f [lis] ...)
```

## casos
```clojure
; '*error* "a"
(println (revisar-f (list '*error* "a")))

; nil
(println (revisar-f (list 'otro "a")))
```