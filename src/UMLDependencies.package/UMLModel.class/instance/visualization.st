visualizations
visualization
	| b |
	b := RTMondrian new.
	b nodes: self packages forEach: [ :p |
		b nodes: p elements.
		b layout grid.
	].
	b normalizer 
		objects: self classes;
		distinctColorUsing: #role.
	b layout flow.
	^ b