visualizations
visualizationPackageInteraction
	| b |
	b := RTMondrian new.
	b shape label
		text: #packageName;
		color: Color veryDarkGray.
	b nodes: self packages.
	b shape line color: Color blue trans.
	b edges
		moveBehind;
		connectToAll: [ :r | r dependentPackages collect: #key ].
	b layout circle.
	b normalizer normalizeLineWidth: [ :from :to | from numberOfRelationsWith: to ].
	^ b