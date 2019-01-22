public - importing csv
importMethodAndAttributesFrom: aContentAsString

"
- Column 0:  class_id (same as in the file class_reference_20181108.csv) 
- Column 1:  class_name (same as in the file class_reference_20181108.csv)
- Column 2:  attribute_method_signature - this column contains either attribute or method signatures such as: 'public int age' or 'public int calculateXY(int x, double y)'
- Column 3:  type (0 if this is method; 1 if this is attribute)
"
	aContentAsString lines do: [ :l |
		self halt.
	]
	 