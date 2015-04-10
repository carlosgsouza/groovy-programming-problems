package tree.bst

import spock.lang.Specification
import spock.lang.Unroll;

class BinarySearchTree extends Specification {

	def "should find common elements in two trees"() {
		given:
		def tree1 = new BinarySearchTreeImpl([4, 2, 1, 3, 6, 5, 7])
		def tree2 = new BinarySearchTreeImpl([6, 4, 9, 1, 7, 11, 0, 8])
		
		expect:
		tree1.findCommonElements(tree2) == [1, 4, 6, 7]
	}
	
	def "should traverse the tree on pre-order, in-order and post-order"() {
		given:
		def tree = new BinarySearchTreeImpl([4, 2, 1, 3, 6, 5, 7])
		
		expect:
		tree.export() == [
			value:4,
			left:[
				value:2,
				left:[value:1],
				right:[value:3]
			],
			right:[
				value:6,
				left:[value:5],
				right:[value:7]
			]
		]
		
		and:
		tree.preOrderIterative() == [4, 2, 1, 3, 6, 5, 7]
		tree.preOrderRecursive() == [4, 2, 1, 3, 6, 5, 7]
		
		and:
		
		tree.inOrderRecursive() == [1, 2, 3, 4, 5, 6, 7]
		tree.inOrderIterative() == [1, 2, 3, 4, 5, 6, 7]
		
		and:
		tree.postOrderIterative() == [1, 3, 2, 5, 7, 6, 4]
		tree.postOrderRecursive() == [1, 3, 2, 5, 7, 6, 4]
	}
	
	def "Given a BST, replace the node value with the sum of all the node values that are greater than the current node value"() {
		// http://www.careercup.com/question?id=13394676
		given:
		def tree = new BinarySearchTreeImpl([4, 2, 8, 1, 3, 7, 10, 9])
		
		expect:
		tree.export() == [
			value:4,
			left:[
				value:2,
				left:[value:1],
				right:[value:3]
			],
			right:[
				value:8,
				left:[value:7],
				right:[
					value:10,
					left:[value:9]
				],
				
			]
		]
		
		when:
		tree.replaceNodeValuesWithSumOfNodesWithGreaterValues()
		
		then: 
		tree.export() == [
			value:34,
			left:[
				value:3,
				left:[value:1],
				right:[value:3]
			],
			right:[
				value:19,
				left:[value:7],
				right:[
					value:10,
					left:[value:9]
				],
				
			]
		]
	}
	
	def "should transform a BST in a sorted double linked list withou consuming additional space"() {
		// http://www.careercup.com/question?id=14232732
		given:
		def tree = new BinarySearchTreeImpl([4, 2, 5, 1, 3, 6, 7])
		
		expect:
		tree.export() == [
			value:4,
			left:[
				value:2,
				left:[value:1],
				right:[value:3]
			],
			right:[
				value:5,
				right:[
					value:6,
					right:[value:7]
				],
				
			]
		]
		
		when:
		def result = tree.toSortedDoubleLinkedList()
		
		then: "head element does not have and index to the left"
		result.left == null
		
		and: "list is double linked" 
		def values = []
		def node = result
		
		while(node != null) {
			if(node.left) {
				assert node.left.value < node.value
			}
			if(node.right) {
				assert node.right.value >= node.value
			}
			
			values << node.value
			node = node.right
		}
		
		and: "list are sorted"
		values == [1, 2, 3, 4, 5, 6, 7]
		
		and: "tail element does not have a link to the right"
		node == null
	}
	
	@Unroll
	def "the minimum common ancestor of #a and #b is #result"() {
		given:
		def tree = new BinarySearchTreeImpl([4, 2, 8, 1, 3, 6, 10])
		
		expect:
		tree.export() == [
			value:4, 
			left:[
				value:2, 
				left:[value:1], 
				right:[value:3]
			], 
			right:[
				value:8, 
				left:[value:6], 
				right:[value:10]
			]
		]
		
		and:
		tree.getLowestCommonAncestor(a, b) == result
		
		where:
		a	| b		| result
		2	| 8		| 4
		8	| 2		| 4
		2	| 4		| 4
		2	| 2		| 2
		4	| 4		| 4
		2	| 6		| 4
		6	| 10	| 8
	}
	
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
		[2, 4, 3, 5]			| 2				| [value:4, left:[value:3], right:[value:5]]
		[1, 2, 4, 3, 5]			| 2				| [value:1, right:[value:4, left:[value:3], right:[value:5]]]
		[10, 2, 4, 3, 5]		| 2				| [value:10, left:[value:4, left:[value:3], right:[value:5]]]
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
		
