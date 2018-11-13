private - utility
packageFor: aString 
	| p pak |
	p := packages detect: [ :pp | pp packageName = aString ] ifNone: [ nil ].
	p ifNotNil: [ ^ p ].
	
	pak := UMLPackage new.
	pak packageName: aString.
	packages add: pak.
	pak model: self.
	^ pak