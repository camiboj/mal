# cons

## casos
```clojure
>>> (evaluar (cons 0 '(1 2 3)) '(cons cons nil nil) nil)
 (0 1 2 3)
>>> (evaluar (cons '(1 2) '(3)) '(cons cons nil nil) nil)
 ((1 2) 3) 

>>> (evaluar (cons 1 '()) '(cons cons nil nil) nil)
 (1) 
>>> (evaluar (cons 1 nil) '(cons cons nil nil) nil)
 (1) 
>>> (evaluar (cons nil nil) '(cons cons nil nil) nil)
 (nil)
>>> (evaluar (cons '() '()) '(cons cons nil nil) nil)
 (nil)

>>> (evaluar (cons '(1 2)) '(cons cons nil nil) nil)
 (list '*error* 'too-few-args)
>>> (evaluar (cons '(1 2) '(3) '(4)) '(cons cons nil nil) nil)
 (list '*error* 'too-many-args)


>>> (evaluar (cons '(1 2 3) 4) '(cons cons nil nil) nil)
 (*error* not-implemented)
>>> (evaluar (cons '(1 2 3) "a") '(cons cons nil nil) nil)
 (*error* not-implemented)
```