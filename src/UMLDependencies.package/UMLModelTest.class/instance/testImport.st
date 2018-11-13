tests
testImport
	m importFrom: self exampleCode01.
	self assert: m numberOfPackages equals: 2.
	self assert: m numberOfClasses equals: 8.
	self assert: m numberOfEnums equals: 3.
	self assert: m numberOfAbstractClasses equals: 1.
	self assert: m numberOfInterfaces equals: 2.
	self assert: m numberOfElements equals: 14.
	self assert: m classes size equals: 8.
	self assert: m elements last index equals: 14.
	
	"visualizations"
	(m elements first: 10) do: #visualizationDependencies.
	m visualizationPackages.
	
	self deny: m classes first hasSourceCode.
	self assert: m classes first sourceCode equals: m classes first defaultSourceCodeWhenNotFound