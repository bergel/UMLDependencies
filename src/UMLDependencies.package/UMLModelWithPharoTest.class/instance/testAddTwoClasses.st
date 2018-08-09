tests
testAddTwoClasses

	m importClass: self class.
	m importClass: self class superclass.
	self assert: m numberOfClasses equals: 2.
	self assert: m classes first name equals: 'UMLModelWithPharoTest'.
	self assert: m classes second name equals: 'UMLTest'.

	self assert: m numberOfPackages equals: 1.
	self assert: m packages anyOne packageName equals: self class package packageName.
	
	self assert: m classes first superclass isNil.
	self assert: m classes second superclass isNil.
	m resolveDependencies.
	self deny: m classes first superclass isNil.
	self assert: m classes first superclass equals: m classes second.
	self assert: m classes second superclass isNil.
