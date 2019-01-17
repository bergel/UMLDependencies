visualizations
visualizationRolesInteraction
	| b lb colorMapping theRoleElements |
	b := RTMondrian new.
	b view: self buildView.
	b shape box withTextAbove: #roleName.
"	b nodes: self roles forEach: [ :r |
		b nodes: (r elements sortedAs: [ :e | e class name size ]).
		b layout grid.
	].
	b layout flow.
	colorMapping := b normalizer
		objects: self elements; 
		distinctColorUsing: #class colorPalette: self colorPalette."
		
	theRoleElements := b nodes: self roles forEach: [ :r |
		| someElements |
		"someElements := b nodes: r elements."
		b interaction showEdge connectToAll: #allDependencies; colorElement: [ :edge | edge to trachelShape color ].
		b shape box
			width: #numberOfAllDependencies;
			height: #numberOfAllIncomingDependencies.

		someElements := b nodes: (r elements sortedAs: [ :el | el role roleName ]).
		someElements @ (RTLabeled new center; fontSize: 1; color: Color black ; setAsShouldBeInTheFront).
		
		b edges connectToAll: #allDependencies.
		b layout force; ifNotConnectedThen: RTGridLayout new.
	].
	b layout flow.
	colorMapping := b normalizer
		objects: self elements; 
		distinctColorUsing: #class colorPalette: self colorPalette.

	"LINES"
	b shape lineDualWidth 
		withShorterDistanceAttachPoint;
		color: Color blue trans;
		widthFrom: [ :anEdge | (anEdge from model numberOfRelationsWith: anEdge to model) / 10 ];
		widthTo: [ :anEdge | (anEdge to model numberOfRelationsWith: anEdge from model) / 10 ].
	b edges
		moveBehind;
		noBidirectional; noRepetition;
		connectToAll: [ :r | r interactingRoles collect: #key ].

	b layout circle initialRadius: 300.
	b normalizer 
		elements: theRoleElements;
		normalizeLineWidth: [ :from :to | from numberOfRelationsWith: to ].

	b build.
	
	"INTERACTIONS"
	theRoleElements @ (RTHighlightable new 
								shouldHighlightEdges; 
								edgeColor: Color red; 
								getConnectedEdgesBlock: [ :anEl | 
									anEl connectedEdges
									]).
	
	lb := RTLegendBuilder new.
	"lb right."
	lb onDemand.
	lb view: b view.
	lb addText: 'Visualize roles and the elements their representation in the code'.
	lb addText: 'Large box = role'.
	lb addText: 'Small box = class|enum|interface|abstract class'.
	lb addText: 'Color of small box = role'.
	colorMapping do: [ :assoc |
		lb addColor: assoc value text: assoc key asString ].
	lb build.

	
	^ b view