accessing
layerName

	"Hardcoded. We will have to work on this"
	
	| l |
	l := #(#('1.' ' Presentation Layer' ) #('1.1. ' 'User Interface' ) #('1.2. ' 'User Interface Logic' ) #('2.' ' Service Layer' ) #('2.1. ' 'Content Provider' ) #('2.2. ' 'Intent Provider' ) #('3.' ' Business Layer' ) #('3.1. ' 'Business Facade' ) #('3.2. ' 'Business Work Flow' ) #('3.2.1. S' 'cheduling' ) #('3.2.2. T' 'alking with mail providers' ) #('3.2.3. M' 'IME encoder | decoder' ) #('3.2.4. O' 'ther' ) #('3.3. ' 'Business Component' ) #('3.4. ' 'Business Entity Component' ) #('4.' ' Data Access Layer' ) #('4.1. ' 'DAO Interface' ) #('4.2. ' 'DAO Implementation' ) #('5.' ' Cross-cutting' ) ).
	^ (l detect: [ :arr | arr first = layerNumber ] ifNone: [ ^ ' ' ]) second