package megraprogramming

import spock.lang.Specification;

class InjectingMethodsSpec extends Specification {

	def "should inject a method in this class"() {
		given:
		String a = "A"
		String b = "B"
		
		when:
		String.metaClass.giveMeThis {
			return this
		}
		String.metaClass.giveMeDelegate {
			return delegate
		}
		String.metaClass.giveMeOwner {
			return owner
		}
		
		then:
		a.giveMeThis() == this
		a.giveMeDelegate() == a
		
		b.giveMeThis() == this
		b.giveMeDelegate() == b
		
		when:
		b.metaClass.willApplyToBOnly {
			return "yes"
		}
		
		then:
		b.willApplyToBOnly() == "yes"
		
		when:
		a.willApplyToBOnly() == "yes"
		
		then:
		thrown MissingMethodException
	}
}
