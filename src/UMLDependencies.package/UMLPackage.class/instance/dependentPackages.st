accessing
dependentPackages
	"Return the list of dependent packages"
		^ ((self elements flatCollect: #allDependencies) groupedBy: #package) associations collect: [ :assoc | assoc key -> assoc value size ].
	