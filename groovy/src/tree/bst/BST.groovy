package tree

import spock.lang.Specification

class BST extends Specification {

	def "should build a BST and search for a node on it"() {
		expect:
		new BinarySearchTree(values).exists(number) == exists
		
		where:
		exists	| number	| values
		false	| 1			| []
		false	| 1			| [2]
		true	| 1			| [1]
		true	| 7			| [5, 2, 7, 1, 9, 3, 8, 6, 4]
		true	| 50		| -100..100	// Unless we have a balanced BST, this will create an equivalent to a sorted linked list
	}

	public static class BinarySearchTree {
		
		static class Node {
			def value
			Node left
			Node right
		}
		
		Node root
		
		public BinarySearchTree(values) {
			values.each {
				insert(it)	
			}
		}
		
		public exists(value) {
			if(!root) {
				return false
			} else {
				Node node = root
				
				while(node) {
					if(node.value == value) {
						return true
					}
					else if(value < node.value) {
						node = node.left
					} else {
						node = node.right
					}
				}
				
				return false
			}
		}
		
		public insert(value) {
			if(!root) {
				root = new Node(value:value)
			} else {
				Node node = root
			
				while(true) {
					if(value < node.value) {
						if(!node.left) {
							node.left = new Node(value:value)
							break
						}  else {
							node = node.left
						}
					} else {
						if(!node.right) {
							node.right = new Node(value:value)
							break
						}  else {
							node = node.right
						}
					}
				}
			}
		}
	}
}
