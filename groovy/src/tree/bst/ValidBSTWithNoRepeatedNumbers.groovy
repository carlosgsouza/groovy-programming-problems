package tree.bst

import spock.lang.Specification

class ValidBSTWithNoRepeatedNumbers extends Specification {

	def "should build a BST and search for a node on it"() {
		expect:
		valid == valid(tree).valid
		
		where:
		valid	| tree
		true	| n(2, n(1), n(3))
		false	| n(1, n(2), n(3))
		false	| n(1, n(1), n(3))
		false	| n(2, n(1, n(0), n(2)), n(3))
		false	| n(2, n(1, n(0), null), n(3, n(2), null))
	}
	
	static class Node {
		def value
		Node left
		Node right
		
		public String toString() {
			"$value:{$left, $right}"
		}
	}
	
	static Node n(value, left, right) {
		return new Node(value:value, left:left, right:right)
	}
	
	static Node n(value) {
		return new Node(value:value)
	}

	private valid(Node node) {
		if(node == null) {
			return [valid:true]
		}
		
		def leftS = valid(node.left)
		def rightS = valid(node.right)
		
		if(!leftS.valid || !rightS.valid) {
			return [valid:false]
		}
		
		if((node.left && node.value <= leftS.max) || (node.right && node.value >= rightS.min)) {
			return [valid:false]
		}
		
		def min = node.left ? leftS.min : node.value
		def max = node.right ? rightS.max : node.value
		
		return [valid:true, min:min, max:max]
	}
}
