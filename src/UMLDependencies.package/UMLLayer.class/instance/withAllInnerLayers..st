accessing
withAllInnerLayers: result
	(result includes: self) ifTrue: [ self halt ].
	result add: self.
	self innerLayers do: [ :l | l withAllInnerLayers: result ]