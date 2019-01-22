visualizations-obsolete
gtInspectorArchIn: composite
	"<gtInspectorPresentationOrder: -10>"
	composite roassal2
		title: 'Arch';
		initializeView: [ arch ifNotNil: [ arch visualizeCircularTreeMap ] ]