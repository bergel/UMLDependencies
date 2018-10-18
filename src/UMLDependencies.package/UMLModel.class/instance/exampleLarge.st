public - examples
exampleLarge
	<script: 'self new exampleLarge inspect'>
	| m |
	m := UMLModel new.
	m importFrom: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/new_version_class_reference_20180726.csv' asFileReference contents.
	m importDependenciesFrom: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/new_version_dependency_matrix_20180726.csv' asFileReference contents.
	m importArchitecture: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/k9_layered_architecture_class.csv'.
	^ m