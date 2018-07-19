private - utility
extractPackageNameFromArray: fullyQualifiedClass
	"the argument may be 'com.fsck.k9.mail.Address.Address'.
	It should return 'com.fsck.k9.mail'"
	| names i |
	names := fullyQualifiedClass substrings: '.'.
	i := names detectIndex: [ :aName | aName first isUppercase ].
	^ $. join: (names first: i - 1)