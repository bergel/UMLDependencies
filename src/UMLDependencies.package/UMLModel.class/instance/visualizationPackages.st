visualizations
visualizationPackages
	| b lb colorMapping |
	b := RTMondrian new.
	b view @ RTZoomableView.
	b shape box withTextAbove: #mostRelevantPackageName.
	b nodes: self packages forEach: [ :p |
		b interaction highlightEdges: #allDependencies.
		b shape box 
			width: #numberOfAllDependencies;
			height: #numberOfAllIncomingDependencies.
		b nodes: p elements.
		b shape line color: Color gray trans.
		b edges connectToAll: #allDependencies.
		b layout force; ifNotConnectedThen: RTGridLayout new.
	].
	colorMapping := b normalizer 
		objects: self classes;
		distinctColorUsing: #role.
	b layout flow.
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