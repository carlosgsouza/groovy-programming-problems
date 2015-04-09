package linkedlists

import spock.lang.Specification

class mthToLastListSpec extends Specification {

	MthToLastList list = new MthToLastList()

	def "should get the mth element from the end"() {
		when:
		list.add(1)
		list.add(2)
		list.add(3)
		list.add(4)
		list.add(5)
		
		then:
		list.mthToLast(0) == 5
		list.mthToLast(1) == 4
		list.mthToLast(2) == 3
		list.mthToLast(3) == 2
		list.mthToLast(4) == 1
		
	}
	
	def "should get the elements in the right order"() {
		when:
		list.add(1)
		list.add(2)
		list.add(3)
		list.add(4)
		list.add(5)
		
		then:
		list.get(0) == 1
		list.get(1) == 2
		list.get(2) == 3
		list.get(3) == 4
		list.get(4) == 5
	}
	
	def "should thrown an exception when ew try to get an element with an index such that index >= list.size"() {
		given:
		list.add(1)
		
		expect:
		list.size == 1
		
		when:
		list.get(1)
		
		then:
		thrown(Exception)
		
	}
	
	def "should count the number of elements in the list"() {
		expect:
		list.size == 0
		
		when:
		list.add(1)
		
		then:
		list.size == 1
		
		when:
		list.add(2)
		
		then:
		list.size == 2
	}
	
	def "should thrown an exception when m == list.size"() {
		given:
		list.add(1)
		
		expect:
		list.size == 1
		
		when:
		list.mthToLast(1)
		
		then:
		thrown(IllegalArgumentException)
	}
	
	def "should thrown an exception when m > list.size"() {
		given:
		list.add(1)
		
		expect:
		list.size == 1
		
		when:
		list.mthToLast(2)
		
		then:
		thrown(IllegalArgumentException)
	}
	
	def "should thrown an exception when m < 0"() {
		when:
		list.mthToLast(-2)
		
		then:
		thrown(IllegalArgumentException)
	}
}
