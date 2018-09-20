initialization
initialize
	super initialize.
	classNames := OrderedCollection new.
	layerNumber := -1.
	
	"No parent for now"
	parent := nil.