initialization
initialize
	super initialize.
	isNested := false.
	dependencies := OrderedCollection new.
	subclasses := OrderedCollection new.
	implementations := OrderedCollection new.