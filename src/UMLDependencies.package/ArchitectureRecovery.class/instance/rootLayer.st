accessing
rootLayer
	^ layers detect: [ :l | l hasNoParent ]