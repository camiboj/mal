(require '[clojure.test :refer [is deftest run-tests]])

(load-file "../../src/tp/tcl-lisp.clj")

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
  ; (is (= '(*error* too-many-args) (revisar-lae '(1 add '(*error* too-many-args) first))))
  )

(deftest test-buscar
  (is (= 'sub (buscar '- '(+ add - sub))))
  (is (= '(*error* unbound-symbol doble) (buscar 'doble '(+ add - sub))))
  )


(deftest test-evaluar-secuencia-en-cond
  (is (= '(2 (y 2 setq setq)) (evaluar-secuencia-en-cond '((setq y 2)) '(setq setq) nil)))
  (is (= '(3 (z 3 y 2 setq setq)) (evaluar-secuencia-en-cond '((setq y 2) (setq z 3)) '(setq setq) nil)))
  )



;;;;;;;;;;;;;;;;;;;;;;;;;;;; TODO
; (deftest test-load
;   (is (= (load-file "tlc-lisp.clj")))
;  )



; (deftest test-evaluar-cond
;   (is (= '(nil (equal equal setq setq)) (evaluar-cond nil '(equal equal setq setq) nil)))
;   (is (= '(nil (equal equal first first)) (evaluar-cond '(((equal 'a 'b) (setq x 1))) '(equal equal first first) nil)))
;   ; (2 (equal equal setq setq y 2))
;   (is (= '((equal equal setq setq) nil) (evaluar-cond '(((equal 'a 'b) (setq x 1)) ((equal 'a 'a) (setq y 2))) '(equal equal setq setq) nil)))
;   ; (3 (equal equal setq setq y 2 z 3))
;   (is (= '((equal equal setq setq) nil) '(evaluar-cond ((equal 'a 'b) (setq x 1)) ((equal 'a 'a) (setq y 2) (setq z 3))) '(equal equal setq setq) nil))
;   )






(run-tests)