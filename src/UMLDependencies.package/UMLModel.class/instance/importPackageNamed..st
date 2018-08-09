public - importing Pharo
importPackageNamed: aPackageName
	self importPackage: (RPackageOrganizer default packageNamed: aPackageName)