package graphs;

import java.util.*;

public class SimpleDijkstra {

	public static class Result {
		List<Integer> distances;
		Map<Integer, List<Integer>> paths;
	}
	
	public Result dijkstra(int source, List<List<Integer>> graph) {
		List<Integer> distances = new ArrayList<Integer>();
		for(int i = 0; i < graph.size(); i++) {
			distances.add(Integer.MAX_VALUE);
		}
		distances.set(source, 0);

		List<Integer> Q = new ArrayList<Integer>();
		for(int i = 0; i < distances.size(); i++) {
			Q.add(i);
		}
		
		List<Integer> previous = new ArrayList<Integer>();
		for(int i = 0; i < distances.size(); i++) {
			previous.add(null);
		}
		
		while(Q.size() > 0) {
			int u = getNextEdge(Q, distances);
			
			for(int v=0; v<graph.size(); v++) {
				if(graph.get(v).get(u) < 0 || u == v) {
					continue;
				}

//				System.out.println(String.format("%d -> %d : %d", u, v, graph.get(v).get(u)));

				int newD = distances.get(u) + graph.get(v).get(u);
				
				if(newD < distances.get(v)) {
					previous.set(v, u);
					distances.set(v, newD);
				}
			}
		}
		
		Result r = new Result();
		r.distances = distances;
		r.paths = buildPaths(previous);
		return r;
	}
	
	private Map<Integer, List<Integer>> buildPaths(List<Integer> previous) {
		Map<Integer, List<Integer>> result = new HashMap<Integer, List<Integer>>();
		
		for(int i = 0; i < previous.size(); i++) {
			
			result.put(i, new ArrayList<Integer>());
			
			Integer j = previous.get(i);
			while(j != null) {
				result.get(i).add(0, j);
				j = previous.get(j);
			}
		}
		
		return result;
	}

	private int getNextEdge(List<Integer> Q, List<Integer> distances) {
		int shortestDistance = distances.get(Q.get(0));
		int result = Q.get(0);
		int nodeToBeRemoved = 0;

		// O(N) -> a heap should be O(log(N))
		int i;
		for(i = 1; i < Q.size(); i++) {
			int d_i = distances.get(Q.get(i));
			
			if(d_i < shortestDistance) {
				shortestDistance = d_i;
				result = Q.get(i);
				nodeToBeRemoved = i;
			}
		}

		Q.remove(nodeToBeRemoved);

		return result;
	}
}