printing
printOn: str
	str nextPutAll: '<E '.
	str nextPutAll: self name.
	str nextPut: $>