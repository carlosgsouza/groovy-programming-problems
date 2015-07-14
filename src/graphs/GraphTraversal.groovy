package graphs;

import spock.lang.Specification

import com.sun.corba.se.impl.orbutil.graph.Graph

public class GraphTraversal extends Specification {

	def "should traverse a graph using BFS and DFS"() {
		given:
		Graph graph = new Graph(6, [0,1], [0,2], [0,5], [1,3], [2,3], [3,4])
		
		expect:
		graph.bfs(0) == [0, 1, 2, 5, 3, 4]
		graph.dfs(0) == [0, 5, 2, 3, 4, 1]
	}
	
	static class Graph {
		
		int size
		Map<Integer, SortedSet<Integer>> edges
		
		Graph(int size, List<Integer> ...edges) {
			this.size = size
			this.edges = [:]
			
			edges.each { e ->
				addEdge(e[0], e[1])
				addEdge(e[1], e[0])
			}
		}
		
		def addEdge(int from, int to) {
			if(!this.edges[from]) {
				this.edges[from] = new TreeSet<Integer>()
			}
			
			this.edges[from].add(to)
		}
		
		List<Integer> bfs(int from) {
			List<Integer> result = []
			
			Map<Integer, Boolean> seen = [:]
			size.times {
				seen[it] = false
			}
			
			LinkedList<Integer> Q = new LinkedList<Integer>()
			
			Q.add(from)
			seen[from] = true
			
			while(!Q.isEmpty()) {
				int n = Q.removeFirst()
				
				println "Visiting $n"
				
				result.add(n)
				
				edges[n].each { m ->
					if(!seen[m]) {
						seen[m] = true
						println "Enqueueing $m"
						Q.add(m)
					} else {
						println "Neighbor $m has already been visited"
					}
				}
			}
			
			return result
		} 
		
		List<Integer> dfs(int from) {
			List<Integer> result = []
			
			Map<Integer, Boolean> seen = [:]
			size.times {
				seen[it] = false
			}
			
			LinkedList<Integer> S = new LinkedList<Integer>()
			
			S.add(from)
			seen[from] = true
			
			while(!S.isEmpty()) {
				int n = S.removeLast()
				
				result.add(n)
				
				edges[n].each { m ->
					if(!seen[m]) {
						seen[m] = true
						S.add(m)
					}
				}
			}
			
			return result
		} 
	}
}
