instance creation
iterationBuildLayer
	| doesExist parentLayer |
	layers
		do: [ :l | 
			doesExist := layers
				anySatisfy: [ :ll | ll layerNumber = l parentLayerNumber ].
			doesExist
				ifTrue: [ parentLayer := layers
						detect: [ :ll | ll layerNumber = l parentLayerNumber ] ]
				ifFalse: [ parentLayer := UMLayer new layerNumber: l parentLayerNumber.
					layers add: parentLayer ].
			l parent: parentLayer.
			parentLayer addInnerLayer: l ]