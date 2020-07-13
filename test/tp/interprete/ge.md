# ge

## casos
```clojure
>>> (ge 1 1)
 t
>>> (ge 2 1)
 t
>>> (ge 1 2)
 nil


>>> (ge '(1) 2)
 (*error* list expectect (1))


;;; cant(parametro) != 2
>>> (ge 1 2 3)
 (*error* too-many-args)
>>> (ge 1)
 (*error* too-few-args)
```