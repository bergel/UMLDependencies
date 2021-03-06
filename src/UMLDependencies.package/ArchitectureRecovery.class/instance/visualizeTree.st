visualizations
visualizeTree
	| b hb |
	b := RTMondrian new.
	b shape box size: [ :l | l allSubElements size ]; withText: #layerName.
	b nodes: layers.
	b edges connectFrom: #parent.
	b layout tree.
	b build.
	
	"Add the highlighter"
	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
"		hb fontSize: 30."
		hb view: b view.
		hb filter: [ :obj | obj class == UMLPackage ].
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