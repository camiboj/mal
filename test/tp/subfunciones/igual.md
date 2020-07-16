# igual?
```clojure
; Compara la igualdad de dos simbolos.
; Recibe dos simbolos a y b. Retorna true si se deben considerar iguales; si no, false.
; Se utiliza porque TLC-LISP no es case-sensitive y ademas no distingue entre nil y la lista vacia.
 (defn igual? [a b]
   (or (= a b)  (igual_string? a b) (igual_lista_nil? a b))
   )
```
## iguales
```clojure
(println (igual? '() nil))
(println (igual? nil '()))
(println (igual? '() '()))
(println (igual? nil nil))
(println (igual? "A" "a"))
(println (igual? "a" "A"))
(println (igual? "a" "a"))
(println (igual? "B" "B"))
```
## no iguales
```clojure
(println (igual? '() "A"))
(println (igual? nil "A"))
(println (igual? "A" '()))
(println (igual? "A" nil))
(println (igual? "A" "aa"))
(println (igual? '(1) nil))
(println (igual? nil '(1)))
(println (igual? "a" '("a")))
```

## comparar literales (IMPORTANTE)
```clojure
; true
(println (igual? 'add 'add))
(println (igual? 'add 'ADD))
; false
(println (igual? 'add '*error))
(println (igual? '*error 'add))
```