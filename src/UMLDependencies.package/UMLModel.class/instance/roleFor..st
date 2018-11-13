private - utility
roleFor: aString 
	| r aRole |
	r := roles detect: [ :pp | pp roleName = aString ] ifNone: [ nil ].
	r ifNotNil: [ ^ r ].
	
	aRole := UMLRole new.
	aRole roleName: aString.
	roles add: aRole.
	aRole model: self.
	^ aRole