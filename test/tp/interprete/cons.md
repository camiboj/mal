# cons

## casos
```clojure
>>> (cons 0 '(1 2 3))
 (0 1 2 3)
>>> (cons '(1 2) '(3))
 ((1 2) 3) 

>>> (cons 1 '())
 (1) 
>>> (cons 1 nil)
 (1) 
>>> (cons nil nil)
 (nil) ; (()) ARREGLAR
>>> (cons '() '())
 (nil)

>>> (cons '(1 2))
 (list '*error* 'too-few-args)
>>> (cons '(1 2) '(3) '(4))
 (list '*error* 'too-many-args)


>>> (cons '(1 2 3) 4)
 (*error* not-implemented)
>>> (cons '(1 2 3) "a")
 (*error* not-implemented)
```