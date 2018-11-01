visualizations
visualizeLayers
	| b hb |
	b := RTMondrian new.
	b interaction 
		item: 'push front' actionElement: [ :e | e trachelShape pushFront. b view signalUpdate ];
		item: 'remove' actionElement: #remove.
	b shape box withBorder; size: [ :l | l allSubElements size ]; withText: #fullLayerName height: [ :l | l allSubElements size sqrt * 5 ] color: Color black.
	b nodes: layers.
	b layout flow.
	b normalizer normalizeColor: #depth using: {Color gray . Color green }.
	b build.
	
	"Add the highlighter"
	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
"		hb fontSize: 30."
		hb view: b view.
		hb filter: [ :obj | obj class == UMLLayer ].
		self roles do: [ :aRole | 
			hb 
				addEntry: [ :layer | layer elements anySatisfy: [ :ee | ee role == aRole ] ] 
				named: aRole roleName
				highlightColor: [ :layer | Color r: (layer elements select: [ :ee | ee role == aRole ]) size / layer numberOfElements g: 0 b: 0 ]
				defaultColor: Color veryDarkGray.
		].
		hb build.
	].
"	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
		hb view: b view.
		hb filter: [ :obj | obj class == UMLLayer ].
		self roles do: [ :r | 
			hb addEntry: [ :layer | layer classes anySatisfy: [ :ee | ee role == r ] ] named: r roleName.
		].
		hb build.
	]."
	
	^ b view