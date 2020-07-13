# length

## casos
```clojure
>>> (not t)
 nill
>>> (not nill)
 t

>>> (not (equal 1 1))
 nil
>>> (not (equal 1 2))
 t

>>> (not 1)
 nil
>>> (not '(1 2))
 nil



;;; cant(parametro) != 1
>>> (not t t)
 (*error* too-many-args)
>>> (not)
 (*error* too-few-args)
```