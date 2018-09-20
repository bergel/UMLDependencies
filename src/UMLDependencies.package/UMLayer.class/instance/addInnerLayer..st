public - adding
addInnerLayer: aLayer
	(innerLayers includes: aLayer) ifTrue: [ ^ self ].
	innerLayers add: aLayer 