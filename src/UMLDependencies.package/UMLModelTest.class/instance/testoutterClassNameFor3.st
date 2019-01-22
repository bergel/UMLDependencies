tests
testoutterClassNameFor3
	| r |
	r := m outterClassNameFor: 'src.main.java.com.fsck.k9.mail.Reason1.Reason2.Reason3'.
	self assert: r equals: 'Reason2'