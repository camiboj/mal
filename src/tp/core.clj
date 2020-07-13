(declare evaluar)
(declare aplicar)
(declare controlar-aridad)
(declare igual?)
(declare cargar-arch)
(declare imprimir)
(declare actualizar-amb)
(declare revisar-f)
(declare revisar-lae)
(declare buscar)
(declare evaluar-cond)
(declare evaluar-secuencia-en-cond)

; REPL (read–eval–print loop).
; Aridad 0: Muestra mensaje de bienvenida y se llama recursivamente con el ambiente inicial.
; Aridad 1: Muestra >>> y lee una expresion y la evalua.
; Si la 2da. posicion del resultado es nil, retorna true (caso base de la recursividad).
; Si no, imprime la 1ra. pos. del resultado y se llama recursivamente con la 2da. pos. del resultado.
(defn repl
  ([]
   (println "Interprete de TLC-LISP en Clojure")
   (println "Trabajo Practico de 75.14/95.48 - Lenguajes Formales 2020")
   (println "Inspirado en:")
   (println "TLC-LISP Version 1.51 for the IBM Personal Computer")
   (println "Copyright (c) 1982, 1983, 1984, 1985 The Lisp Company") (flush)
   (repl '(add add append append cond cond cons cons de de env env equal equal eval eval exit exit
               first first ge ge gt gt if if lambda lambda length length list list load load lt lt nil nil not not
               null null or or prin3 prin3 quote quote read read rest rest reverse reverse setq setq sub sub
               t t terpri terpri + add - sub)))
  ([amb]
   (print ">>> ") (flush)
   (try (let [res (evaluar (read) amb nil)]
          (if (nil? (fnext res))
            true
            (do (imprimir (first res)) (repl (fnext res)))))
        (catch Exception e (println) (print "*error* ") (println (get (Throwable->map e) :cause)) (repl amb))))
  )

