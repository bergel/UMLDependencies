accessing
elementFullyNamed: aName
	"Return an element named"
	^ self elements detect: [ :cls | cls fullyQualifiedName = aName ] ifNone: [ nil ]