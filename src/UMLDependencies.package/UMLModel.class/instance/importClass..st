public - importing Pharo
importClass: aClass
	"Import a pharo class in the model"
	| element packageName package genericClass |
	genericClass := aClass isAbstract ifTrue: [ self classFor: 'abstract class' ] ifFalse: [ self classFor: 'class' ].
	element := genericClass new.
	element name: aClass name.
	elements add: element.
	
	packageName := aClass package packageName.
	package := self packageFor: packageName.
	
	package add: element.
	element package: package