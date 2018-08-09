public - importing csv
importFrom: string
	string lines do: [ :l | self importLine: l ]