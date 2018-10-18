visualizations
visualizationTreeMap
	| b shape label hb |
	b := RTTreeMapBuilder new.
	b extent: 1800@1800.
	b shape fillColor: [ :pak | Color random ].
	
	b from: packages  using: [#()].
	b depthShadedBorder.
	b weight: #numberOfElements.

shape := RTLabel new text: #mostRelevantPackageName.
b build.
b elements 
    select: [ :el | (el attributes at: #elements) isEmpty ]
    thenDo: [ :el | 
        label := shape elementOn: el model.
        label trachelShape scaleBy: el extent y / label extent y / 8.
        
        (RTConstraint movable: label fix: el)
                inner; stick.
            b view add: label ].

"Add the highlighter"
	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
		hb fontSize: 40.
		hb view: b view.
		hb filter: [ :obj | obj class == UMLPackage ].
		self roles do: [ :r | 
			hb addEntry: [ :layer | layer elements anySatisfy: [ :ee | ee role == r ] ] named: r roleName.
		].
		hb build.
	].
^ b view