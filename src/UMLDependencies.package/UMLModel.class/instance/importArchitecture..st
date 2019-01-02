public - importing csv
importArchitecture: aCSVFilenameFileReference

	arch := ArchitectureRecovery new fromFileReference: aCSVFilenameFileReference.
	arch roles: roles.
	arch layers do: [ :l |
		| someClass |
		someClass := l classNames collect: [ :clsNameAndLayer | self elements detect: [ :el | el name = clsNameAndLayer key ] ifNone: [self halt. nil ] ].
		someClass := someClass copyWithout: nil.
		l classes: someClass
	].