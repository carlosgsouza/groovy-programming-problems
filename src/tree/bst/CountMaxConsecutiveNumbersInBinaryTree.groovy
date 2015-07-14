package tree.bst

import spock.lang.Specification

class CountMaxConsecutiveNumbersInBinaryTree extends Specification {
	
	def "should count the maximum consecutive numbers in a binary tree"() {
		given:
		Node root = new Node(
			value:7,
			left:new Node(
				value:5,
				left: new Node(value:3),
				right: new Node(value:2)	
			),
			right:new Node(
				value:8,
				left: new Node(value:9),
				right: new Node(value:10)	
			)
		)
		
		expect:
		countMaxConsecutiveNumbers(root) == 3
	}

	int countMaxConsecutiveNumbers(Node root) {
		int result = 0;

		if(root == null) {
			return 0;
		}

		LinkedList<Node> S = new LinkedList<Node>();
		LinkedList<Integer> C = new LinkedList<Node>();

		S.add(root);
		C.add(1);

		while(S.isEmpty() == false) {
			println S
			println C
			
			Node n = S.removeLast();
			Integer c = C.removeLast();

			if(c > result) {
				result = c;
			}

			if(n.left != null) {
				S.add(n.left);
				if(n.value == n.left.value - 1) {
					C.add(c+1);
				} else {
					C.add(1);
				}
			}

			if(n.right != null) {
				S.add(n.right);
				if(n.value == n.right.value - 1) {
					C.add(c+1);
				} else {
					C.add(1);
				}
			}
		}

		return result;
	}
	
	static class Node {
		int value;
		Node left, right;
		
		@Override
		public String toString() {
			"$value"
		}
	}
}
