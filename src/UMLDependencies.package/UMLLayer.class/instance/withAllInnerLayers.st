accessing
withAllInnerLayers
	| result |
	result := OrderedCollection new.
	self withAllInnerLayers: result.
	^ result