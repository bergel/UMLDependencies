visualizations
buildView
	^ RTView new @ RTDraggableView @ RTHighlightElementsInView
		@ RTFilterInView
		"@ (RTShowLabelWhenZooming new
				conditionBlock: [ :aModel | aModel isKindOf: UMLAbstractElement ])"