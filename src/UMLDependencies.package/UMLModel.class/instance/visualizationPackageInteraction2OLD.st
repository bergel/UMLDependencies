visualizations
visualizationPackageInteraction2OLD

	| b lb colorMapping |
	b := RTMondrian new.
	b shape box withTextAbove: #mostRelevantPackageName.
	
	b interaction 
		"highlightNeighbors: [ :r | r dependentPackages collect: #key ] color: Color red;"
		highlightEdges: [ :r | r dependentPackages collect: #key ].
	
	b nodes: self packages forEach: [ :p |
		b interaction showEdge connectToAll: #allDependencies; color: Color green.
		b shape box 
			width: #numberOfAllDependencies;
			height: #numberOfAllIncomingDependencies.

		b nodes: p elements.
		b shape line color: Color gray trans.
		b edges connectToAll: #allDependencies.
		b layout force; ifNotConnectedThen: RTGridLayout new.
	].

	colorMapping := b normalizer 
		objects: self elements;
		distinctColorUsing: #role colorPalette: (RTColorPalette diverging colors: 7 scheme: 'Spectral').
	b layout circle.

	b shape line color: Color blue trans.
	b edges
		moveBehind;
		connectToAll: [ :r | r dependentPackages collect: #key ].
	b layout circle.
	b normalizer 
		objects: self packages;
		normalizeLineWidth: [ :from :to | from numberOfRelationsWith: to ].
	
	b build.
	
	
	lb := RTLegendBuilder new.
	lb onDemand.
	lb view: b view.
	lb addText: 'Visualize packages and their inner dependencies'.
	lb addText: 'Encapsulating box = package'.
	lb addText: 'Inner box = class'.
	lb addText: 'Color of small box = role'.
	lb addText: 'Height inner box = #incoming dependencies'.
	lb addText: 'Width inner box = #outgoing dependencies'.
	lb addText: 'Edges = dependencies scoped in the package'.
	colorMapping do: [ :assoc |
		lb addColor: assoc value text: assoc key asString ].
	lb build.

	
	^ b view
