accessing
filename: aFilename
	"Set the filename of the element. Could be for example k9mail-library\src\main\java\com\fsck\k9\mail\AuthType.java"
	filename := aFilename replaceAll: $\ with: $/.
	