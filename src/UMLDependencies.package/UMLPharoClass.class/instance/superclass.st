accessing
superclass
	"Return the superclass of myself"
	self superclasses ifEmpty: [ ^ nil ].
	^ self superclasses first