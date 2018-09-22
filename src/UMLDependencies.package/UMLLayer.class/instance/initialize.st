initialization
initialize
	super initialize.
	classNames := OrderedCollection new.
	innerLayers := OrderedCollection new.
	layerNumber := -1.
	
	"No parent for now"
	parent := nil.