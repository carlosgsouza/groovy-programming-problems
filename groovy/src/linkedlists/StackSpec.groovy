package linkedlists

import spock.lang.Specification

class StackSpec extends Specification {

	def "should create an empty list and the add elements to it"() {
		when:
		def stack = new Stack()
		
		then:
		stack.size == 0
		
		when:
		stack.push(1)
		
		then:
		stack.size == 1
		
		when:
		stack.push(2)
		
		then:
		stack.size == 2
	}
	
	def "should pop the elements of the stack in the LIFO order"() {
		given:
		def stack = new Stack()
		
		when:
		stack.push(1)
		stack.push(2)
		stack.push(3)
		
		then:
		stack.pop() == 3
		stack.pop() == 2
		stack.pop() == 1
	}
	
	def "should return null when we try to pop an element from an empty list"() {
		when:
		def stack = new Stack()
		
		then:
		stack.size == 0
		
		and:
		stack.pop() == null
		stack.pop() == null
		
		when:
		stack.push(1)
		
		then:
		stack.pop() == 1
		stack.pop() == null
	}
}
