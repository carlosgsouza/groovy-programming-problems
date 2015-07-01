package megraprogramming

import spock.lang.Specification;

class ThisOwnerAndDelegateSpec extends Specification {

	def "code running outside a closure don't have owner or delegate"() {
		expect:
		this.class == ThisOwnerAndDelegateSpec
		
		when:
		this.owner
		
		then:
		thrown MissingPropertyException
		
		when:
		this.delegate
		
		then:
		thrown MissingPropertyException
	}
	
	def "by default, owner and delegate in a closure are equal to this"() {
		given:
		def thisObject
		def ownerObject
		def delegateObject
		
		and:
		Closure c = {
			thisObject = this
			ownerObject = owner 
			delegateObject = delegate
		}
		
		when:
		c()
		
		then:
		thisObject     == null
		ownerObject    == this
		delegateObject == this
		
		and:
		this.class == ThisOwnerAndDelegateSpec
	}
	
	def "by default, the owner and delegate of a closure inside another closure are the surrouding closure"() {
		given:
		def thisObject
		def ownerObject
		def delegateObject
		
		and:
		Closure outsideClosure = {
			Closure insideClosure = {
				thisObject     = this
				ownerObject    = owner 
				delegateObject = delegate
			}
			insideClosure()
		}
		
		when:
		outsideClosure()
		
		then:
		thisObject     == null
		ownerObject    == outsideClosure
		delegateObject == outsideClosure
	}
	
	def "the delegate of a closure can be changed"() {
		given:
		def thisObject
		def ownerObject
		def delegateObject
		
		and:
		Object anyObject = new Object()
		
		and:
		Closure outsideClosure = {
			Closure insideClosure = {
				thisObject     = this
				ownerObject    = owner 
				delegateObject = delegate
			}
			insideClosure.delegate = anyObject
			insideClosure()
		}
		
		when:
		outsideClosure()
		
		then:
		thisObject     == null
		ownerObject    == outsideClosure
		delegateObject == anyObject
	}
}
