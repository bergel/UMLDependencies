public - dependencies
dependsOn: anElement
	"I depend on anElement. I represent an outgoingDependency"
	dependencies add: anElement.
	anElement addIncomingDependsOn: self