# eval

## casos
```clojure
>>> (eval 1)
 1
>>> (eval '(+ 1 2))
 3
>>> (eval nil)
 nil
>>> (eval '())
 nil


>>> (eval 1 '(2))
 (*error* list expectect)

;;; cant(parametro) != 1
>>> (eval '(1) '(2))
 (*error* too-many-args)
>>> (eval)
 (*error* too-few-args)

;;; errores al aplicar la lista
>>> (eval '(1))
 (list '*error* 'non-applicable-type 1)
>>> (eval '(reverse 1))
 *error* list expectect 1
```