public - dependencies
subclassOf: anElement
	"I am a subclass of anElement. I represent and outoing dependency"
	subclasses add: anElement.
	anElement superclassOf: self