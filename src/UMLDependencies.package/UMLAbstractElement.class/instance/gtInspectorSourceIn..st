visualizations
gtInspectorSourceIn: composite
	<gtInspectorPresentationOrder: -10>
	composite text
		title: 'Source';
		format: [ :a | a asText ];
		display: [ self sourceCode ]