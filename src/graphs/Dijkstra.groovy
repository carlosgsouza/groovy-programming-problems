package graphs

import spock.lang.Specification


class Dijkstra extends Specification {

	def "should find the shortest distance between two nodes"() {
		given: "a connected graph"
		Node a = new Node('a')
		Node b = new Node('b')
		Node c = new Node('c')
		Node d = new Node('d')
		Node e = new Node('e')
		Node f = new Node('f')
		Node g = new Node('g')
		
		a.to(c, 1)
		a.to(d, 2)
		b.to(c, 3)
		b.to(c, 2)
		c.to(e, 3)
		c.to(d, 1)
		d.to(g, 1)
		e.to(f, 2)
		f.to(g, 1)
		
		and: "a separete connected graph"
		Node h = new Node('h')
		Node i = new Node('i')
		
		h.to(i, 5)
		
		and: "a graph with all nodes"
		List<Node> graph = [a, b, c, d, e, f, g, h, i]
				
		expect:
		distance(graph, a, a) == 0
		distance(graph, a, c) == 1
		distance(graph, a, f) == 4
		distance(graph, a, i) == Integer.MAX_VALUE
		distance(graph, h, i) == 5
	}
	
	public int distance(List<Node> graph, Node from, Node to) {
		graph.each { Node node ->
			node.distance = Integer.MAX_VALUE
		}
		from.distance = 0
		
		// Creates a min heap which based on the distance to the node
		PriorityQueue<Node> H = new PriorityQueue<Node>(new Comparator<Node>() {
			@Override
			public int compare(Node n1, Node n2) {
				return n1.distance.compareTo(n2.distance)
			}
		})
		
		graph.each { Node node ->
			H.add(node)
		}
		
		Node n = H.poll()
		while(n != null && n.distance < Integer.MAX_VALUE) {
			println "Visting ${n.name}"
			
			n.edges.each { Node m, int distanceToM ->
				println "Checking distance to $m.name"
				int distanceThroughN = n.distance + distanceToM
				
				if(distanceThroughN < m.distance) {
					H.remove(m)
					m.distance = distanceThroughN
					H.add(m)
				}
			}
			
			n = H.poll()
		}
		
		return to.distance
	}
	
	
	private static class Node {
		String name
		int distance
		Map<Node, Integer> edges = [:]
		
		public Node(String name) {
			this.name = name
		}
		
		public void to(Node node, int distance) {
			this.edges[node] = distance
			node.edges[this] = distance
		}
	}
}
