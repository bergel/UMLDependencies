public - layer building
buildLayers
	| numberOfLayers g layers layersAsDict mappingPackageToLayer energy |
	numberOfLayers := 4.
	packages := self packages sortedAs: #packageName.
	g := GAEngine new.
	"g endForMaxNumberOfGeneration: 50."
	g endIfNoImprovementFor: 3.
	g selection: GATournamentSelection new.
	g populationSize: 3000.
	g mutationOperator: GASwapGeneMutationOperation new.
	g mutationRate: 0.1.
	g numberOfGenes: packages size.
	g createGeneBlock: [ :rand :index | (1 to: numberOfLayers) atRandom: rand ].
	g
		fitnessBlock: [ :genes | 
			mappingPackageToLayer := Dictionary new.
			layersAsDict := Dictionary new.
			packages
				with: genes
				do: [ :pak :l | 
					(layersAsDict includesKey: l)
						ifFalse: [ layersAsDict at: l put: OrderedCollection new ].
					(layersAsDict at: l) add: pak.
					mappingPackageToLayer at: pak put: l ].
			layers := (layersAsDict associations sortedAs: #key) collect: #value.
			energy := 0.
			layers
				withIndexDo: [ :packagesInTheCurrentLayer :currentLayerIndex | 
					packagesInTheCurrentLayer
						do: [ :aPackage | 
							aPackage dependentPackagesWithoutWeight
								do: [ :dependentPackage | 
									"In the same package"
									(mappingPackageToLayer at: dependentPackage) = currentLayerIndex ifTrue: [ energy := energy - 0.2 ].
									
									"Direct Downward dependency"
									"(mappingPackageToLayer at: dependentPackage) = (currentLayerIndex + 1) ifTrue: [ energy := energy + 1.2 ]."
									
									"Downward dependency"
									(mappingPackageToLayer at: dependentPackage) > (currentLayerIndex) ifTrue: [ energy := energy + 1 ].
									
									"Upward dependency"
									(mappingPackageToLayer at: dependentPackage) < currentLayerIndex ifTrue: [ energy := energy - 1 ] ] ] ]. 
			energy ].
	g run.
	self halt.
	^ self visualizationPackageInteraction2WithLayers: (self builderLayerFromGene: g bestGenes)