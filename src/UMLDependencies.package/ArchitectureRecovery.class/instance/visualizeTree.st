visualizations
visualizeTree
	| b hb |
	b := RTMondrian new.
	b shape box size: [ :l | l classNames size ]; withText: #layerName.
	b nodes: layers.
	b edges connectFrom: #parent.
	b layout tree.
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