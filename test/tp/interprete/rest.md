# rest

## casos
```clojure
>>> (rest '(1 2 3))
 3
>>> (rest '())
 nil
>>> (rest nil)
 nil

>>> (rest 1)
 (*error* list-expected 1)


;;; cant(parametro) != 1
>>> (rest '(1) '(2))
 (*error* too-many-args)
>>> (rest)
 (*error* too-few-args)
```