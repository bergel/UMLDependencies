printing
printOn: str
	str nextPut: $<.
	str nextPutAll: self packageName.
	str nextPut: $>.