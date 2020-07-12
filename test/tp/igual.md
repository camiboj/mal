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
(println (igual? (list) nil))
(println (igual? nil (list)))
(println (igual? (list) (list)))
(println (igual? nil nil))
(println (igual? "A" "a"))
(println (igual? "a" "A"))
(println (igual? "a" "a"))
(println (igual? "B" "B"))
```
## no iguales
```clojure
(println (igual? (list) "A"))
(println (igual? nil "A"))
(println (igual? "A" (list)))
(println (igual? "A" nil))
(println (igual? "A" "aa"))
(println (igual? (list 1) nil))
(println (igual? nil (list 1)))
(println (igual? "a" (list "a")))
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