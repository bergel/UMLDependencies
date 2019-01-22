accessing
elementFullyNamed: aName
	"Return an element named"
	^ self elements detect: [ :cls | cls fullName = aName ] ifNone: [ nil ]