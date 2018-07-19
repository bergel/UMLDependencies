public - importing
importDependenciesLineFrom: aLine rowIndex: rowIndex
	" 
Purpose: To visualize class role/responsibility and relationship between the classes of the K9-mail app.
- File dependency-matrix.csv presents the dependency matrix of the system. Each row represents one class and has the same index as in the file class_reference.csv. The value of a cell (X, Y) – X is row index, Y is column index falls in one of the following:
- If 0: no relationship between class X and Y
- if 1: class X depends on class Y
- if 2: class X is a sub class of class Y
- if 3: class X implements class Y
"
	| values |
	values := (aLine substrings: ',') collect: #asInteger.
	
	"Make sure that the matrix is not desynchronized with the elements"
	self assert: [ values size = self numberOfElements ].

	values doWithIndex: [ :aValue :columnIndex |
		aValue = 1 ifTrue: [ (elements at: rowIndex) dependsOn: (elements at: columnIndex) ].
		aValue = 2 ifTrue: [ (elements at: columnIndex ) subclassOf: (elements at: rowIndex) ].
		aValue = 3 ifTrue: [ (elements at: rowIndex) implements: (elements at: columnIndex) ].
	].
	