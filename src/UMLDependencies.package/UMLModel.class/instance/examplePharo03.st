public - examples
examplePharo03
	<script: 'self new examplePharo03 inspect'>
	| m |
	m := UMLModel new.
	m importPackagesNamedMatching: 'Calypso*'.
	m resolveDependencies.
	^ m