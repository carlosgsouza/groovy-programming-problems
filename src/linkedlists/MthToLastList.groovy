package linkedlists

import spock.lang.Specification

class MthToLastList {
	
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