visualizations
visualizationPackageInteraction
	| b |
	b := RTMondrian new.
	b shape label
		text: #packageName;
		color: Color veryDarkGray.
	b nodes: self packages.
"	b shape line color: Color blue trans.
	b edges
		moveBehind;
		connectToAll: [ :r | r dependentPackages collect: #key ].
	b normalizer normalizeLineWidth: [ :from :to | from numberOfRelationsWith: to ]."

	b shape lineDualWidth 
		withShorterDistanceAttachPoint;
		color: Color blue trans;
		widthFrom: [ :anEdge | anEdge from model numberOfRelationsWith: anEdge to model ];
		widthTo: [ :anEdge | anEdge to model numberOfRelationsWith: anEdge from model ].
	b edges
		moveBehind;
		noBidirectional; noRepetition;
		connectToAll: [ :r | r dependentPackages collect: #key ].
	b layout circle.
	^ b