accessing
allSubElements: r
	r addAll: self elements.
	self innerLayers do: [ :l | l allSubElements: r ]