# reverse

## casos
```clojure
>>> (reverse nil)
 nil
>>> (reverse '())
 nil
>>> (reverse '(1 2 3))
 (3 2 1)

>>> (reverse 1)
 *error* list expectect 1


;;; recibir solo 2 parametro
>>> (reverse '(1) '(2))
 (*error* too-many-args)
>>> (reverse)
 (*error* too-few-args)
```