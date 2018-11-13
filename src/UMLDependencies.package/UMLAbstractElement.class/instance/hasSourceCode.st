visualizations
hasSourceCode
	"Return true if the source code exist and is available"
	| mfr |
	model projectRootFilename  ifNil: [ ^ false ].
	mfr := model projectRootFilename asFileReference.
	mfr exists ifFalse: [ ^ false ].
	
	self filename ifNil: [ ^ self ].
	^ (mfr / self filename) exists