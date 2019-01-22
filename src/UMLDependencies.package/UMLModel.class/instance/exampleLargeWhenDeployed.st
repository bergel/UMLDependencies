public - examples
exampleLargeWhenDeployed
	<script: 'self new exampleLargeWhenDeployed inspect'>
	| m root |

	"root := (IceRepository registry detect: [ :each | each includesPackageNamed: 'UMLDependencies' ] ifNone: [ self error: 'Please add a clone of this project to Iceberg to access to the resources' ]) location."
	root := Smalltalk imageDirectory / 'data'.

	m := UMLModel new.
	m projectRootFilename: (root / 'k9mail_src') fullName.
	m importFrom: (root / 'class_reference_20181108.csv') contents.
	m importDependenciesFrom: (root / 'dependency_matrix_20181108.csv') contents.
	m importArchitecture: (root / 'k9_layered_architecture_class.csv').
	m importMethodAndAttributesFrom: (root / 'sample_attribute_method_K9mail.csv') contents.
	
	^ m