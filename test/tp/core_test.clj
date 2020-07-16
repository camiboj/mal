(require '[clojure.test :refer [is deftest run-tests]])

(load-file "core.clj")

(deftest test-controlar-aridad
  (is (= '(*error* too-few-args) (controlar-aridad '(a b c) 4)))
  (is (= 4 (controlar-aridad '(a b c d) 4)))
  (is (= '(*error* too-many-args) (controlar-aridad '(a b c d e) 4)))
  )

(deftest test-aplicar
  (is (= '((a b) (cons cons)) (aplicar 'cons '(a (b)) '(cons cons) nil)))
  (is (= '(9 (+ add r 5)) (aplicar 'add '(4 5) '(+ add r 5) nil)))
  (is (= '(8 (+ add r 4 doble (lambda (x) (+ x x)))) (aplicar '(lambda (x) (+ x x)) '(4) '(+ add r 4 doble (lambda (x) (+ x x))) nil)))
  )


(deftest test-EVALUAR
  (is (= '(3 (r 3 + add)) (evaluar '(setq r 3) '(+ add) nil)))
  (is (= '(doble (doble (lambda (x) (+ x x)) + add)) (evaluar '(de doble (x) (+ x x)) '(+ add) nil)))
  (is (= '(5 (+ add)) (evaluar '(+ 2 3) '(+ add) nil)))
  (is (= '((*error* unbound-symbol +) (add add)) (evaluar '(+ 2 3) '(add add) nil)))
  (is (= '(6 (+ add doble (lambda (x) (+ x x)))) (evaluar '(doble 3) '(+ add doble (lambda (x) (+ x x))) nil)))
  (is (= '(8 (+ add r 4 doble (lambda (x) (+ x x)))) (evaluar '(doble r) '(+ add r 4 doble (lambda (x) (+ x x))) nil)))
  (is (= '(6 (+ add)) (evaluar '((lambda (x) (+ x x)) 3 ) '(+ add) nil)))
  )

(deftest test-load-file
  (is (= true (load-file &quot;tlc-lisp.clj&quot;))))

(run-tests)