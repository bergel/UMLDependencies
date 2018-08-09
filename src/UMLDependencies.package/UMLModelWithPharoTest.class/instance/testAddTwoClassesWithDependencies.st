tests
testAddTwoClassesWithDependencies

	| firstClass secondClass |
	m importClass: UMLRole.
	m importClass: OrderedCollection.
	m resolveDependencies.
	self assert: m numberOfClasses equals: 2.
	self assert: m numberOfPackages equals: 2.

	firstClass := m classes first.
	secondClass := m classes second.
	self assert: (firstClass dependencies includes: secondClass)