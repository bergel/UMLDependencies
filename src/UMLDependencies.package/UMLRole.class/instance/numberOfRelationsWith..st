public - interactions
numberOfRelationsWith: anotherRole
	"Return the number of relation with two roles"
	^ (self interactingRoles detect: [ :assoc | assoc key = anotherRole ]) value