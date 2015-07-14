package iterators

import spock.lang.Specification
import spock.lang.Unroll

class BSTIterator extends Specification {

	@Unroll
	def "iterating on a BST with elements added in the following order #tree_elements should return the following #elements"() {
		
		given:
		BST bst = new BST()
		
		and:
		tree_elements.each { int element ->
			bst.add(element)
		}
		
		and:
		Iterator<Integer> iterator = bst.iterator()
		
		when:
		def iteratedNumbers = []
		while(iterator.hasNext()) {
			iteratedNumbers.add(iterator.next())
		}
		
		then:
		iteratedNumbers == numbers
		
		where:
		tree_elements	| numbers
		[4, 2,6, 1,3,7]	| [1, 2, 3, 4, 6, 7]
		[4, 5, 6, 7, 8]	| [4, 5, 6, 7, 8]
		[]				| []
		[4]				| [4]
	
	}
	
	public class BST implements Iterable<Integer> {
	
		Node root = null;
	
		public class Node {
			Node left, right;
			Integer value;
			
			boolean visited = false;
			
			public Node(Integer value) {
				this.value = value;
			}
			
			@Override
			public String toString() {
				"$value ($visited)"
			}
		}
	
		public class MyBSTIterator implements Iterator<Integer> {
			BST bst = null;
			LinkedList<Node> S = new LinkedList<Node>();
	
			public MyBSTIterator(BST bst) {
				this.bst = bst;
				if(this.bst.root != null) {
					S.push(this.bst.root);
				}
				
				enqueueSmallerChildren()
			}
			
			@Override
			public boolean hasNext() {
				return S.size() > 0
			}
	
			@Override
			public Integer next() {
				if(!hasNext()) {
					throw new IllegalStateException("No nodes");
				}
				
				Integer result = S.peek().value;
				
				S.peek().visited = true;
				println "Visiting node ($result): $S"
				
				Node rightChild = S.peek().right
				
				if(rightChild != null) {
					S.add(0, rightChild)
					rightChild.visited = false
					
					println "Pushing right child($rightChild.value): $S"
					
					enqueueSmallerChildren()
				} else {
					dequeueVisited()
				}
				
				return result;
			}
			
			private void enqueueSmallerChildren() {
				if(S.isEmpty()) {
					return
				}
				
				Node n = S.peek().left
				if(n != null && n.visited == true) {
					return
				}				
				
				while(n != null) {
					S.add(0, n)
					n.visited = false
					
					n = n.left
				}
				
				println "Enqueueing smaller children: $S"
			}
			
			private void dequeueVisited() {
				while(S.isEmpty() == false && S.get(0).visited) {
					S.remove()
				}
				
				println "Dequeueing visited: $S"
			}
	
			@Override
			public void remove() {
				throw new Exception();
			}
		}
	
		public void add(Integer value) {
			if(root == null) {
				root = new Node(value)
			} else {
				Node node = root
				while(true) {
					if(value < node.value) {
						if(node.left == null) {
							node.left = new Node(value)
							break
						} else {
							node = node.left
						}
					} else {
						if(node.right == null) {
							node.right = new Node(value)
							break
						} else {
							node = node.right
						}
					}
				}
			}
		}
	
		@Override
		public Iterator<Integer> iterator() {
			return new MyBSTIterator(this);
		}
	
	}
	
}
