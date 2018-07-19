printing
printOn: str
	str nextPut: $<.
	str nextPutAll: self roleName.
	str nextPut: $>.