; Carga el contenido de un archivo.
; Aridad 3: Recibe los ambientes global y local y el nombre de un archivo
; (literal como string o atomo, con o sin extension .lsp, o el simbolo ligado al nombre de un archivo en el ambiente), abre el archivo
; y lee un elemento de la entrada (si falla, imprime nil), lo evalua y llama recursivamente con el (nuevo?) amb., nil, la entrada y un arg. mas: el resultado de la evaluacion.
; Aridad 4: lee un elem. del archivo (si falla, imprime el ultimo resultado), lo evalua y llama recursivamente con el (nuevo?) amb., nil, la entrada y el resultado de la eval.
(defn cargar-arch
  ([amb-global amb-local arch]
   (let [nomb (first (evaluar arch amb-global amb-local))]
     (if (and (seq? nomb) (igual? (first nomb) '*error*))
       (do (imprimir nomb) amb-global)
       (let [nm (clojure.string/lower-case (str nomb)),
             nom (if (and (> (count nm) 4) (clojure.string/ends-with? nm ".lsp")) nm (str nm ".lsp")),
             ret (try (with-open [in (java.io.PushbackReader. (clojure.java.io/reader nom))]
                        (binding [*read-eval* false] (try (let [res (evaluar (read in) amb-global nil)]
                                                            (cargar-arch (fnext res) nil in res))
                                                          (catch Exception e (imprimir nil) amb-global))))
                      (catch java.io.FileNotFoundException e (imprimir (list '*error* 'file-open-error 'file-not-found nom '1 'READ)) amb-global))]
         ret))))
  ([amb-global amb-local in res]
   (try (let [res (evaluar (read in) amb-global nil)] (cargar-arch (fnext res) nil in res))
        (catch Exception e (imprimir (first res)) amb-global)))
  )

; Evalua una expresion usando los ambientes global y local. Siempre retorna una lista con un resultado y un ambiente.
; Si la evaluacion falla, el resultado es una lista con '*error* como primer elemento, por ejemplo: (list '*error* 'too-many-args) y el ambiente es el ambiente global.
; Si la expresion es un escalar numero o cadena, retorna la expresion y el ambiente global.
; Si la expresion es otro tipo de escalar, la busca (en los ambientes local y global) y retorna el valor y el ambiente global.
; Si la expresion es una secuencia nula, retorna nil y el ambiente global.
; Si el primer elemento de la expresion es '*error*, retorna la expresion y el ambiente global.
; Si el primer elemento de la expresion es una forma especial o una macro, valida los demas elementos y retorna el resultado y el (nuevo?) ambiente.
; Si no lo es, se trata de una funcion en posicion de operador (es una aplicacion de calculo lambda), por lo que se llama a la funcion aplicar,
; pasandole 4 argumentos: la evaluacion del primer elemento, una lista con las evaluaciones de los demas, el ambiente global y el ambiente local.
(defn evaluar [expre amb-global amb-local]
  (print "- EVALUANDO PIPI -")
  (print "   - expre: ")
  (println expre)
  (print "   - amb-global: ")
  (println amb-global)
  (print "   - amb-local: ")
  (println amb-local)
  (if (not (seq? expre))
    (if (or (number? expre) (string? expre)) (list expre amb-global) (list (buscar expre (concat amb-local amb-global)) amb-global))
    (cond (igual? expre nil) (list nil amb-global)
          (igual? (first expre) '*error*) (list expre amb-global)
          (igual? (first expre) 'exit) (if (< (count (next expre)) 1) (list nil nil) (list (list '*error* 'too-many-args) amb-global))
          (igual? (first expre) 'setq) (cond (< (count (next expre)) 2) (list (list '*error* 'list 'expected nil) amb-global)
                                             (igual? (fnext expre) nil) (list (list '*error* 'cannot-set nil) amb-global)
                                             (not (symbol? (fnext expre))) (list (list '*error* 'symbol 'expected (fnext expre)) amb-global)
                                             (= (count (next expre)) 2) (let [res (evaluar (first (nnext expre)) amb-global amb-local)]
                                                                          (list (first res) (actualizar-amb amb-global (fnext expre) (first res))))
                                             true (let [res (evaluar (first (nnext expre)) amb-global amb-local)]
                                                    (evaluar (cons 'setq (next (nnext expre))) (actualizar-amb amb-global (fnext expre) (first res)) amb-local)))
          (igual? (first expre) 'de) (cond (< (count (next expre)) 2) (list (list '*error* 'list 'expected nil) amb-global)
                                           (and (not (igual? (first (nnext expre)) nil)) (not (seq? (first (nnext expre))))) (list (list '*error* 'list 'expected (first (nnext expre))) amb-global)
                                           (igual? (fnext expre) nil) (list (list '*error* 'cannot-set nil) amb-global)
                                           (not (symbol? (fnext expre))) (list (list '*error* 'symbol 'expected (fnext expre)) amb-global)
                                           true (list (fnext expre) (actualizar-amb amb-global (fnext expre) (cons 'lambda (nnext expre)))))
          (igual? (first expre) 'quote) (list (if (igual? (fnext expre) nil) nil (fnext expre)) amb-global)
          (igual? (first expre) 'lambda) (cond (< (count (next expre)) 1) (list (list '*error* 'list 'expected nil) amb-global)
                                               (and (not (igual? (fnext expre) nil)) (not (seq? (fnext expre)))) (list (list '*error* 'list 'expected (fnext expre)) amb-global)
                                               true (list expre amb-global))
          (igual? (first expre) 'cond) (evaluar-cond (next expre) amb-global amb-local)
          true (aplicar (first (evaluar (first expre) amb-global amb-local)) (map (fn [x] (first (evaluar x amb-global amb-local))) (next expre)) amb-global amb-local))))

; cond: macro (evalúa múltiples condiciones)
; de: macro (define función y la liga
; exit: sale del intérprete
; if: forma especial (evalúa una condición)
; lambda: macro (define una func.
; load: carga un archivo
; or: macro (evalúa mientras no obtenga
; quote: forma especial (impide evalua
; setq: forma especial (liga símbolo a valor)



; Aplica una funcion a una lista de argumentos evaluados, usando los ambientes global y local. Siempre retorna una lista con un resultado y un ambiente.
; Si la aplicacion falla, el resultado es una lista con '*error* como primer elemento, por ejemplo: (list '*error* 'arg-wrong-type) y el ambiente es el ambiente global.
; Aridad 4: Recibe la func., la lista de args. evaluados y los ambs. global y local. Se llama recursivamente agregando 2 args.: la func. revisada y la lista de args. revisada.
; Aridad 6: Si la funcion revisada no es nil, se la retorna con el amb. global.
; Si la lista de args. evaluados revisada no es nil, se la retorna con el amb. global.
; Si no, en caso de que la func. sea escalar (predefinida o definida por el usuario), se devuelven el resultado de su aplicacion (controlando la aridad) y el ambiente global.
; Si la func. no es escalar, se valida que la cantidad de parametros y argumentos coincidan, y:
; en caso de que se trate de una func. lambda con un solo cuerpo, se la evalua usando el amb. global intacto y el local actualizado con los params. ligados a los args.,
; en caso de haber multiples cuerpos, se llama a aplicar recursivamente, pasando la funcion lambda sin el primer cuerpo, la lista de argumentos evaluados,
; el amb. global actualizado con la eval. del 1er. cuerpo (usando el amb. global intacto y el local actualizado con los params. ligados a los args.) y el amb. local intacto.
(defn aplicar
  ([f lae amb-global amb-local]
   (aplicar (revisar-f f) (revisar-lae lae) f lae amb-global amb-local))
  ([resu1 resu2 f lae amb-global amb-local]
   (println "* APLICANDO PIPI * ")
   (print "   * f: ")
   (println f)
   (print "   * lae: ")
   (println lae)
   (cond resu1 (list resu1 amb-global)
         resu2 (list resu2 amb-global)
         true  (if (not (seq? f))
                 (list (cond
                         (igual? f 'env) (if (> (count lae) 0)
                                           (list '*error* 'too-many-args)
                                           (concat amb-global amb-local))
                         (igual? f 'first) (let [ari (controlar-aridad lae 1)]
                                             (cond (seq? ari) ari
                                                   (igual? (first lae) nil) nil
                                                   (not (seq? (first lae))) (list '*error* 'list 'expected (first lae))
                                                   true (ffirst lae)))
                         (igual? f 'add) (if (< (count lae) 2)
                                           (list '*error* 'too-few-args)
                                           (try (reduce + lae)
                                                (catch Exception e (list '*error* 'number-expected))))
                         true (let [lamb (buscar f (concat amb-local amb-global))]
                                (cond (or (number? lamb) (igual? lamb 't) (igual? lamb nil)) (list '*error* 'non-applicable-type lamb)
                                      (or (number? f) (igual? f 't) (igual? f nil)) (list '*error* 'non-applicable-type f)
                                      (igual? (first lamb) '*error*) lamb
                                      true (aplicar lamb lae amb-global amb-local)))) amb-global)
                 (cond (< (count lae) (count (fnext f))) (list (list '*error* 'too-few-args) amb-global)
                       (> (count lae) (count (fnext f))) (list (list '*error* 'too-many-args) amb-global)
                       true (if (nil? (next (nnext f)))
                              (evaluar (first (nnext f)) amb-global (concat (reduce concat (map list (fnext f) lae)) amb-local))
                              (aplicar (cons 'lambda (cons (fnext f) (next (nnext f)))) lae (fnext (evaluar (first (nnext f)) amb-global (concat (reduce concat (map list (fnext f) lae)) amb-local))) amb-local))))))
  )



; Retorna True si el valor no es escalar y
; en su primera posicion contiene elemento
(defn primer_elemento? [secuencia elemento]
  (and (sequential? secuencia) (= (first secuencia) elemento)
    )
  )

(defn primer_elemento_error? [secuencia]
  (primer_elemento? secuencia '*error*))

; Retorna valor si indice es par
; retorna nil en caso contrario
(defn pos_par? [pos_valor]
  (even? (first pos_valor))
  )

(defn obtener_valor [pos_valor]
  (second pos_valor)
  )

; Retorna las claves del ambiente
; aquellas que se encuentre en posiciones pares (0, 2, 4...)
(defn obtener_claves [ambiente]

  (print "  < claves: ")
  (println (map-indexed list ambiente))
  (print "  < claves: ")
  (println (filter pos_par? (map-indexed list ambiente)))
  (print "  < claves: ")
  (println (map obtener_valor (filter pos_par? (map-indexed list ambiente))))

  (map obtener_valor (filter pos_par? (map-indexed list ambiente)))
  )

; Devuelve el indice de la clave
; Si la clave no existe retorna un numero negativo
(defn index_clave [ambiente clave]

  (let [indice (.indexOf (obtener_claves ambiente) clave)]
    (print "  < indice clave en claves: ")
    (println indice)
    (print "  < indice clave: ")
    (println (* indice 2))
    (* indice 2)
    )
  )

(defn agregar_varieble [ambiente clave valor]
  (let [indice (index_clave ambiente clave)]
    (if (< indice 0)
      (do (conj (conj ambiente valor) clave))
      (do (assoc ambiente (inc indice) valor))
      )
    )
  )

; Actualiza un ambiente (una lista con claves en las posiciones pares [0, 2, 4..]
; y valores en las pares [1, 2, 3..] Recibe el ambiente, la clave y el valor.
; Si el valor no es escalar y en su primera posicion contiene '*error*,: retorna el ambiente intacto
; Si no, coloca la clave y el valor en el ambiente (puede ser un alta o una actualizacion) y lo retorna.
(defn actualizar-amb [amb-global clave valor]
  (if (primer_elemento_error? amb-global)
    (do amb-global)
    (do (agregar_varieble amb-global clave valor))
    )
  )





; Controla la aridad (cantidad de argumentos de una funcion).
; Recibe una lista y un numero. Si la longitud de la lista coincide con el numero, retorna el numero.
; Si es menor, retorna (list '*error* 'too-few-args).
; Si es mayor, retorna (list '*error* 'too-many-args).
(defn controlar-aridad [lis val-esperado]
  (let [len (count lis)]
    (cond
      (= len val-esperado) val-esperado
      (< len val-esperado) (list '*error* 'too-few-args)
      (> len val-esperado) (list '*error* 'too-many-args)
      )
    )
  )










(defn convert_to_compare
  [elem]
  (cond
    (= elem nil) "nil"
    (= elem "") "nil"
    (= elem '()) "nil"
    (= (clojure.string/lower-case (str elem)) "nil") "nil"
    (not (seq? elem)) (clojure.string/lower-case (str elem))
    true elem
    )
  )

; Compara la igualdad de dos simbolos.
; Recibe dos simbolos a y b. Retorna true si se deben considerar iguales; si no, false.
; Se utiliza porque TLC-LISP no es case-sensitive y ademas no distingue entre nil y la lista vacia.
(defn igual?
  [a b]
  (or
    (= (convert_to_compare a) (convert_to_compare b))
    (= a b)
    )
  )






; define los separadores a imprimir luego de cada elemento de un vecto
; ("a" "b" "c") -> (" ", " ", ")")
(defn def_separadores_vector [elem]
  (concat (take (dec (count elem)) (repeat " ")) ")")
  )


; imprime el elemento y luego el separador
; si el elemento es un string "<elem>"
; si el elemento es un lista (<elemento1> <elemento2> )
;   si cada subelemento es string => agrega
; si no imprime el elemento a secas <elemento>
(defn _imprimir [separador elem]
  (if (not (= elem 'exit ))
    (if (string? elem)
      (do (printf  "\"%s\"" elem))
      (if (list? elem)
        (do (print "(") (dorun (map _imprimir (def_separadores_vector elem) elem)))
        (do (print elem))
        )
      )
    )
  (print separador)
  )

; Imprime, con salto de linea, atomos o listas en formato estandar (las cadenas con comillas) y devuelve su valor. Muestra errores sin parentesis.
; Aridad 1: Si recibe un escalar, lo imprime con salto de linea en formato estandar (pero si es space no lo imprime). purga la salida y devuelve el escalar.
; Si recibe una secuencia cuyo primer elemento es "error", se llama recursivamente con dos argumentos iguales: la secuencia recibida.
; Si no, imprime lo recibido con salto de linea en formato estandar, purga la salida y devuelve la cadena.
; Aridad 2: Si el primer parametro es nil, imprime un salto de linea, purga la salida y devuelve el segundo parametro.
; Si no, imprime su primer elemento en formato estandar, imprime un espacio y se llama recursivamente con la cola del primer parametro y el segundo intacto.
(defn imprimir
  ([elem]
   (if  (primer_elemento? elem "error")
     (imprimir elem elem)
     (do (_imprimir "\n" elem) elem)
     )
   )
  ([lis orig]
   (if (nil? lis)
    (do (print "\n") orig)
    (do (_imprimir " " (first lis)) (imprimir (rest lis) orig))
    )
   )
  )





; Revisa una lista que representa una funcion.
; Recibe la lista y, si esta comienza con '*error*, la retorna. Si no, retorna nil.
(defn revisar-f [lis]
  (if (primer_elemento_error? lis)
    (do lis)
    (do nil)
    )
  )



; Revisa una lista de argumentos evaluados.
; Recibe la lista y, si esta contiene alguna sublista que comienza con '*error*, retorna esa sublista. Si no, retorna nil.
(defn revisar-lae [lis]
  (first (filter primer_elemento_error? lis))
  )





; Busca una clave en un ambiente (una lista con claves en las posiciones pares [0, 2, 4...]
; y valores en las pares [1, 3, 5...] y retorna el val
; Si no la encuentra, retorna una lista con '*error* en la 1ra. pos., 'unbound
(defn buscar [clave ambiente]
  (list '*error* 'unbound)
  (println "<  BUSCAR  >")
  (print "  < clave: ")
  (println clave)
  (print "  < ambiente: ")
  (println ambiente)

  (let [indice (index_clave ambiente clave)]
    (if (< indice 0)
      (list '*error* 'unbound)
      (do (nth ambiente (inc indice)))
      )
    )
  )






;;;;;;;;;;;;;;;;;;;; testear ;;;;;;;;;;;;;;;;;;;;
; Evalua (con evaluar) secuencialmente las sublistas de una lista y retorna el valor de la ultima evaluacion.
(defn evaluar-secuencia-en-cond [lis amb-global amb-local]
  (let [ret (evaluar (first lis) amb-global amb-local)]
    (if (< 1 (count lis))
      (evaluar-secuencia-en-cond (next lis) amb-global amb-local)
      ret
      )
    )
  )

; Evalua el cuerpo de una macro COND. Siempre retorna una lista con un resultado y un ambiente.
; Recibe una lista de sublistas (cada una de las cuales tiene una condicion en su 1ra. posicion) y los ambientes global y local
; Si la lista es nil, el resultado es nil y el ambiente retornado es el global.
; Si no, evalua (con evaluar) la cabeza de la 1ra. sublista y, si el resultado no es nil, retorna el res. de invocar a evaluar-secuencia-en-condicion con la cola de esa sublista.
; En caso contrario, sigue con las demas sublistas.
(defn evaluar-cond [lis amb-global amb-local]
  (if (nil? lis)
    (list nil amb-global)
    (let [cabeza_primer_sublista (ffirst lis), cola_primer_sublista (fnext lis)]
      (if-not (nil? (evaluar cabeza_primer_sublista amb-global amb-local))
        (evaluar-secuencia-en-cond cola_primer_sublista amb-global amb-local)
        (evaluar-cond (next lis) amb-global amb-local))
      )
    )
  )




;;;;; usar como log pata cond ;;;;;
(defn f [lis amb-global amb-local]
  (println "____ EVALUAR COND ___ ")
  (print "  ___ lis: ")
  (println lis)
  (print "  ___ global: ")
  (println amb-global)
  (print "  ___ local: ")
  (println amb-local)
  (let [variable (evaluar-cond lis amb-global amb-local)]
    (print "  ___ resultado: ")
    (println variable)
    )
  )


(println "--- testiro ----")
; (println (f (list (list 't 't 't)) (list '+ 'add 't 't 'nil 'nil) (list)))

(println "---   FIN   ---")
;;;;; fin log cond ;;;;;



(repl)

; Falta terminar de implementar las 2 funciones anteriores (aplicar y evaluar)

; Falta implementar las 9 funciones auxiliares (actualizar-amb, controlar-aridad, imprimir, buscar, etc.)

; Falta hacer que la carga del interprete en Clojure (tlc-lisp.clj) retorne true
