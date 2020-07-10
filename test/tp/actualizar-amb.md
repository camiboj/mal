```clojure
(def ambiente (vector "clave1" "valor1" "clave2" "valor2" "clave3" "valor3"))


(def clave "clave1")
(def valor "NuevoValor1")
; -> ["clave1" "NuevoValor1" "clave2" "valor2" "clave3" "valor3"]
(println  (actualizar-amb ambiente clave valor))

(def clave "clave4")
(def valor "valor4")
; -> ["clave1" "valor1" "clave2" "valor2" "clave3" "valor3" "clave4" "valor4"]
(println  (actualizar-amb ambiente clave valor))
```