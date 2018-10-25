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
	
	"Add the highlighter"
	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
		hb view: b view.
		hb filter: [ :obj | obj class == UMLLayer ].
		self roles do: [ :aRole | 
			"hb addEntry: [ :layer | layer classes anySatisfy: [ :ee | ee role == r ] ] named: r roleName."
			hb 
				addEntry: [ :layer | layer elements anySatisfy: [ :ee | ee role == aRole ] ] 
				named: aRole roleName
				highlightColor: [ :layer | Color r: (layer elements select: [ :ee | ee role == aRole ]) size / layer numberOfElements g: 0 b: 0 ]
				defaultColor: Color veryDarkGray.
		].
		hb build.
	].
	
	^ b view