public - importing Pharo
resolveDependencies
	"Resolve dependencies between imported Pharo classes"
	self classes do: [ :cls |
		cls superclass: (self classNamed: cls superclassName).
		]