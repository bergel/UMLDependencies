public - importing Pharo
resolveDependencies
	"Resolve dependencies between imported Pharo classes"

	| dependentClasses |
	self classes do: [ :cls | 
		cls superclass: (self classNamed: cls superclassName).
		
		dependentClasses := (cls dependentClassesNames collect: [ :clsName | self classNamed: clsName ]) copyWithout: nil.
		dependentClasses do: [ :cc | cls dependsOn: cc ] ].
	 