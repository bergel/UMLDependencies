visualizations
visualizationPackageInteraction2WithLayers: layers

	| b lb colorMapping packagesElements |
	b := RTMondrian new.
	b view: self buildView.
	
"	b interaction 
		highlightEdges: [ :r | r dependentPackages collect: #key ].
"	
	
	packagesElements := RTGroup new.
	
	b shape box withBorder; color: Color transparent.
	b nodes: layers forEach: [ :somePackages |
		| t |
		b shape box withTextAbove: #mostRelevantPackageName.
		t := 
		b nodes: somePackages  forEach: [ :p |
			b interaction showEdge connectToAll: #allDependencies; colorElement: [ :edge | edge to trachelShape color ].
			b shape box 
				width: #numberOfAllDependencies;
				height: #numberOfAllIncomingDependencies.

			b nodes: (p elements sortedAs: [ :el | el role roleName ]).
			b shape line color: Color gray trans.
			b edges connectToAll: #allDependencies.
			b layout force; ifNotConnectedThen: RTGridLayout new.
		].
		b layout horizontalLine.
		packagesElements addAll: t.
	].


	packagesElements @ (RTHighlightable new 
								shouldHighlightEdges; 
								edgeColor: Color red; 
								getConnectedEdgesBlock: [ :anEl | 
									anEl connectedEdges
									]).

	colorMapping := b normalizer 
		objects: self elements;
		distinctColorUsing: #role colorPalette: (RTColorPalette diverging colors: 7 scheme: 'Spectral').

	b shape lineDualWidth 
		withShorterDistanceAttachPoint;
		color: Color blue trans;
		widthFrom: [ :anEdge | anEdge from model numberOfRelationsWith: anEdge to model ];
		widthTo: [ :anEdge | anEdge to model numberOfRelationsWith: anEdge from model ].
	b edges
		moveBehind;
		noRepetition;
		noBidirectional;
		elements: packagesElements;
		connectToAll: [ :r | r dependentPackages collect: #key ].
	b layout verticalLine.

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

"	b view @ RTThresholdFilter."
	^ b view
