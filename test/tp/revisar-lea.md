# revisar-lae

## casos
```clojure
; nil
(println (revisar-lae (list "a" "b" "c" '*error*)))

(def le (list '*error* "algo"))
; ('*error* "algo")
(println (revisar-lae (list "a" "b" le "c" '*error*)))
```

## verificar esto
```clojure
(def le (list '*error* "algo"))
; nil
(println (revisar-lae (list "a" "b" (list "sub-sub-lista" le) "c" '*error*)))
```