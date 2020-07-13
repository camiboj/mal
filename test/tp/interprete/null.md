# length

## casos
```clojure
>>> (null t)
 nil
>>> (null nil)
 t

>>> (null (equal 1 1))
 nil
>>> (null (equal 1 2))
 t

>>> (null 1)
 nil
>>> (null '(1 2))
 nil



;;; cant(parametro) != 1
>>> (null t t)
 (*error* too-many-args)
>>> (null)
 (*error* too-few-args)
```