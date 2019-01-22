visualizations
gtInspectorMethodsIn: composite
	<gtInspectorPresentationOrder: -10>
	composite table
		title: 'Methods';
		format: [ :m | m signature ];
		display: [ self methods ]