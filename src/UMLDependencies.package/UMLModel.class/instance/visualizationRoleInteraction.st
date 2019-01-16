visualizations - obsolete
visualizationRoleInteraction
	| b |
	b := RTMondrian new.
	b view: self buildView.
	b shape label
		text: #roleName;
		color: Color veryDarkGray.
		
	b interaction highlightEdges: [ :r | r interactingRoles collect: #key ].
	b nodes: self roles.
	
"	b interaction popupText: [ :edge | edge model from numberOfRelationsWith: edge model to ].
	b shape line color: Color blue trans.
	b edges
		moveBehind;
		connectToAll: [ :r | r interactingRoles collect: #key ].
"
	b shape lineDualWidth 
		withShorterDistanceAttachPoint;
		color: Color blue trans;
		widthFrom: [ :anEdge | (anEdge from model numberOfRelationsWith: anEdge to model) / 10 ];
		widthTo: [ :anEdge | (anEdge to model numberOfRelationsWith: anEdge from model) / 10 ].
	b edges
		moveBehind;
		noBidirectional; noRepetition;
		connectToAll: [ :r | r interactingRoles collect: #key ].

	b layout circle.
	b normalizer normalizeLineWidth: [ :from :to | from numberOfRelationsWith: to ].
	^ b