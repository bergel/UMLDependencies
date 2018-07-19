visualizations
visualizationHierarchies
	| b |
	b := RTMondrian new.
	b nodes: self elements.
	b edges connectToAll: #subclasses.
	b layout tree; ifNotConnectedThen: RTGridLayout new.
	^ b