# gt

## casos
```clojure
>>> (gt 2 1)
 t
>>> (gt 1 1)
 nil
>>> (gt 1 2)
 nil


>>> (gt '(1) 2)
 (*error* number expected (1))
>>> (gt 1 '(2))
 (*error* number expected (2))


;;; cant(parametro) != 2
>>> (gt 1 2 3)
 (*error* too-many-args)
>>> (gt 1)
 (*error* too-few-args)
```