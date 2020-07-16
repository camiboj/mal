# imprrimir (Aridad 0)
```clojure
; Imprime, con salto de linea, atomos o listas en formato estandar (las cadenas con comillas) y devuelve su valor. Muestra errores sin parentesis.
; Aridad 1: Si recibe un escalar, lo imprime con salto de linea en formato estandar (pero si es space no lo imprime). purga la salida y devuelve el escalar.
; Si recibe una secuencia cuyo primer elemento es "error", se llama recursivamente con dos argumentos iguales: la secuencia recibida.
; Si no, imprime lo recibido con salto de linea en formato estandar, purga la salida y devuelve la cadena.
(defn imprimir
  ([elem]...)
)
```

## imprimit string
```clojure
(def elem "hola")
; "hola"
; hola
(println (imprimir elem))
```

## imprimit vector strings
```clojure
(def elem '("a" "b"))
; ("a" "b")
; (a b)
(println (imprimir elem))
```

## imprimit quote
```clojure
(def elem 'a)
; a
; a
(println (imprimir elem))
```

## imprimit vector quotes
```clojure
(def elem (list 'a 'b))
; (a b)
; (a b)
(println (imprimir elem))
```
