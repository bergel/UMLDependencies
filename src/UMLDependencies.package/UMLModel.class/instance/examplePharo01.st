public - examples
examplePharo01
	<script: 'self new examplePharo01 inspect'>
	| m |
	m := UMLModel new.
	m importPackageName: 'Roassal2'.
	m importPackageName: 'Trachel'.
	m importPackageName: 'Roassal2-Spec'.
	m resolveDependencies.
	^ m