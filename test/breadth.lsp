(de breadth-first (bc)
    (prin3 "Ingrese el estado inicial: ") (setq inicial (read))
    (prin3 "Ingrese el estado final: ") (setq final (read))
    (cond ((equal inicial final) (prin3 "El problema ya esta resuelto !!!") (terpri) (breadth-first bc))
    (t (buscar bc final (list (list inicial)) nil))))

(de buscar (bc fin grafobusq estexp)
    (prin3 "Buscando")
    (cond ((null grafobusq) (fracaso))
        ((pertenece fin (first grafobusq)) (exito grafobusq))
        (t (buscar bc fin (append (rest grafobusq) (expandir (first grafobusq) bc estexp))
        (if (pertenece (first (first grafobusq)) estexp)
            estexp
            (cons (first (first grafobusq)) estexp))))))

(de expandir (linea basecon estexp)
    (if (or (null basecon) (pertenece (first linea) estexp))
        nil
        (if (not (equal ((eval (first basecon)) (first linea)) (first linea)))
            (cons (cons ((eval (first basecon)) (first linea)) linea) (expandir linea (rest basecon) estexp))
            (expandir linea (rest basecon) estexp))))

(de pertenece (x lista)
    (cond ((null lista) nil)
        ((equal x (first lista)) t)
        (t (pertenece x (rest lista)))))

(de exito (grafobusq)
    (prin3 "Exito !!!") (terpri)
    (prin3 "Prof ....... ") (prin3 (- (length (first grafobusq)) 1)) (terpri)
    (prin3 "Solucion ... ") (prin3 (reverse (first grafobusq))) (terpri) t)

(de fracaso ()
    (prin3 "No existe solucion") (terpri) t)