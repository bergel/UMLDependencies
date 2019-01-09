visualizations
buildView
	^ RTView new @ RTDraggableView @ RTHighlightElementsInView
		@ RTFilterInView
		"@ (RTShowLabelWhenZooming new
				conditionBlock: [ :aTrachelShape | aTrachelShape element isNil  ];
				scaleThreshold: 5)"
				