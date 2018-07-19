visualizations
visualizationGraph
	| b |
	b := RTMondrian new.
	b shape circle color: Color gray trans.
	b interaction highlightNeighbors: #allDependencies color: Color red.
	b nodes: self elements.
	b shape line color: Color blue trans.
	b edges
		moveBehind;
		connectToAll: #dependencies.
	b shape line color: Color red trans.
	b edges
		moveBehind;
		connectToAll: #subclasses.
	b shape line color: Color green trans.
	b edges
		moveBehind;
		connectToAll: #implementations.
	b layout force.
	^ b