public - examples
examplePharo01
	<script: 'self new examplePharo01 inspect'>
	| m |
	m := UMLModel new.
	m importPackageNamed: 'Roassal2'.
	m importPackageNamed: 'Trachel'.
	m importPackageNamed: 'Roassal2Spec'.
	m resolveDependencies.
	^ m