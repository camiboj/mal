(require '[clojure.test :refer [is deftest run-tests]])

(load-file "../../src/tp/tcl-lisp.clj")

;;;;;;;;;;;;;;;;;;;;;;;;;;;; UNIT TEST ;;;;;;;;;;;;;;;;;;;;;;;;;;;;
(deftest test-aplicar
  (is (= '((a b) (cons cons)) (aplicar 'cons '(a (b)) '(cons cons) nil)))
  (is (= '(9 (+ add r 5)) (aplicar 'add '(4 5) '(+ add r 5) nil)))
  (is (= '(8 (+ add r 4 doble (lambda (x) (+ x x)))) (evaluar '(doble r) '(+ add r 4 doble (lambda (x) (+ x x))) nil)))
  (is (= '(8 (+ add r 4 doble (lambda (x) (+ x x)))) (aplicar '(lambda (x) (+ x x)) '(4) '(+ add r 4 doble (lambda (x) (+ x x))) nil)))
  )


(deftest test-evaluar
  (is (= '(3 (r 3 + add)) (evaluar '(setq r 3) '(+ add) nil)))
  (is (= '(doble (doble (lambda (x) (+ x x)) + add)) (evaluar '(de doble (x) (+ x x)) '(+ add) nil)))
  (is (= '(5 (+ add)) (evaluar '(+ 2 3) '(+ add) nil)))
  (is (= '((*error* unbound-symbol +) (add add)) (evaluar '(+ 2 3) '(add add) nil)))
  (is (= '(6 (+ add doble (lambda (x) (+ x x)))) (evaluar '(doble 3) '(+ add doble (lambda (x) (+ x x))) nil)))
  (is (= '(8 (+ add r 4 doble (lambda (x) (+ x x)))) (evaluar '(doble r) '(+ add r 4 doble (lambda (x) (+ x x))) nil)))
  (is (= '(6 (+ add)) (evaluar '((lambda (x) (+ x x)) 3) '(+ add) nil)))
  )


(deftest test-controlar-aridad
  (is (= '(*error* too-few-args) (controlar-aridad '(a b c) 4)))
  (is (= '4 (controlar-aridad '(a b c d) 4)))
  (is (= '(*error* too-many-args) (controlar-aridad '(a b c d e) 4)))
  )


(deftest test-igual
  (is (= true (igual? nil 'NIL)))
  (is (= true (igual? nil "NIL")))
  (is (= true (igual? () 'NIL)))
  (is (= true (igual? '() nil)))
  (is (= true (igual? nil '())))
  (is (= true (igual? '() '())))
  (is (= true (igual? nil nil)))
  (is (= true (igual? "A" "a")))
  (is (= true (igual? "a" "A")))

  (is (= false (igual? '() "A")))
  (is (= false (igual? nil "A")))
  (is (= false (igual? "A" '())))
  (is (= false (igual? "A" nil)))
  (is (= false (igual? "A" "aa")))
  (is (= false (igual? '(1) nil)))
  (is (= false (igual? nil '(1))))
  (is (= false (igual? "a" '("a"))))

  (is (= false (= nil 'NIL)))
  (is (= false (= nil "NIL")))
  (is (= false (= nil ())))
  (is (= false (= () 'NIL)))
  )

(deftest test-imprimir
  ; = "hola" output
  (is (= "hola" (imprimir "hola")))
  ; = 5 output
  (is (= 5 (imprimir 5)))
  ; = a output
  (is (= 'a (imprimir 'a)))
  ; =   output
  (is (= \space (imprimir \space)))
  ; = (hola "mundo") output
  (is (= '(hola "mundo") (imprimir '(hola "mundo"))))
  ; = (*error* hola "mundo") output
  (is (= '(*error* hola "mundo") (imprimir '(*error* hola "mundo"))))
  )

(deftest test-actualizar-ambiente
  (is (= '(x 1 + add - sub) (actualizar-amb '(+ add - sub) 'x 1)))
  (is (= '(+ add - sub x 3 Y 2) (actualizar-amb '(+ add - sub x 1 Y 2) 'x 3)))
  )

(deftest test-revisar-f
  (is (= nil (revisar-f '(lista sin error))))
  (is (= nil (revisar-f 'doble)))
  (is (= '(*error* two-few-args) (revisar-f '(*error* two-few-args))))
  )

(deftest test-revisar-lea
  (is (= nil (revisar-lae '(1 add first))))
  (is (= '(*error* too-many-args) (revisar-lae '(1 add (*error* too-many-args) first))))
  )

(deftest test-buscar
  (is (= 'sub (buscar '- '(+ add - sub))))
  (is (= '(*error* unbound-symbol doble) (buscar 'doble '(+ add - sub))))
  )


(deftest test-evaluar-secuencia-en-cond
  (is (= '(2 (y 2 setq setq)) (evaluar-secuencia-en-cond '((setq y 2)) '(setq setq) nil)))
  (is (= '(3 (z 3 y 2 setq setq)) (evaluar-secuencia-en-cond '((setq y 2) (setq z 3)) '(setq setq) nil)))
  )

(def amb-global '(add add append append cond cond cons cons de de env env equal equal eval eval exit exit
                      first first ge ge gt gt if if lambda lambda length length list list load load lt lt nil nil not not
                      null null or or prin3 prin3 quote quote read read rest rest reverse reverse setq setq sub sub
                      t t terpri terpri + add - sub))



;;;;;;;;;;;;;;;;;;;;;;;;;;;; INTERPRETE ;;;;;;;;;;;;;;;;;;;;;;;;;;;;


(deftest test-append
  (is (= '((1) (append append nil nil))  (evaluar '(append '(1) nil) '(append append nil nil) nil)))
  (is (= '((1) (append append nil nil))  (evaluar '(append nil '(1)) '(append append nil nil) nil)))
  (is (= '(() (append append nil nil))  (evaluar '(append nil nil) '(append append nil nil) nil)))
  (is (= '((1 2) (append append nil nil))  (evaluar '(append '(1) '(2)) '(append append nil nil) nil)))
  (is (= '((*error* list expected 1) (append append nil nil))  (evaluar '(append 1 '(2)) '(append append nil nil) nil)))
  (is (= '((*error* too-many-args) (append append nil nil))  (evaluar '(append '(1) '(2) '(3)) '(append append nil nil) nil)))
  (is (= '((*error* too-few-args) (append append nil nil))  (evaluar '(append '(1)) '(append append nil nil) nil)))
  (is (= '((*error* not-implemented) (append append nil nil))  (evaluar '(append '(1) 2) '(append append nil nil) nil)))
  (is (= '((*error* not-implemented) (append append nil nil))  (evaluar '(append '(1) "a") '(append append nil nil) nil)))
  )


(deftest test-cons
  (is (= '((0 1 2 3) (cons cons nil nil))  (evaluar '(cons 0 '(1 2 3)) '(cons cons nil nil) nil)))
  (is (= '(((1 2) 3) (cons cons nil nil))  (evaluar '(cons '(1 2) '(3)) '(cons cons nil nil) nil)))
  (is (= '((1) (cons cons nil nil))  (evaluar '(cons 1 '()) '(cons cons nil nil) nil)))
  (is (= '((1) (cons cons nil nil))  (evaluar '(cons 1 nil) '(cons cons nil nil) nil)))
  (is (= '((nil) (cons cons nil nil))  (evaluar '(cons nil nil) '(cons cons nil nil) nil)))
  (is (= '((nil) (cons cons nil nil))  (evaluar '(cons '() '()) '(cons cons nil nil) nil)))
  (is (= '((*error* too-few-args) (cons cons nil nil))  (evaluar '(cons '(1 2)) '(cons cons nil nil) nil)))
  (is (= '((*error* too-many-args) (cons cons nil nil))  (evaluar '(cons '(1 2) '(3) '(4)) '(cons cons nil nil) nil)))
  (is (= '((*error* not-implemented) (cons cons nil nil))  (evaluar '(cons '(1 2 3) 4) '(cons cons nil nil) nil)))
  (is (= '((*error* not-implemented) (cons cons nil nil))  (evaluar '(cons '(1 2 3) "a") '(cons cons nil nil) nil)))
  )

(deftest test-equal
  (is (= '(t (equal equal nil nil))  (evaluar '(equal 1 1) '(equal equal nil nil) nil)))
  (is (= '(nil (equal equal nil nil))  (evaluar '(equal 1 2) '(equal equal nil nil) nil)))
  (is (= '(t (equal equal nil nil))  (evaluar '(equal '() nil) '(equal equal nil nil) nil)))
  (is (= '(t (equal equal nil nil))  (evaluar '(equal nil '()) '(equal equal nil nil) nil)))
  (is (= '(t (equal equal nil nil))  (evaluar '(equal '(1 2) '(1 2)) '(equal equal nil nil) nil)))
  (is (= '((*error* too-many-args) (equal equal nil nil))  (evaluar '(equal 1 1 1) '(equal equal nil nil) nil)))
  (is (= '((*error* too-few-args) (equal equal nil nil))  (evaluar '(equal 1) '(equal equal nil nil) nil)))
)

(deftest test-eval
  (is (= '(1 (eval eval nil nil))  (evaluar '(eval 1) '(eval eval nil nil) nil)))
  (is (= '(3 (eval eval nil nil + add))  (evaluar '(eval '(+ 1 2)) '(eval eval nil nil + add) nil)))
  (is (= '(nil (eval eval nil nil))  (evaluar '(eval nil) '(eval eval nil nil) nil)))
  (is (= '(nil (eval eval nil nil))  (evaluar '(eval '()) '(eval eval nil nil) nil)))
  (is (= '((*error* too-many-args) (eval eval nil nil))  (evaluar '(eval '(1) '(2)) '(eval eval nil nil) nil)))
  (is (= '((*error* too-few-args) (eval eval nil nil))  (evaluar '(eval) '(eval eval nil nil) nil)))
  (is (= '((*error* non-applicable-type 1) (eval eval nil nil))  (evaluar '(eval '(1)) '(eval eval nil nil) nil)))
  )


(deftest test-ge
  (is (= '(t (ge ge nil nil))  (evaluar '(ge 2 1) '(ge ge nil nil) nil)))
  (is (= '(t (ge ge nil nil))  (evaluar '(ge 1 1) '(ge ge nil nil) nil)))
  (is (= '(nil (ge ge nil nil))  (evaluar '(ge 1 2) '(ge ge nil nil) nil)))
  (is (= '((*error* number expected (1)) (ge ge nil nil))  (evaluar '(ge '(1) 2) '(ge ge nil nil) nil)))
  (is (= '((*error* number expected (2)) (ge ge nil nil))  (evaluar '(ge 1 '(2)) '(ge ge nil nil) nil)))
  (is (= '((*error* too-many-args) (ge ge nil nil))  (evaluar '(ge 1 2 3) '(ge ge nil nil) nil)))
  (is (= '((*error* too-few-args) (ge ge nil nil))  (evaluar '(ge 1) '(ge ge nil nil) nil)))
  )



(deftest test-gt
  (is (= '(t (gt gt nil nil))  (evaluar '(gt 2 1) '(gt gt nil nil) nil)))
  (is (= '(nil (gt gt nil nil))  (evaluar '(gt 1 1) '(gt gt nil nil) nil)))
  (is (= '(nil (gt gt nil nil))  (evaluar '(gt 1 2) '(gt gt nil nil) nil)))
  (is (= '((*error* number expected (1)) (gt gt nil nil))  (evaluar '(gt '(1) 2) '(gt gt nil nil) nil)))
  (is (= '((*error* number expected (2)) (gt gt nil nil))  (evaluar '(gt 1 '(2)) '(gt gt nil nil) nil)))
  (is (= '((*error* too-many-args) (gt gt nil nil))  (evaluar '(gt 1 2 3) '(gt gt nil nil) nil)))
  (is (= '((*error* too-few-args) (gt gt nil nil))  (evaluar '(gt 1) '(gt gt nil nil) nil)))
  )



(deftest test-length
  (is (= '(3 (length length nil nil))  (evaluar '(length '(1 2 3)) '(length length nil nil) nil)))
  (is (= '(0 (length length nil nil))  (evaluar '(length '()) '(length length nil nil) nil)))
  (is (= '(0 (length length nil nil))  (evaluar '(length nil) '(length length nil nil) nil)))
  (is (= '(1 (length length nil nil))  (evaluar '(length "a") '(length length nil nil) nil)))
  (is (= '((*error* arg-wrong-type 1) (length length nil nil))  (evaluar '(length 1 ) '(length length nil nil) nil)))
  (is (= '((*error* too-many-args) (length length nil nil))  (evaluar '(length '(1) '(2)) '(length length nil nil) nil)))
  (is (= '((*error* too-few-args) (length length nil nil))  (evaluar '(length) '(length length nil nil) nil)))
  )

(deftest test-list
 (is (= '((nil) (list list nil nil))  (evaluar '(list '()) '(list list nil nil) nil)))
 (is (= '((1 2 3) (list list nil nil))  (evaluar '(list 1 2 3) '(list list nil nil) nil)))
 (is (= '((nil) (list list nil nil))  (evaluar '(list nil) '(list list nil nil) nil)))
 (is (= '((1 "a" 2 "b") (list list nil nil))  (evaluar '(list 1 "a" 2 "b") '(list list nil nil) nil)))
 )


(deftest test-lt
  (is (= '(t (lt lt nil nil))  (evaluar '(lt 1 2) '(lt lt nil nil) nil)))
  (is (= '(nil (lt lt nil nil))  (evaluar '(lt 1 1) '(lt lt nil nil) nil)))
  (is (= '(nil (lt lt nil nil))  (evaluar '(lt 2 1) '(lt lt nil nil) nil)))
  (is (= '((*error* number expected (1)) (lt lt nil nil))  (evaluar '(lt '(1) 2) '(lt lt nil nil) nil)))
  (is (= '((*error* number expected (2)) (lt lt nil nil))  (evaluar '(lt 1 '(2)) '(lt lt nil nil) nil)))
  (is (= '((*error* too-many-args) (lt lt nil nil))  (evaluar '(lt 1 2 3) '(lt lt nil nil) nil)))
  (is (= '((*error* too-few-args) (lt lt nil nil))  (evaluar '(lt 1) '(lt lt nil nil) nil)))
  )


(deftest test-not
  (is (= '(nil (not not nil nil t t equal equal))  (evaluar '(not t) '(not not nil nil t t equal equal) nil)))
  (is (= '(t (not not nil nil t t equal equal))  (evaluar '(not nil) '(not not nil nil t t equal equal) nil)))
  (is (= '(nil (not not nil nil t t equal equal))  (evaluar '(not (equal 1 1)) '(not not nil nil t t equal equal) nil)))
  (is (= '(t (not not nil nil t t equal equal))  (evaluar '(not (equal 1 2)) '(not not nil nil t t equal equal) nil)))
  (is (= '(nil (not not nil nil t t equal equal))  (evaluar '(not 1) '(not not nil nil t t equal equal) nil)))
  (is (= '(nil (not not nil nil t t equal equal))  (evaluar '(not '(1 2)) '(not not nil nil t t equal equal) nil)))
  (is (= '((*error* too-many-args) (not not nil nil t t equal equal))  (evaluar '(not t t) '(not not nil nil t t equal equal) nil)))
  (is (= '((*error* too-few-args) (not not nil nil t t equal equal))  (evaluar '(not) '(not not nil nil t t equal equal) nil)))
  )

(deftest test-null
 (is (= '(nil (null null nil nil t t equal equal))  (evaluar '(null t) '(null null nil nil t t equal equal) nil)))
 (is (= '(t (null null nil nil t t equal equal))  (evaluar '(null nil) '(null null nil nil t t equal equal) nil)))
 (is (= '(nil (null null nil nil t t equal equal))  (evaluar '(null (equal 1 1)) '(null null nil nil t t equal equal) nil)))
 (is (= '(t (null null nil nil t t equal equal))  (evaluar '(null (equal 1 2)) '(null null nil nil t t equal equal) nil)))
 (is (= '(nil (null null nil nil t t equal equal))  (evaluar '(null 1) '(null null nil nil t t equal equal) nil)))
 (is (= '(nil (null null nil nil t t equal equal))  (evaluar '(null '(1 2)) '(null null nil nil t t equal equal) nil)))
 (is (= '((*error* too-many-args) (null null nil nil t t equal equal))  (evaluar '(null t t) '(null null nil nil t t equal equal) nil)))
 (is (= '((*error* too-few-args) (null null nil nil t t equal equal))  (evaluar '(null) '(null null nil nil t t equal equal) nil)))
 )


(deftest test-prin3
 ; ouput 1
 (is (= '(1 (prin3 prin3 nil nil))  (evaluar '(prin3 1) '(prin3 prin3 nil nil) nil)))
 ; ouput '(1 2)
 (is (= '((1 2) (prin3 prin3 nil nil))  (evaluar '(prin3 '(1 2)) '(prin3 prin3 nil nil) nil)))
 (is (= '((*error* stream expected 2) (prin3 prin3 nil nil))  (evaluar '(prin3 1 2) '(prin3 prin3 nil nil) nil)))
 (is (= '((*error* too-few-args) (prin3 prin3 nil nil))  (evaluar '(prin3) '(prin3 prin3 nil nil) nil)))
 )



(deftest test-rest
 (is (= '((2 3) (rest rest nil nil))   (evaluar '(rest '(1 2 3)) '(rest rest nil nil) nil)))
 (is (= '(nil (rest rest nil nil))   (evaluar '(rest '()) '(rest rest nil nil) nil)))
 (is (= '(nil (rest rest nil nil))   (evaluar '(rest nil) '(rest rest nil nil) nil)))
 (is (= '((*error* list-expected 1) (rest rest nil nil))   (evaluar '(rest 1) '(rest rest nil nil) nil)))
 (is (= '((*error* too-many-args) (rest rest nil nil))   (evaluar '(rest '(1) '(2)) '(rest rest nil nil) nil)))
 (is (= '((*error* too-few-args) (rest rest nil nil))   (evaluar '(rest) '(rest rest nil nil) nil)))
 )


(deftest test-reverse
  (is (= '((3 2 1) (reverse reverse nil nil)) (evaluar '(reverse '(1 2 3)) '(reverse reverse nil nil) nil)))
  (is (= '(nil (reverse reverse nil nil)) (evaluar '(reverse nil) '(reverse reverse nil nil) nil)))
  (is (= '(nil (reverse reverse nil nil)) (evaluar '(reverse '()) '(reverse reverse nil nil) nil)))
  (is (= '((*error* list expected 1) (reverse reverse nil nil)) (evaluar '(reverse 1) '(reverse reverse nil nil) nil)))
  (is (= '((*error* too-many-args) (reverse reverse nil nil)) (evaluar '(reverse '(1) '(2)) '(reverse reverse nil nil) nil)))
  (is (= '((*error* too-few-args) (reverse reverse nil nil)) (evaluar '(reverse) '(reverse reverse nil nil) nil)))
  )


(deftest test-terpri
  (is (= '(nil (terpri terpri nil nil)) (evaluar '(terpri) '(terpri terpri nil nil) nil)))
  (is (= '((*error* stream expected 1) (terpri terpri nil nil)) (evaluar '(terpri 1) '(terpri terpri nil nil) nil)))

  )



;;;;;;;;;;;;;;;;;;;;;;;;;;;; TODO
; (deftest test-load
;   (is (= (load-file "tlc-lisp.clj")))
;  )



(deftest test-evaluar-cond
  (is (= '(nil (equal equal setq setq)) (evaluar-cond nil '(equal equal setq setq) nil)))
  (is (= '(nil (equal equal first first)) (evaluar-cond '(((equal 1 2) (setq x 1))) '(equal equal first first) nil)))
  (is (= '(nil (equal equal first first)) (evaluar-cond '(((equal 'a 'b) (setq x 1))) '(equal equal first first) nil)))
  (is (= '(2 (y 2 equal equal setq setq)) (evaluar-cond '(((equal 'a 'b) (setq x 1)) ((equal 'a 'a) (setq y 2))) '(equal equal setq setq) nil)))
  (is (= '(3 (z 3 y 2 equal equal setq setq)) (evaluar-cond '(((equal 'a 'b) (setq x 1)) ((equal 'a 'a) (setq y 2) (setq z 3))) '(equal equal setq setq) nil)))
)






(run-tests)