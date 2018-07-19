visualizations
visualizationPackages
	| b lb colorMapping |
	b := RTMondrian new.
	b shape box withTextAbove: #mostRelevantPackageName.
	b nodes: self packages forEach: [ :p |
		b nodes: p elements.
		b shape line color: Color gray trans.
		b edges connectToAll: #allDependencies.
		b layout tree; ifNotConnectedThen: RTGridLayout new.
	].
	colorMapping := b normalizer 
		objects: self classes;
		distinctColorUsing: #role.
	b layout flow.
	b build.
	
	lb := RTLegendBuilder new.
	lb view: b view.
	lb addText: 'Visualize packages and their inner dependencies'.
	lb addText: 'Large box = package'.
	lb addText: 'Small box = class'.
	lb addText: 'Color of small box = role'.
	lb addText: 'Edges = dependencies scoped in the package'.
	colorMapping do: [ :assoc |
		lb addColor: assoc value text: assoc key asString ].
	lb build.
	
	^ b view