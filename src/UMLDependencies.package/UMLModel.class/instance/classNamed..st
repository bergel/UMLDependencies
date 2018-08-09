accessing
classNamed: aClassName
	"Return the class having the name aClassName. The argument is a String and return a UMLClass.
	If the class is not found returns nil"
	^ self classes detect: [ :cls | cls name = aClassName ] ifNone: [ nil ]