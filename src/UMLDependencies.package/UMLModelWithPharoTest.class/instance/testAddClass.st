tests
testAddClass

	m importClass: self class.
	self assert: m numberOfClasses equals: 1.
	self assert: m classes anyOne name equals: 'UMLModelWithPharoTest'.

	self assert: m numberOfPackages equals: 1.
	self assert: m packages anyOne packageName equals: self class package packageName