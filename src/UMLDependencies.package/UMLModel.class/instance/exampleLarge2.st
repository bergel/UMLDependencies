public - examples
exampleLarge2
	<script: 'self new exampleLarge2 inspect'>
	| m |
	m := UMLModel new.
	m importFrom: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/NewData/poi_317-predicted-no-feature_original.csv' asFileReference contents.
	m importDependenciesFrom: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/NewData/dependency_matrix_poi_317.csv' asFileReference contents.
	^ m