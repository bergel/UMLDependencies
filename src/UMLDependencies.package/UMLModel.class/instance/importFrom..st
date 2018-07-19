public - importing
importFrom: string
	string lines do: [ :l | self importLine: l ]