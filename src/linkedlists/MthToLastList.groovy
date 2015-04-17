package linkedlists

import spock.lang.Specification


class MthToLastList extends Specification {
		
	def "should get the mth element from the end"() {
		when:
		add(1)
		add(2)
		add(3)
		add(4)
		add(5)
		
		then:
		mthToLast(0) == 5
		mthToLast(1) == 4
		mthToLast(2) == 3
		mthToLast(3) == 2
		mthToLast(4) == 1
		
	}
	
	def "should get the elements in the right order"() {
		when:
		add(1)
		add(2)
		add(3)
		add(4)
		add(5)
		
		then:
		get(0) == 1
		get(1) == 2
		get(2) == 3
		get(3) == 4
		get(4) == 5
	}
	
	def "should thrown an exception when ew try to get an element with an index such that index >= size"() {
		given:
		add(1)
		
		expect:
		size == 1
		
		when:
		get(1)
		
		then:
		thrown(Exception)
		
	}
	
	def "should count the number of elements in the list"() {
		expect:
		size == 0
		
		when:
		add(1)
		
		then:
		size == 1
		
		when:
		add(2)
		
		then:
		size == 2
	}
	
	def "should thrown an exception when m == size"() {
		given:
		add(1)
		
		expect:
		size == 1
		
		when:
		mthToLast(1)
		
		then:
		thrown(IllegalArgumentException)
	}
	
	def "should thrown an exception when m > size"() {
		given:
		add(1)
		
		expect:
		size == 1
		
		when:
		mthToLast(2)
		
		then:
		thrown(IllegalArgumentException)
	}
	
	def "should thrown an exception when m < 0"() {
		when:
		mthToLast(-2)
		
		then:
		thrown(IllegalArgumentException)
	}
	
	class Element {
		def value
		def next
	}
	
	def size = 0
	def first
	def last
	
	def add(value) {
		def element = new Element()
		element.value = value
		
		if(first == null) {
			first = element
		} else {
			def last = first
			
			while(last.next != null) {
				last = last.next
			}
			
			last.next = element
		}
		
		size++
	}
	
	def get(index) {
		if(index >= size || index < 0) {
			throw new IllegalArgumentException()
		}
		
		def i = 0
		def ithElement = first
		
		while(i < index) {
			ithElement = ithElement.next
			i++
		}
		
		return ithElement.value
	}
	
	def mthToLastWithSize(m) {
		get(size - m - 1)
	}
	
	def mthToLast(m) {
		if(m < 0) {
			throw new IllegalArgumentException()
		}
		
		def i = 0
		def ithElement = first
		def mthElement = first
		
		while(ithElement?.next) {
			ithElement = ithElement.next
			i++
			
			if(i > m) {
				mthElement = mthElement.next
			}
		}
		
		if(i < m) {
			throw new IllegalArgumentException()
		}
		
		return mthElement.value
	}
}