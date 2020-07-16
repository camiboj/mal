# revisar-lae

## casos
```clojure
; nil
(println (revisar-lae '("a" "b" "c" *error*)))

(def le '('*error* "algo"))
; ('*error* "algo")
(println (revisar-lae '("a" "b" le "c" *error*)))
```
