visualization
visualizationPackage
	| g roles d |

	roles := (self elements groupedBy: #role) associations.
	
	g := RTGrapher new.
	d := RTData new.
"	d barShape."
	d barChartWithBarTitle: [ :assoc | assoc key roleName, ' (', ((assoc value size / self elements size) asFloat * 100 round: 2) asString ,'%)' ].
	d points: roles.
	d y: [ :assoc | assoc value size ].
	g add: d.
	g axisX noTitle; noTick.
	g axisY title: '# elements'.
	^ g
	