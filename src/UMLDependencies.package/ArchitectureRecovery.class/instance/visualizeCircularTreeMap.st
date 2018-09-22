visualizations
visualizeCircularTreeMap
	| b  |
	b := RTCircularTreeMapBuilder new.
	
	b shape
		color: [ :aLayer | Color gray: (1 - (aLayer depth / 5) ) ].
	b
		baseradius: 200;
		weight: [ :layer | layer numberOfClasses + 1 ].
	b explore: self rootLayer
		using: #innerLayers.
	
	b build.
	^ b view