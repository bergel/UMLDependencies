public - examples
exampleLarge
	<script: 'self new exampleLarge inspect'>
	| m root |

	root := (IceRepository registry detect: [ :each | each includesPackageNamed: 'UMLDependencies' ] ifNone: [ self error: 'Please add a clone of this project to Iceberg to access to the resources' ]) location.

	m := UMLModel new.
	"m projectRootFilename: (Smalltalk imageDirectory / 'k9mail_src') fullName."
	m projectRootFilename: root / 'data' / 'k9mail_src'.
	m importFrom: (root / 'data' / 'class_reference_20181108.csv') contents.
	m importDependenciesFrom: (root / 'data' / 'dependency_matrix_20181108.csv') contents.
	m importArchitecture: (root / 'data' / 'k9_layered_architecture_class.csv').
	m importMethodAndAttributesFrom: (root / 'data' / 'sample_attribute_method_K9mail.csv') contents.
	
	^ m