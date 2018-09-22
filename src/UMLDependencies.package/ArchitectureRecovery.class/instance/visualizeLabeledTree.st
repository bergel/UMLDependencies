visualizations
visualizeLabeledTree
| b label shape |
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
^ b view