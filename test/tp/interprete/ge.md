# ge

## casos
```clojure
>>> (ge 2 1)
 t
>>> (ge 1 1)
 t
>>> (ge 1 2)
 nil


>>> (ge '(1) 2)
 (*error* number expected (1))
>>> (ge 1 '(2))
 (*error* number expected (2))


;;; cant(parametro) != 2
>>> (ge 1 2 3)
 (*error* too-many-args)
>>> (ge 1)
 (*error* too-few-args)
```