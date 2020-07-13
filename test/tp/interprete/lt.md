# lt

## casos
```clojure
>>> (lt 1 2)
 t
>>> (lt 1 1)
 nil
>>> (lt 2 1)
 nil


>>> (lt '(1) 2)
 (*error* number-expectect (1))
>>> (lt 1 '(2))
 (*error* number-expectect (2))


;;; cant(parametro) != 2
>>> (lt 1 2 3)
 (*error* too-many-args)
>>> (lt 1)
 (*error* too-few-args)
```