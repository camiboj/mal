# length

## casos
```clojure
>>> (length '(1 2 3))
 3
>>> (length '())
 0
>>> (length nil)
 0
>>> (length "a")
 1

>>> (length 1 )
 (*error* arg-wrong-type 1)


;;; cant(parametro) != 2
>>> (length '(1) '(2))
 (*error* too-many-args)
>>> (length)
 (*error* too-few-args)
```