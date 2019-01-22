tests
testoutterClassNameFor
	| r |
	r := m outterClassNameFor: 'src.main.java.com.fsck.k9.mail.CertificateValidationException.Reason'.
	self assert: r equals: 'CertificateValidationException'.