accessing
parent: aParentLayer

	"No circular referency"
	aParentLayer == self ifTrue: [ ^ self ].
	
	parent := aParentLayer 