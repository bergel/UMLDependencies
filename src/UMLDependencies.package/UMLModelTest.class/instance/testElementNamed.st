tests
testElementNamed
	| enum |
	m importFrom: self exampleCode01.
	enum := m elementNamed: 'ConnectionSecurity'.
	self assert: enum isEnum.
	self assert: enum name equals: 'ConnectionSecurity'.
	self assert: enum package packageName equals: 'com.fsck.k9.mail'.
	self assert: enum role roleName equals: 'Information Holder'