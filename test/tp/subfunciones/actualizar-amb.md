# actualizar-amb
```clojure
; Actualiza un ambiente (una lista con claves en las posiciones pares [0, 2, 4..]
; y valores en las pares [1, 2, 3..] Recibe el ambiente, la clave y el valor.
; Si el valor no es escalar y en su primera posicion contiene '*error*,: retorna el ambiente intacto
; Si no, coloca la clave y el valor en el ambiente (puede ser un alta o una actualizacion) y lo retorna.
(defn actualizar-amb [amb-global clave valor] ...)
```

## Agrego una clave que no existía (add)
```clojure
(def ambiente (vector "clave1" "valor1" "clave2" "valor2" "clave3" "valor3"))
(def clave "clave1")
(def valor "NuevoValor1")
; -> ["clave1" "NuevoValor1" "clave2" "valor2" "clave3" "valor3"]
(println  (actualizar-amb ambiente clave valor))
```

## Agrego una clave existente (update)
```clojure
(def ambiente (vector "clave1" "valor1" "clave2" "valor2" "clave3" "valor3"))
(def clave "clave4")
(def valor "valor4")
; -> ["clave1" "valor1" "clave2" "valor2" "clave3" "valor3" "clave4" "valor4"]
(println  (actualizar-amb ambiente clave valor))
```

## error
```clojure
(def ambiente (vector '*error* 'descripcion))
(def clave "clave4")
(def valor "valor4")
; -> ['*error* 'descripcion]
(println  (actualizar-amb ambiente clave valor))
```