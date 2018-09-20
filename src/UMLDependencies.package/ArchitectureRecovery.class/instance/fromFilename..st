instance creation
fromFilename: aCSVFilename

	"
	self new fromFilename: '/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/k9_layered_architecture_class.csv'
	"
	<script: 'self new fromFilename: ''/Users/alexandrebergel/Dropbox/GitRepos/UMLDependencies/data/k9_layered_architecture_class.csv'''>
	
	^ self fromFileReference: aCSVFilename asFileReference