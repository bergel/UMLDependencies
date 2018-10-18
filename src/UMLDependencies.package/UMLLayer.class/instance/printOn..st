printing
printOn: s
	super printOn: s.
	s nextPut: $<.
	s nextPutAll: (self layerName).
	s nextPutAll: '   '.
	s nextPutAll: layerNumber.
	s nextPut: $>