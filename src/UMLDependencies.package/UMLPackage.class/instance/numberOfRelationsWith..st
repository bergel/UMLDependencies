accessing
numberOfRelationsWith: anotherPackage
	"Return the number of relation with two roles"
	^ (self dependentPackages detect: [ :assoc | assoc key = anotherPackage ] ifNone: [ ^ 0 ]) value
	