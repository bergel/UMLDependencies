public - adding
addInnerLayer: aLayer
	(aLayer == self) ifTrue: [ ^ self ].
	(innerLayers includes: aLayer) ifTrue: [ ^ self ].
	innerLayers add: aLayer 