private - utility
outterClassNameFor: aString 
	"Return nil if no outterclasse exist of if the argument does not point to an inner class"
	| ss beforeLast t |
	ss := aString substrings: '.'.
	beforeLast := ss at: (ss size - 1).
	t := beforeLast first isUppercase.
	^ t ifTrue: [ beforeLast ] ifFalse: [ nil ]