visualizations
visualizeLabeledTree
| b label shape hb |
b := RTTreeMapBuilder new.
b
    extent: 800@800;
    leafWeight: [ :l | l numberOfClasses + 1 ];
	 leafColor: [ :aLayer | Color gray: (1 - (aLayer depth / 5) ) ];
    explore: self rootLayer
        using: #innerLayers;
    depthShadedBorder.

shape := RTLabel new text: #layerNumber.
b build.
b elements 
    select: [ :el | (el attributes at: #elements) isEmpty ]
    thenDo: [ :el | 
        label := shape elementOn: el model.
        label trachelShape scaleBy: el extent y / label extent y / 4.
        
        (RTConstraint movable: label fix: el)
                inner; stick.
            b view add: label ].

"Add the highlighter"
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