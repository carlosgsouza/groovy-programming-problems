package megraprogramming

import spock.lang.Specification

class MissingMethodsAndPropertiesSpec extends Specification {

	def "should implement default behaviour for missing methods and properties"() {
		expect:
		existingProperty == "existingProperty exists"
		
		and:
		nonExistingProperty == "nonExistingProperty is missing"
		
		and:
		existingMethod("1", "2") == "existingMethod(1, 2) exists"
		
		and:
		nonExistingMethod("1", "2") == "nonExistingMethod([1, 2]) is missing"
		nonExistingMethod() == "nonExistingMethod([]) is missing"
		
		and:
		existingMethod("1") == "existingMethod([1]) is missing"
		existingMethod(0.5, 3.14) == "existingMethod([0.5, 3.14]) is missing"
		existingMethod() == "existingMethod([]) is missing"
	}
		
	String existingProperty = "existingProperty exists"
	
	public String existingMethod(String arg1, String arg2) {
		"existingMethod($arg1, $arg2) exists"
	}
	
	public Object methodMissing(String name, Object args) {
		"$name($args) is missing"
	}
			
	def propertyMissing(String name) {
		println name
		"$name is missing"
	}
}
