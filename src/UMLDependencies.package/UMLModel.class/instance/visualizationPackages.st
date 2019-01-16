visualizations
visualizationPackages
	| b lb colorMapping someElements |
	b := RTMondrian new.
	b view: self buildView.
	b shape box withTextAbove: #mostRelevantPackageName.
	b nodes: self packages forEach: [ :p |
		b interaction highlightEdges: #allDependencies.
		b shape box 
			width: #numberOfAllDependencies;
			height: #numberOfAllIncomingDependencies.

		someElements := b nodes: (p elements sortedAs: [ :el | el role roleName ]).
		someElements @ (RTLabeled new center; fontSize: 1; color: Color black ; setAsShouldBeInTheFront).

		"b nodes: (p elements sortedAs: [:el | el role asString ])."
		
		
		"b shape line color: Color gray trans.
		b edges connectToAll: #allDependencies.
		b layout force; ifNotConnectedThen: RTGridLayout new."
		b layout flow.
	].
	colorMapping := b normalizer 
		objects: self elements;
		distinctColorUsing: #role colorPalette: self colorPalette.
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