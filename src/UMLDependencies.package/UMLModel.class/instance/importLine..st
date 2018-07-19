public - importing
importLine: aLine
	" 
Purpose: To visualize class role/responsibility and relationship between the classes of the K9-mail app.
- File class_reference.csv contains information about class and its role/responsibility. Each row represents a class with no headers. Four columns:
                +  First column: shows the package that the class belongs to.
                + Second column: shows name of the class
                + Third column: shows Roles of the class
                + Fourth column: shows type of the class (could be enumeration, class, abstract class or interface).
- File dependency-matrix.csv presents the dependency matrix of the system. Each row represents one class and has the same index as in the file class_reference.csv. The value of a cell (X, Y) – X is row index, Y is column index falls in one of the following:
- If 0: no relationship between class X and Y
- if 1: class X depends on class Y
- if 2: class X is a sub class of class Y
- if 3: class X implements class Y
"

	| ss element package packageName |
	ss := aLine substrings: ','.
	element := (self classFor: ss last) new.
	element name: ss second.
	elements add: element.
	packageName := self extractPackageNameFromArray: ss first.
	package := self packageFor: packageName.
	package add: element.
	element package: package