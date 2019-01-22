initialization
initialize
	super initialize.
	isNested := false.
	
	"outgoing links"
	dependencies := OrderedCollection new.
	subclasses := OrderedCollection new.
	implementations := OrderedCollection new.
	
	"incoming links"
	superclasses := OrderedCollection new.
	implementedBy := OrderedCollection new.
	incomingDependencies := OrderedCollection new.
	
	"attributes and methods"
	attributes := OrderedCollection new.
	methods := OrderedCollection new.