public - importing Pharo
importPackagesNamedMatching: aRegularExpression
	| packagesNames |
	packagesNames := RPackageOrganizer default packageNames select: [ :pName | aRegularExpression match: pName ].
	packagesNames do: [ :pName | self importPackageNamed: pName ]