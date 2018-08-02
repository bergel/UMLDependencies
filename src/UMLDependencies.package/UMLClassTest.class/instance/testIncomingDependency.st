tests
testIncomingDependency

	| c1 c2 |
	c1 := UMLClass new.
	c2 := UMLClass new.

	self assert: c1 dependencies isEmpty.	
	self assert: c1 incomingDependencies isEmpty. 
	self assert: c2 dependencies isEmpty.
	self assert: c2 incomingDependencies isEmpty.
	
	self assert: c1 numberOfAllDependencies isZero.
	self assert: c1 numberOfAllIncomingDependencies isZero.

	c1 dependsOn: c2.
	self assert: c1 dependencies size equals: 1.	
	self assert: c1 incomingDependencies isEmpty. 
	self assert: c2 dependencies isEmpty.
	self assert: c2 incomingDependencies size equals: 1.
	
	self assert: c1 numberOfAllDependencies equals: 1.
	self assert: c2 numberOfAllDependencies equals: 0.
	self assert: c1 numberOfAllIncomingDependencies equals: 0.
	self assert: c2 numberOfAllIncomingDependencies equals: 1.