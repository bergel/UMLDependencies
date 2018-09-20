private - utils
classFor: kind
	(#('class' 'Class') includes: kind) ifTrue: [ ^ UMLClass ].
	kind = 'enum' ifTrue: [ ^ UMLEnum ].
	kind = 'abstract class' ifTrue: [ ^ UMLAbstractClass ].
	kind = 'interface' ifTrue: [ ^ UMLInterface ].
	self error: 'Should not be there'