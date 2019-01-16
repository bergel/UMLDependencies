visualizations - obsolete
gtInspectorArchAsTreeMapIn: composite
	"<gtInspectorPresentationOrder: -10>"
	composite roassal2
		title: 'ArchAsTreeMap';
		initializeView: [ arch ifNotNil: [ arch visualizeTree ] ]