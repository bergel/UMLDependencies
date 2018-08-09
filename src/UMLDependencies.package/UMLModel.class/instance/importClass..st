public - importing Pharo
importClass: aClass
	"Import a pharo class in the model"
	| element packageName package |
	element := UMLPharoClass new.
	element name: aClass name.
	element superclassName: aClass superclass name.
	elements add: element.
	
	packageName := aClass package packageName.
	package := self packageFor: packageName.
	
	package add: element.
	element package: package