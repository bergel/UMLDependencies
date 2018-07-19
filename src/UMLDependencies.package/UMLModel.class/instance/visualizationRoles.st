visualizations
visualizationRoles
	| b |
	b := RTMondrian new.
	b shape box withTextAbove: #roleName.
	b nodes: self roles forEach: [ :r |
		b nodes: r elements.
		b layout grid.
	].
	b layout flow.
	^ b