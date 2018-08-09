public - importing Pharo
resolveDependencies
	"Resolve dependencies between imported Pharo classes"

	| dependentClasses aSuperclass |
	self classes do: [ :cls | 
		aSuperclass := self classNamed: cls superclassName.
		aSuperclass ifNotNil: [ cls superclass: aSuperclass ].
		
		dependentClasses := (cls dependentClassesNames collect: [ :clsName | self classNamed: clsName ]) copyWithout: nil.
		dependentClasses do: [ :cc | cls dependsOn: cc ] ].
	 