visualizations
visualizationPackageInteraction2

	| b lb colorMapping packagesElements someElements |
	b := RTMondrian new.
	b view: self buildView.
	b shape box withTextAbove: #mostRelevantPackageName.
	
"	b interaction 
		highlightEdges: [ :r | r dependentPackages collect: #key ].
"	
	packagesElements :=
	b nodes: self packages forEach: [ :p |
		b interaction showEdge connectToAll: #allDependencies; colorElement: [ :edge | edge to trachelShape color ].
		b shape box
			width: #numberOfAllDependencies;
			height: #numberOfAllIncomingDependencies.

		"b nodes: (p elements sortedAs: [ :el | el role roleName ])."
		someElements := b nodes: (p elements sortedAs: [ :el | el role roleName ]).
		someElements @ (RTLabeled new center; fontSize: 1; color: Color black ; setAsShouldBeInTheFront).
		b shape line color: Color gray trans.
		b edges connectToAll: #allDependencies.
		b layout force; ifNotConnectedThen: RTGridLayout new.
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
	b layout circle.

	b shape lineDualWidth 
		withShorterDistanceAttachPoint;
		color: Color blue trans;
		widthFrom: [ :anEdge | anEdge from model numberOfRelationsWith: anEdge to model ];
		widthTo: [ :anEdge | anEdge to model numberOfRelationsWith: anEdge from model ].
	b edges
		moveBehind;
		noRepetition;
		noBidirectional;
		connectToAll: [ :r | r dependentPackages collect: #key ].
	b layout weightedCircle.

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
