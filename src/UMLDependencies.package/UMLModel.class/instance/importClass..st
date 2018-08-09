public - importing Pharo
importClass: aClassOrTrait
	"Import a pharo class in the model"
	| element packageName package genericClass |
	genericClass := aClassOrTrait isTrait ifTrue: [ UMLPharoTrait ] ifFalse: [ UMLPharoClass ].
	element := genericClass new.
	
	element name: aClassOrTrait name.
	aClassOrTrait isClass ifTrue: [ 
		element superclassName: aClassOrTrait superclass name ].
	elements add: element.
	
	element role: (self roleFor: 'PharoClass').
	element dependentClassesNames addAll: ((aClassOrTrait dependentClasses) collect: #name).
	
	packageName := aClassOrTrait package packageName.
	package := self packageFor: packageName.
	
	package add: element.
	element package: package