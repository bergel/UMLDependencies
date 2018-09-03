public - layer building
builderLayerFromGene: genes
	"genes is a set of numbers"
	| layers layersAsDict |
	layersAsDict := Dictionary new.
	packages with: genes do: [ :pak :l | 
		(layersAsDict includesKey: l) ifFalse: [ layersAsDict at: l put: OrderedCollection new ]. 
		(layersAsDict at: l) add: pak.
	]. 

	layers := (layersAsDict associations sortedAs: #key) collect: #value.
	^ layers