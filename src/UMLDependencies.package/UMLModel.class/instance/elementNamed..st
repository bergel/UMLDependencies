accessing
elementNamed: aName
	"Return an element named"
	^ self elements detect: [ :cls | cls name = aName ] ifNone: [ nil ]