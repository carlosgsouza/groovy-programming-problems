package tree.bst

import spock.lang.Specification
import spock.lang.Unroll;

class BinarySearchTree extends Specification {

	@Unroll
	def "should crate the tree #exported from values #values"() {
		expect:
		new BinarySearchTreeImpl(values).export() == exported
		
		where:
		values					| exported
		[]						| null
		[1]						| [value:1]
		[null]					| [value:null]
		[2, 1, 3]				| [value:2, left:[value:1], right:[value:3]]
		[4, 2, 8, 1, 3, 6, 10]	| [value:4, left:[value:2, left:[value:1], right:[value:3]], right:[value:8, left:[value:6], right:[value:10]]]
		[1, 2, 3, 4]			| [value:1, right:[value:2, right:[value:3, right:[value:4]]]]
		[4, 1, 4, 1]			| [value:4, left:[value:1, right:[value:1]], right:[value:4]]
	}
	
	@Unroll
	def "given the tree created with values #values, when the node with value #removedValue is removed, the tree should be #resultingTree"() {
		given:
		def tree = new BinarySearchTreeImpl(values)
		
		when:
		tree.remove(removedValue)
		
		then:
		tree.export() == resultingTree
		
		where:
		values					| removedValue	| resultingTree
		[]						| null			| null
		[]						| 1				| null
		[1]						| 1				| null
		[1, 2]					| 1				| [value:2]
		[1, 2]					| 2				| [value:1]
		[2, 1]					| 2				| [value:1]
		[2, 1, 3]				| 1				| [value:2, right:[value:3]]
		[2, 1, 3]				| 2				| [value:3, left:[value:1]]
		[2, 1, 3]				| 3				| [value:2, left:[value:1]]
		[1, 2, 3, 4]			| 2				| [value:1, right:[value:3, right:[value:4]]]
		[4, 2, 8, 1, 3, 6, 10]	| 10			| [value:4, left:[value:2, left:[value:1], right:[value:3]], right:[value:8, left:[value:6]]]
		[4, 2, 8, 1, 3, 6, 10]	| 8				| [value:4, left:[value:2, left:[value:1], right:[value:3]], right:[value:10, left:[value:6]]]
		[4, 2, 8, 1, 3, 6, 10]	| 4				| [value:8, left:[value:2, left:[value:1], right:[value:3]], right:[value:10, left:[value:6]]]
	}
	
	@Unroll
	def "given a BST with the values #values, exists(#number) should return #exists"() {
		expect:
		new BinarySearchTreeImpl(values).exists(number) == exists
		
		where:
		exists	| number	| values
		false	| 1			| []
		false	| 1			| [2]
		true	| 1			| [1]
		true	| 7			| [5, 2, 7, 1, 9, 3, 8, 6, 4]
		true	| 50		| -100..100	// Unless we have a balanced BST, this will create an equivalent to a sorted linked list
	}
	
	@Unroll
	def "given a BST with the values #values, should find the node with value #number"() {
		when:
		def node = new BinarySearchTreeImpl(values).find(number)
		
		then:
		node.value == number
		
		where:
		number	| values
		1		| [1]
		7		| [5, 2, 7, 1, 9, 3, 8, 6, 4]
		50		| -100..100	// Unless we have a balanced BST, this will create an equivalent to a sorted linked list
	}
	
	@Unroll
	def "given a BST with the values #values, should not find a node with value #number"() {
		when:
		def node = new BinarySearchTreeImpl(values).find(number)
		
		then:
		node == null
		
		where:
		number	| values
		1		| []
		1		| [4]
		100		| [5, 2, 7, 1, 9, 3, 8, 6, 4]
		101		| -100..100	// Unless we have a balanced BST, this will create an equivalent to a sorted linked list
	}
	
	public static class BinarySearchTreeImpl {
		
		static class Node {
			def value
			Node left
			Node right
			
			public String toString() {
				return "[value:$value, left:${left?.value}, right:${right?.value}]"
			}
		}
		
		Node root
		
		public exists(value) {
			find(value) != null
		}
		
		public Node find(value) {
			Node node = root
			while(node) {
				if(node.value == value) {
					return node
				} else {
					if(value < node.value) {
						node = node.left
					} else {
						node = node.right
					}
				}
			}
			
			return null
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
		
		public BinarySearchTreeImpl(values) {
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
		
		
		
		public remove(value) {
			if(root == null) {
				return
			}
			
			if(root.value == value) {
				root = removeNode(root)
			}
			
			Node parent = root
			while(parent) {
				if(parent.left?.value == value) {
					parent.left = removeNode(parent.left)
					return
				} else if(parent.right?.value == value) {
					parent.right = removeNode(parent.right)
					return
				} else if(value < parent.left?.value) {
					parent = parent.left
				} else {
					parent = parent.right
				}
			}
		}
		
		private Node removeNode(Node node) {
			if(node == null) {
				return null
			}
			
			Node result = node
			Node parent = null
			String lastDirection = null
			
			while(node) {
				if(node.right) {
					node.value = node.right.value
					
					parent = node
					node = node.right
					lastDirection = 'r'
				} else if(node.left) {
					node.value = node.left.value
					
					parent = node
					node = node.left
					lastDirection = 'l'
				} else {
					// We hit a leaf
					
					if(parent == null) {
						// In case the deleted node is a , we return null instead of a Node with value=null
						return null
					}
				
					if(lastDirection == 'l') {
						parent.left = null
						return result
					} else if(lastDirection == 'r') {
						parent.right = null
						return result
					}
				}
			}
		}
	
	}
}
