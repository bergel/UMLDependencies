tests
testoutterClassNameFor2
	| r |
	r := m outterClassNameFor: 'src.main.java.com.fsck.k9.mail.Reason'.
	self assert: r isNil