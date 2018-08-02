accessing
dependentPackages
	"Return the list of dependent packages. Return a list of associations"
		^ ((self elements flatCollect: #allDependencies) groupedBy: #package) associations collect: [ :assoc | assoc key -> assoc value size ].
	