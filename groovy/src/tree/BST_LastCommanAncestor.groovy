package tree

import spock.lang.Specification

class BST_LastCommanAncestor extends Specification {

	def "should build a BST and search for a node on it"() {
		expect:
		new BinarySearchTree([8, 4, 16, 2, 6, 10, 20]).lca(x, y) == lca
		
		where:
		x	| y		| lca
		4	| 16	| 8
		4	| 6		| 4
		2	| 6		| 4
		6	| 2		| 4	// order doesn't matter
		6	| 10	| 8
		20	| 8		| 8
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
		
		public lca(x, y) {
			def node = root
			def min = Math.min(x, y)
			def max = Math.max(x,y)
			
			while(true) {
				if(node.value < min) {
					node = node.right // both min and max are under the node.right
				} else if(node.value > max) {
					node = node.left // both min and max are under the node.left
				} else {
					return node.value // min is under node.left and max is under node.right. If we go deeper in the tree, the next node won't be an ancestor of either min or max
				}
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
