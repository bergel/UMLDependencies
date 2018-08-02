public - dependencies
implements: anElement
	"I implement anElement. I represent an outgoing dependency"
	self assert: [ anElement isInterface ].
	implementations add: anElement.
	anElement isImplementedBy: self