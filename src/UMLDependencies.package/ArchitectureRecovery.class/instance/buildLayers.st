instance creation
buildLayers
	| d previousSize |
	d := entries groupedBy: #value.
	layers := d associations
		collect: [ :association | 
			UMLayer new
				layerNumber: association key;
				classNames: association value ].
	layers := layers asOrderedCollection.

	previousSize := 0.
	[ layers size > previousSize ] whileTrue: 
		[ previousSize := layers size. self iterationBuildLayer. ]