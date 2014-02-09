package tree

import spock.lang.Specification

class PrintExtremeNodesOfEachLevelInAlternateOrder extends Specification {
	
/*
		Given a binary tree. 
		Print nodes of extreme corners of each level but in alternate order.
		
		
						10
		          5             11
		       9     20      -       15
		   14    - -     -               25
		                                   30
		then output should be 10,11,9,25,30 
		left most of 0th level 
		right most of 1st level 
		left most of 2nd level 
		& like this
		
		(http://www.careercup.com/question?id=19574703)
 */

	def "should build a BST and search for a node on it"() {
		given:
		def tree = n(10, n(5, n(9, n(14), null), n(20)), n(11, null, n(15, null, n(25, null, n(30)))))
		
		when:
		def resultRecursive = extremeNodesInAlternateOrderRecursive(tree)
		def resultIterative = extremeNodesInAlternateOrderIterative(tree)
		
		then:
		resultRecursive == [10, 11, 15, 25, 30]
		resultIterative == [10, 11, 15, 25, 30]
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

	def extremeNodesInAlternateOrderRecursive(tree) {
		def level_values = []
		
		extremeNodesInAlternateOrderRecursive(tree, 0, level_values)
		
		return level_values
	}
	
	def extremeNodesInAlternateOrderRecursive(node, level, level_values) {
		if(!node) {
			return
		}
		
		// even levels should return the leftmost element
		// odd levels should return rightmost element
		def even = level%2 == 0
		
		// only add the element in case it is the first of that level
		if(even && level_values.size() <= level) {
			level_values << node.value
		} else {
			if(level_values.size() <= level) {
				level_values << node.value
			} else {
				level_values[level] = node.value
			}
		}
		
		extremeNodesInAlternateOrderRecursive(node.left, level+1, level_values)
		extremeNodesInAlternateOrderRecursive(node.right, level+1, level_values)
	}
	
	
	def extremeNodesInAlternateOrderIterative(tree) {
		def level_values = []
		
		def nodeQueue = [tree]		// nodes found in breadth-first 
		def levelQueue = [0]	// level of the nodes in the nodeQueue
		
		def i = 0
		while(i < nodeQueue.size()) {
			def node = nodeQueue[i]
			def level = levelQueue[i]
			
			// even levels should return the leftmost element
			// odd levels should return rightmost element
			def even = level%2 == 0
		
			// only add the element in case it is the first of that level
			if(even && level_values.size() <= level) {
				level_values << node.value
			} else {
				if(level_values.size() <= level) {
					level_values << node.value
				} else {
					level_values[level] = node.value
				}
			}
		
			if(node.left) {
				nodeQueue << node.left
				levelQueue << level+1
			}
			
			if(node.right) {
				nodeQueue << node.right
				levelQueue << level+1
			}
			
			i++
		}
		
		return level_values
	}
}
