accessing - dependencies
allIncomingDependencies
	"Return the list of dependent elements, all kind of __outgoing__ dependencies"
	^ self incomingDependencies, self implementedBy, self superclasses