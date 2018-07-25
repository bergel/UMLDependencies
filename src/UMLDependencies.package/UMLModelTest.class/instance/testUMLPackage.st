tests
testUMLPackage
	| r |
	r := UMLPackage new packageName: 'com.foo.org.aPackage'.
	self assert: r printString equals: '<com.foo.org.aPackage>'.
	self assert: r mostRelevantPackageName equals: 'aPackage'