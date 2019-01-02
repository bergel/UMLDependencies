visualizations
visualizationGraph
	| b alpha |
	alpha := 0.3.
	b := RTMondrian new.
	b view: self buildView.
	b shape circle color: Color gray.
	b interaction highlightNeighbors: #allDependencies color: Color red.
	b nodes: self elements.
	
	"BLUE = DEPENDENCIES"
	b shape line color: (Color blue alpha: alpha). 
	b edges
		moveBehind;
		connectToAll: #dependencies.
		
	"RED = INHERITANCE"
	b shape line color: (Color red alpha: alpha).
	b edges
		moveBehind;
		connectToAll: #subclasses.
		
	"GREEN = IMPLEMENTATIONS"
	b shape line color: (Color green alpha: alpha).
	b edges
		moveBehind;
		connectToAll: #implementations.

	b layout force; ifNotConnectedThen: RTGridLayout new.
	^ b