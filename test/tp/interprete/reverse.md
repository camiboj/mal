# reverse
```clojure
; Actualiza un ambiente (una lista con claves en las posiciones pares [0, 2, 4..]
; y valores en las pares [1, 2, 3..] Recibe el ambiente, la clave y el valor.
; Si el valor no es escalar y en su primera posicion contiene '*error*,: retorna el ambiente intacto
; Si no, coloca la clave y el valor en el ambiente (puede ser un alta o una actualizacion) y lo retorna.
(defn reverse [lis] ...)
```

## casos
```clojure
; nil
(reverse nil)
; nil
(reverse '())
; (3 2 1)
(reverse (1 2 3))


; list expectect
(reverse 1)

; recibir solo un parametro
```