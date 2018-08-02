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
	
	self assert: aClass numberOfAllDependencies equals: 1.
	self assert: anInterface numberOfAllDependencies equals: 0.
	self assert: aClass numberOfAllIncomingDependencies equals: 0.
	self assert: anInterface numberOfAllIncomingDependencies equals: 1.
