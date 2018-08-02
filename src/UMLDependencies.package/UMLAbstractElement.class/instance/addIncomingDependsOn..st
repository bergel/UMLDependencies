public - dependencies
addIncomingDependsOn: anElement
	"Make the argument depends on me. Calling this method creates an _incoming_ dependency."
	incomingDependencies add: anElement