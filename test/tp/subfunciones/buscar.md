#buscar
```clojure
; Busca una clave en un ambiente (una lista con claves en las posiciones pares [0, 2, 4...]
; y valores en las pares [1, 3, 5...] y retorna el val
; Si no la encuentra, retorna una lista con '*error* en la 1ra. pos., 'unbound
(defn buscar [clave ambiente] ...)
```

## casos
```clojure
(def ambiente (vector "clave1" "valor1" "clave2" "valor2" "clave3" "valor3"))
(def clave "clave4")
; (*error* unbound-symbol clave4)
(println (buscar clave ambiente))


(def ambiente (vector "clave1" "primer_valor1" "calve1" "segundo_valor1"))
(def clave "clave1")
; primer_valor1
(println (buscar clave ambiente))

(def ambiente (list "clave1" "primer_valor1" "calve1" "segundo_valor1"))
(def clave "clave1")
; primer_valor1
(println (buscar clave ambiente))
```