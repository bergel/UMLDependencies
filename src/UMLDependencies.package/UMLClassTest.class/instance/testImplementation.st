tests
testImplementation

	| anInterface aClass |
	anInterface := UMLInterface new.
	aClass := UMLClass new.

	self assert: anInterface implementations isEmpty.	
	self assert: aClass implementations isEmpty.
	self assert: anInterface implementedBy isEmpty.
	self assert: aClass implementedBy isEmpty.

	aClass implements: anInterface.
	self assert: anInterface implementations isEmpty.	
	self assert: aClass implementations notEmpty.
	self assert: anInterface implementedBy notEmpty.
	self assert: aClass implementedBy isEmpty.
