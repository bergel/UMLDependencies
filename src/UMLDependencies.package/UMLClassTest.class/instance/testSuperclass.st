tests
testSuperclass

	| c1 c2 |
	c1 := UMLClass new.
	c2 := UMLClass new.

	self deny: c1 hasSuperclasses.	
	self deny: c1 hasSubclasses. 
	self deny: c2 hasSuperclasses.
	self deny: c2 hasSubclasses.
	
	self assert: c1 numberOfAllDependencies isZero.

	c1 subclassOf: c2.
	self deny: c1 hasSuperclasses.	
	self assert: c1 hasSubclasses. 
	self assert: c2 hasSuperclasses.
	self deny: c2 hasSubclasses.
	
	self assert: c1 numberOfAllDependencies equals: 1.
	self assert: c2 numberOfAllDependencies equals: 0.
	self assert: c1 numberOfAllIncomingDependencies equals: 0.
	self assert: c2 numberOfAllIncomingDependencies equals: 1.