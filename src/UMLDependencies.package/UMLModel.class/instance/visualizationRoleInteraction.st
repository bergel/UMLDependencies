visualizations
visualizationRoleInteraction
	| b |
	b := RTMondrian new.
	b shape label
		text: #roleName;
		color: Color veryDarkGray.
	b nodes: self roles.
	b shape line color: Color blue trans.
	b edges
		moveBehind;
		connectToAll: [ :r | r interactingRoles collect: #key ].
	b layout circle.
	b normalizer normalizeLineWidth: [ :from :to | from numberOfRelationsWith: to ].
	^ b