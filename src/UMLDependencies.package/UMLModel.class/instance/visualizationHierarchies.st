visualizations
visualizationHierarchies
	| b |
	b := RTMondrian new.
	b view: self buildView.
	b nodes: self elements.
	b edges connectToAll: #subclasses.
	b layout tree; ifNotConnectedThen: RTGridLayout new.
	^ b