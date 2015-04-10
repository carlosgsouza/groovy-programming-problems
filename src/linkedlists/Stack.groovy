package linkedlists

import spock.lang.Specification

class Stack {
	
	class Element {
		def value
		def next
	}
	
	def size = 0
	def top
	
	def push(value) {
		def element = new Element()
		element.value = value
		element.next = top
		top = element
		
		size++
	}
	
	def pop() {
		if(top) {
			def result = top.value
			top = top.next
			
			size--
			
			return result
		} else {
			return null
		}
	}
}