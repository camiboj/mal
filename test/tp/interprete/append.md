# append

## casos
```clojure
>>> (append '(1) nil)
 nil
>>> (append nil '(1))
 nil
>>> (append nil nil)
 nil
>>> (append '(1) '(2))
 (1 2)


>>> (append 1 '(2))
 (*error* list expectect 1)

;;; cant(parametro) != 2
>>> (append '(1) '(2) '(3))
 (*error* too-many-args)
>>> (append '(1))
 (*error* too-few-args)

;;; segundo no es lista
>>> (append '(1) 2)
 (list '*error* 'not-implemented)
>>> (append '(1) "a")
 (list '*error* 'not-implemented)
```