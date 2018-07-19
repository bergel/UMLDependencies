public - examples
exampleLarge
	<script: 'self new exampleLarge inspect'>
	| m |
	m := UMLModel new.
	m importFrom: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/class_reference.csv' asFileReference contents.
	^ m