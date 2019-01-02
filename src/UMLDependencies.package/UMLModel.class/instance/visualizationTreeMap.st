visualizations
visualizationTreeMap
	| b shape label hb |
	b := RTTreeMapBuilder new.
	b view: self buildView.
	b extent: 1000@1000.
	b shape fillColor: [ :pak | Color random ].
	
	b from: packages  using: [#()].
	b depthShadedBorder.
	b weight: #numberOfElements.

shape := RTLabel new "text: #mostRelevantPackageName".
b build.
b elements 
    select: [ :el | (el attributes at: #elements) isEmpty ]
    thenDo: [ :el | 
        label := shape elementOn: el model mostRelevantPackageName.
        label trachelShape scaleBy: el extent y / label extent y / 8.
        
        (RTConstraint movable: label fix: el)
                inner; stick.
            b view add: label ].

"Add the highlighter"
	roles ifNotNil: [
		hb := RTHighlighterBuilder new.
		hb fontSize: 30.
		hb view: b view.
		hb filter: [ :obj | obj class == UMLPackage ].
		self roles do: [ :aRole | 
			hb 
				addEntry: [ :package | package elements anySatisfy: [ :ee | ee role == aRole ] ] 
				named: aRole roleName
				highlightColor: [ :aPack | Color r: (aPack elements select: [ :ee | ee role == aRole ]) size / aPack numberOfElements g: 0 b: 0 ]
				defaultColor: Color veryDarkGray.
		].
		hb build.
	].
^ b view