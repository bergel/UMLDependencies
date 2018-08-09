public - examples
examplePharo02
	<script: 'self new examplePharo02 inspect'>
	| m |
	m := UMLModel new.
	m importPackagesNamedMatching: 'Metacello*'.
	m resolveDependencies.
	^ m