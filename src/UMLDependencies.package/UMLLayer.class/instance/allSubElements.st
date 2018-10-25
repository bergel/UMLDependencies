accessing
allSubElements
	| r |
	r := OrderedCollection new.
	self allSubElements: r.
	^ r