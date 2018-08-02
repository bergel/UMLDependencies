visualizations
visualizationRolesAndElementInteractionDistribution
	| b lb colorMapping |
	b := RTMondrian new.
	b shape box withTextAbove: #roleName.
	b nodes: self roles forEach: [ :r |
		b nodes: r elements.
		b edges connectToAll: #allDependencies.
		b layout force; ifNotConnectedThen: RTGridLayout new.
	].
	b layout flow.
	colorMapping := b normalizer
		objects: self elements; distinctColorUsing: #class.
	b build.
	
	lb := RTLegendBuilder new.
	lb view: b view.
	lb addText: 'Visualize roles and the elements their representation in the code'.
	lb addText: 'Large box = role'.
	lb addText: 'Small box = class|enum|interface|abstract class'.
	lb addText: 'Color of small box = role'.
	colorMapping do: [ :assoc |
		lb addColor: assoc value text: assoc key asString ].
	lb build.

	
	^ b view