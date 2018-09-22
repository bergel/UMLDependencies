printing
printOn: s
	super printOn: s.
	s nextPut: $<.
	s nextPutAll: layerNumber.
	s nextPut: $>