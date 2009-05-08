class Test {

	Integer age
	String name
	
	static hasMany = [children:Child]
    static constraints = {
		name blank:false
    }
}
