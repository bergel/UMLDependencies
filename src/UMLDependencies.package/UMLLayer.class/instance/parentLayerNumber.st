accessing
parentLayerNumber
	| answer |
	layerNumber isEmpty ifTrue: [ ^ '0' ].
	answer := '' join: ((layerNumber substrings: '.') allButLast collect: [ :n | n, '.' ]).
	answer = '' ifTrue: [ ^ '0' ].
	^ answer