(de sumar (a b) (+ a b))
(de restar (a b) (- a b))
(setq x 1)
(setq y 2)
(sumar 3 6)
(restar 4 5)
y
(de C (LF X)
 (if (null LF)
 nil
 (cons ((first LF) X) (C (rest LF) X))
 )
)
(C (list first rest list) '((1 2 3)(4 5 6)(7 8 9)))
(de cargarR ()
 (prin3 "R: ")(setq R (read))(prin3 "R * 2: ")(prin3 (+ R R))(terpri))

(de recorrer (L)
 (recorrer2 L 0))

(DE recorrer2 (L i)
(COND
((NULL (rest L)) (list (first L) i))
(T (prin3 (list (first L) i))(setq D (+ i 1)) (terpri)(recorrer2 (REST L) D))))
(de compa (a b)
 (if (equal a b) (setq m 5) (exit)))