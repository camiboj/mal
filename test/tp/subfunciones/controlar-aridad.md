# controlar-aridad
```clojure
; Controla la aridad (cantidad de argumentos de una funcion).
; Recibe una lista y un numero. Si la longitud de la lista coincide con el numero, retorna el numero.
; Si es menor, retorna (list '*error* 'too-few-args).
; Si es mayor, retorna (list '*error* 'too-many-args).
(defn controlar-aridad [lis val-esperado] ...)
```

## aridad correcta (retorna aridad)
```clojure
(def lis '('a 'b))
(def val-esperado 2)
; -> 2
(println (controlar-aridad lis val-esperado))
```

## menos parametros (retorna error)
```clojure
(def lis '('a 'b))
(def val-esperado 3)
; -> ('*error* 'too-few-args)
(println (controlar-aridad lis val-esperado))
```

## mas parametros (retorna error)
```clojure
(def lis '('a 'b))
(def val-esperado 1)
; -> ('*error* 'too-many-args)
(println (controlar-aridad lis val-esperado))```