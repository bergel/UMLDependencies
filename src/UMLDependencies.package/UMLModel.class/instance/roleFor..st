private - utility
roleFor: aString 
	| r pak |
	r := roles detect: [ :pp | pp roleName = aString ] ifNone: [ nil ].
	r ifNotNil: [ ^ r ].
	
	pak := UMLRole new.
	pak roleName: aString.
	roles add: pak.
	^ pak