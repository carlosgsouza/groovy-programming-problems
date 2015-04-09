package tree.bst

import spock.lang.Specification
import spock.lang.Unroll;

class BinarySearchTree extends Specification {

	@Unroll
	def "given a BST with the values #values, exists(#number) should return #exists"() {
		expect:
		new IterativeBinarySearchTree(values).exists(number) == exists
		
		where:
		exists	| number	| values
		false	| 1			| []
		false	| 1			| [2]
		true	| 1			| [1]
		true	| 7			| [5, 2, 7, 1, 9, 3, 8, 6, 4]
		true	| 50		| -100..100	// Unless we have a balanced BST, this will create an equivalent to a sorted linked list
	}
	
	@Unroll
	def "given a BST with values #values, should export it"() {
		expect:
		new IterativeBinarySearchTree(values).export() == export
		
		where:
		values					| export
		[]						| null
		[1]						| [value:1]
		[null]					| [value:null]
		[2, 1, 3]				| [value:2, left:[value:1], right:[value:3]]
		[4, 2, 8, 1, 3, 6, 10]	| [value:4, left:[value:2, left:[value:1], right:[value:3]], right:[value:8, left:[value:6], right:[value:10]]]
		[1, 2, 3, 4]			| [value:1, right:[value:2, right:[value:3, right:[value:4]]]]
	}
	
	public static class IterativeBinarySearchTree {
		
		static class Node {
			def value
			Node left
			Node right
		}
		
		Node root
		
		public IterativeBinarySearchTree(values) {
			values.each {
				insert(it)	
			}
		}
		
		public Map export() {
			exportNode(root)
		}
		
		private exportNode(Node node) {
			if(node == null) {
				return null
			} 
			
			def result = [value : node.value]
			if(node.left) {
				result.left = exportNode(node.left)
			} 
			if(node.right) {
				result.right = exportNode(node.right)
			}
			return result
		}
		
		public exists(value) {
			Node node = root
			while(node) {
				if(node.value == value) {
					return true
				} else {
					if(value < node.value) {
						node = node.left
					} else {
						node = node.right
					}
				}
			}
			
			return false
		}
		
		public insert(value) {
			if(root == null) {
				root = new Node(value:value)
			} else {
				Node node = root
				while(true) {
					if(value < node.value) {
						if(node.left == null) {
							node.left = new Node(value:value)
							break
						} else {
							node = node.left
						}
					} else {
						if(node.right == null) {
							node.right = new Node(value:value)
							break
						} else {
							node = node.right
						}
					}
				}
			}
		}
	}
}
