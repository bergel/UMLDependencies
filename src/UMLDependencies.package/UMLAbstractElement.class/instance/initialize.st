initialization
initialize
	super initialize.
	isNested := false.
	
	"outoing links"
	dependencies := OrderedCollection new.
	subclasses := OrderedCollection new.
	implementations := OrderedCollection new.
	
	"incoming links"
	superclasses := OrderedCollection new.
	implementedBy := OrderedCollection new.
	incomingDependencies := OrderedCollection new.