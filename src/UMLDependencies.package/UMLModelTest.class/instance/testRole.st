tests
testRole
	| r |
	r := UMLRole new roleName: 'abstract class'.
	self assert: r printString equals: '<abstract class>'