		public static class TreeNode {
			def value
			TreeNode left
			TreeNode right
			
			public String toString() {
				return "[value:$value, left:${left?.value}, right:${right?.value}]"
			}
		}
		
		TreeNode root
		
		public exists(value) {
			find(value) != null
		}
		
		public TreeNode find(value) {
			TreeNode node = root
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
				root = new TreeNode(value:value)
			} else {
				TreeNode node = root
				while(true) {
					if(value < node.value) {
						if(node.left == null) {
							node.left = new TreeNode(value:value)
							break
						} else {
							node = node.left
						}
					} else {
						if(node.right == null) {
							node.right = new TreeNode(value:value)
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
		
		public TreeNode toSortedDoubleLinkedList() {
			return nodeToSortedDoubleLinkedList(root).min
		}
		
		public replaceNodeValuesWithSumOfNodesWithGreaterValues() {
			replaceNodeValuesWithSumOfNodesWithGreaterValuesForNode(root)
		}
		
		private int replaceNodeValuesWithSumOfNodesWithGreaterValuesForNode(TreeNode node) {
			int leftSum = 0
			int rightSum = 0
						
			if(node.left != null) {
				leftSum = replaceNodeValuesWithSumOfNodesWithGreaterValuesForNode(node.left)
			}
			if(node.right != null) {
				rightSum = replaceNodeValuesWithSumOfNodesWithGreaterValuesForNode(node.right)
			}
			
			int result = leftSum + node.value + rightSum
			
			if(node.right != null) {
				node.value = rightSum
			}
			
			return result
		}
		
		class MinMaxNode {
			TreeNode min, max
		}
		private MinMaxNode nodeToSortedDoubleLinkedList(TreeNode node) {
			MinMaxNode result = new MinMaxNode()
			
			if(node.left == null) {
				result.min = node
			} else {
				MinMaxNode resultLeft = nodeToSortedDoubleLinkedList(node.left)
				resultLeft.max.right = node
				result.min = resultLeft.min
				node.left = resultLeft.max
			}
			
			if(node.right == null) {
				result.max = node
			} else {
				MinMaxNode resultRight = nodeToSortedDoubleLinkedList(node.right)
				resultRight.max.left = node
				result.max = resultRight.max
				node.right = resultRight.min
			}
			
			return result
		}
		
		public Map export() {
			exportNode(root)
		}
		
		private exportNode(TreeNode node) {
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
		
		// Walk the tree until min(a,b) < node.value < max(a,b)
		public getLowestCommonAncestor(a, b) {
			def min = Math.min(a, b)
			def max = Math.max(a, b)
			
			TreeNode node = root
						
			while(node != null) {
				if(node.value < min) {
					// Both values are under the right side of the node
					node = node.right
				} else if(node.value > max){
					// Both values are under the right side of the node
					node = node.left
				} else {
					return node.value
				}
			}
		}
		
		public remove(value) {
			if(root == null) {
				return
			}
			
			if(root.value == value) {
				root = removeNode(root)
				return
			}
			
			TreeNode parent = root
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
		
		private TreeNode removeNode(TreeNode node) {
			if(node == null) {
				return null
			}
			
			TreeNode result = node
			TreeNode parent = null
			String lastDirection = null
			
			while(node) {
				if(node.right && node.left == null) {
					// node has only the right child, so replace node with it
					if(parent == null) {
						// this is the root node, no need to update parent
						return node.right
					} else if(lastDirection== 'r') {
						// node is the right child
						parent.right = node.right
						return result
					} else if(lastDirection== 'l') {
						// node is the left child
						parent.left = node.right
						return result
					}
				}
				if(node.left && node.right == null) {
					// node has only the left child, so replace node with it
					
					if(parent == null) {
						// this is the root node, no need to update parent
						return node.left
					} else if(lastDirection== 'l') {
						// node is the left child
						parent.left = node.left
						return result
					} else if(lastDirection== 'r') {
						// node is the right child
						parent.right = node.left
						return result
					}
				}
				if(node.right) {
					// promote the right child
					node.value = node.right.value
					
					parent = node
					node = node.right
					lastDirection = 'r'
				} else if(node.left) {
					// promote the left child
					node.value = node.left.value
					
					parent = node
					node = node.left
					lastDirection = 'l'
				} else {
					// We hit a leaf
					
					if(parent == null) {
						// In case the deleted node is the root, we return null instead of a Node with value=null
						return null
					}
				
					// the leaf has been promoted on a previous step
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
		
		public List postOrderRecursive() {
			def result = []
			postOrderRecursiveForNode(root, result)
			return result
		}
		
		private postOrderRecursiveForNode(TreeNode node, List result) {
			if(node.left) {
				postOrderRecursiveForNode(node.left, result)
			}
			if(node.right) {
				postOrderRecursiveForNode(node.right, result)
			}
			
			result.add(node.value)
		}
		
		public List inOrderRecursive() {
			def result = []
			inOrderRecursiveForNode(root, result)
			return result
		}
		
		private inOrderRecursiveForNode(TreeNode node, List result) {
			if(node.left) {
				inOrderRecursiveForNode(node.left, result)
			}
			
			result.add(node.value)
			
			if(node.right) {
				inOrderRecursiveForNode(node.right, result)
			}
		}
		
		public List preOrderRecursive() {
			def result = []
			preOrderRecursiveForNode(root, result)
			return result
		}
		
		private preOrderRecursiveForNode(TreeNode node, List result) {
			result.add(node.value)
			
			if(node.left) {
				preOrderRecursiveForNode(node.left, result)
			}
			
			if(node.right) {
				preOrderRecursiveForNode(node.right, result)
			}
		}
		
		public List preOrderIterative() {
			def result = []
			
			List Q = new LinkedList()
			Q.add(root)
			
			while(!Q.isEmpty()) {
				TreeNode node = Q.pop()
				result << node.value
				
				if(node.right != null) {
					Q.add(node.right)
				}
				if(node.left != null) {
					Q.add(node.left)
				}
			}
			
			return result
		}
		
		public List postOrderIterative() {
			def result = []
			
			List Qnode = new LinkedList()
			List Qvisited = new LinkedList()
			
			Qnode.add(root)
			Qvisited.add(false)
			
			while(!Qnode.isEmpty()) {
				boolean visited = Qvisited.last()
				if(visited == true) {
					result << Qnode.pop().value
					Qvisited.pop()
					
					continue
				}
				
				Qvisited[-1] = true
				TreeNode node = Qnode.last()
				
				if(node.right != null) {
					Qnode.add(node.right)
					Qvisited.add(false)
				}
				if(node.left != null) {
					Qnode.add(node.left)
					Qvisited.add(false)
				}
			}
			
			return result
		}
		
		public List inOrderIterative() {
			def result = []
			
			List Qnode = new LinkedList()
			List Qprocess = new LinkedList()
			
			Qnode.add(root)
			Qprocess.add(false)
			
			while(!Qnode.isEmpty()) {
				boolean process = Qprocess.pop()
				if(process == true) {
					result << Qnode.pop().value
					continue
				}
				
				TreeNode node = Qnode.pop()
				
				if(node.right != null) {
					Qnode.add(node.right)
					Qprocess.add(false)
				}
				
				Qnode.add(node)
				Qprocess.add(true)
				
				if(node.left != null) {
					Qnode.add(node.left)
					Qprocess.add(false)
				}
			}
			
			return result
		}
		
		public List findCommonElements(BinarySearchTreeImpl tree2) {
			def result = []
			
			List Qnode_1 = new LinkedList()
			List Qprocess_1 = new LinkedList()
			Qnode_1.add(root)
			Qprocess_1.add(false)
			
			List Qnode_2 = new LinkedList()
			List Qprocess_2 = new LinkedList()
			Qnode_2.add(tree2.root)
			Qprocess_2.add(false)
			
			def value1 = nextValueInOrder(Qnode_1, Qprocess_1)
			def value2 = nextValueInOrder(Qnode_2, Qprocess_2)
			
			while(value1 != null && value2 != null) {
				if(value1 == value2) {
					result << value1
					value1 = nextValueInOrder(Qnode_1, Qprocess_1)
				} else if(value1 > value2) {
					value2 = nextValueInOrder(Qnode_2, Qprocess_2)
				} else if(value1 < value2) {
					value1 = nextValueInOrder(Qnode_1, Qprocess_1)
				}
			}
			
			return result
			
		}
		
		private nextValueInOrder(List Qnode, List Qprocess) {
			while(!Qnode.isEmpty()) {
				boolean process = Qprocess.pop()
				if(process == true) {
					return Qnode.pop().value
				}
				
				TreeNode node = Qnode.pop()
				
				if(node.right != null) {
					Qnode.add(node.right)
					Qprocess.add(false)
				}
				
				Qnode.add(node)
				Qprocess.add(true)
				
				if(node.left != null) {
					Qnode.add(node.left)
					Qprocess.add(false)
				}
			}
		}
	
	}
	
	
}
