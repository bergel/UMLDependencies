instance creation
fromFileReference: aFileReference

	| lines |
	entries := OrderedCollection new.
	lines := aFileReference readStream contents lines allButFirst.
	lines do: [ :l | 
		| ss className layerNumber |
		ss := l substrings: ','.
		className := ss third. 
		layerNumber := ss sixth.
		entries add: className -> layerNumber.
	].

	self buildLayers.
	^ self visualize