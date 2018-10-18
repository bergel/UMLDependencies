visualizations
visualizeCircularTreeMap
	| b  hb |
	b := RTCircularTreeMapBuilder new.
	
	b shape
		color: [ :aLayer | Color gray: (1 - (aLayer depth / 5) ) ].
	b
		baseradius: 200;
		weight: [ :layer | layer numberOfClasses + 1 ].
	b explore: self rootLayer
		using: #innerLayers.
	b build.
	
	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
		hb view: b view.
		hb filter: [ :obj | obj class == UMLLayer ].
		self roles do: [ :r | 
			hb addEntry: [ :layer | layer classes anySatisfy: [ :ee | ee role == r ] ] named: r roleName.
		].
		hb build.
	].
	
	^ b view