tests
testImportLine02
	| theEnum thePackage |
	m importLine: 'com.fsck.k9.mail.CertificateValidationException.Reason,Reason,Information Holder,enum'.
	self assert: m numberOfPackages equals: 1.
	self assert: m numberOfEnums equals: 1.
	self assert: m numberOfClasses equals: 0.
	self assert: m numberOfElements equals: 1.
	self assert: m classes size equals: 0.
	self assert: m enums size equals: 1.
	
	theEnum := m enums anyOne.
	thePackage := m packages anyOne.
	self assert: theEnum name equals: 'Reason'.
	self assert: theEnum package equals: thePackage.

	self assert: thePackage packageName equals: 'com.fsck.k9.mail'.
	self assert: thePackage numberOfElements equals: 1.
	self assert: thePackage elements size equals: 1.
	self assert: thePackage elements anyOne equals: theEnum.
	
	