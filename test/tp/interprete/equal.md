# equal

## casos
```clojure
(equal 1 1)
>>> t
(equal 1 2)
>>> nil
>>> (equal '() nil)
t
>>> (equal nil '())
t
>>> (equal '(1 2) '(1 2))
t

>>> (equal 1 1 1)
 error* too-many-args 
>>> (equal 1)
error* too-few-args 
```