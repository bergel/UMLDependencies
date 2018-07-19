visualizations
visualizationDependencies
	| v this deps eb |
	v := RTView new.
	this := RTLabel elementOn: self.
	v add: this.
	deps := RTLabel elementsOn: self allDependencies.
	v addAll: deps.
	RTVerticalLineLayout on: deps.
	TRConstraint move: deps onTheRightOf: this.
	deps translateBy: 80 @ 0.
	
	eb := RTEdgeBuilder new.
	eb view: v.
	eb shape line; shortestDistanceAttachPointWithJump; color: Color blue trans.
	eb objects: (Array with: self), (self dependencies).
	eb moveBehind.
	eb connectToAll: #dependencies.
	
	eb := RTEdgeBuilder new.
	eb view: v.
	eb shape line; shortestDistanceAttachPointWithJump; color: Color red trans.
	eb objects: (Array with: self), (self subclasses).
	eb moveBehind.
	eb connectToAll: #subclasses.
	
	eb := RTEdgeBuilder new.
	eb view: v.
	eb shape line; shortestDistanceAttachPointWithJump; color: Color green trans.
	eb objects: (Array with: self), (self subclasses).
	eb moveBehind.
	eb connectToAll: #implementations.
	
	^ v