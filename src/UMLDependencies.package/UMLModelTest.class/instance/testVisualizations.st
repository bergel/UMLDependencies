tests
testVisualizations
	m importFrom: self exampleCode01.
	m visualizationPackages open delete.
	m visualizationRolesInteraction open delete.
	m visualizationPackageInteraction open delete.
	self assert: m packages notEmpty.
	m packages do: [ :p | p visualizationPackage open delete ]