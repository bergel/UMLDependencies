tests
testImplementationError

	| anInterface aClass |
	anInterface := UMLInterface new.
	aClass := UMLClass new.

	"self should: [ anInterface implements: aClass ] raise: AssertionFailure"