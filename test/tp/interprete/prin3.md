# prin3

## casos
```clojure
>>> (prin3 1)
1
1

>>> (prin3 '(1 2))
'(1 2)
'(1 2)


;;; cant(parametro) != 1
>>> (prin3 1 2)
 (*error* stream expected 2)
>>> (prin3)
 (*error* too-few-args)
```