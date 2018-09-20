instance creation
visualize
	| b |
	b := RTMondrian new.
	b shape box size: [ :l | l classNames size ]; withText: #layerNumber.
	b nodes: layers.
	b edges connectFrom: #parent.
	b layout tree.
	^ b