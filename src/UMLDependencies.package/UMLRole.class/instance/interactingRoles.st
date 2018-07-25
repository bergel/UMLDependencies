public - interactions
interactingRoles
	"Return a pondered list of roles to which I am interacting"
	"a return could be: an Array(<abstract class>->8 <Information Holder>->239 <Service Provider>->186 <Controller>->38 <interface>->2 <Coordinator>->36 <class>->26 <enum>->7 <Interfacer>->7 <Structurer>->86)"
	^ ((self elements flatCollect: #allDependencies) groupedBy: #role) associations collect: [ :assoc | assoc key -> assoc value size ]