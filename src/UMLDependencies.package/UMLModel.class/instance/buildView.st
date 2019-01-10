visualizations
buildView
	| filter |
	filter := RTFilterInView new.
	filter spawnBlock: [ :someElements | 
		| umlModel |
		umlModel := UMLModel new.
		
		umlModel elements addAll: ((someElements reject: #hasNestedElements) collect: #model).
		umlModel packages addAll: ((someElements select: #hasNestedElements) collect: #model).
		umlModel roles addAll: (umlModel elements collect: #role as: Set) asArray.
		umlModel projectRootFilename: self projectRootFilename.
		umlModel
	].
	
	^ RTView new @ RTDraggableView @ RTHighlightElementsInView
		@ filter
		@ (RTShowLabelWhenZooming new
				conditionBlock: [ :aTrachelShape | aTrachelShape element isNil  ];
				scaleThreshold: 5)		