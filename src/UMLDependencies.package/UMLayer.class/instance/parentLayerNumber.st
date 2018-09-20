accessing
parentLayerNumber
	layerNumber isEmpty ifTrue: [ ^ '0' ].
	^ '' join: ((layerNumber substrings: '.') allButLast collect: [ :n | n, '.' ])
	