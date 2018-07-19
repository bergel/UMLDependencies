public - examples
exampleSmall
	<script: 'self new exampleSmall inspect'>
	| m |
	m := UMLModel new.
	m importFrom: UMLModelTest new exampleCode01.
	^ m