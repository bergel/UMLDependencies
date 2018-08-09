public - importing Pharo
importPackage: aRPackage
	aRPackage definedClasses do: [ :cls | self importClass: cls ]