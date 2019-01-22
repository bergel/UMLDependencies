visualizations
sourceCode
	"Return the source code of the element. Return an error message in case of not found"
	| fr |
	self hasSourceCode ifFalse: [ ^ self defaultSourceCodeWhenNotFound ].
	fr := model projectRootFilename asFileReference / self filename.
	^ fr contents