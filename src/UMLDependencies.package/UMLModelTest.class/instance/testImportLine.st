tests
testImportLine
	| theClass thePackage |
	m importLine: 'com.fsck.k9.mail.Address,Address,Service Provider,class'.
	self assert: m numberOfPackages equals: 1.
	self assert: m numberOfClasses equals: 1.
	self assert: m numberOfElements equals: 1.
	self assert: m classes size equals: 1.
	
	theClass := m classes anyOne.
	thePackage := m packages anyOne.
	self assert: theClass name equals: 'Address'.
	self assert: theClass package equals: thePackage.

	self assert: thePackage packageName equals: 'com.fsck.k9.mail'.
	self assert: thePackage numberOfElements equals: 1.
	self assert: thePackage elements size equals: 1.
	self assert: thePackage elements anyOne equals: theClass.
	
	