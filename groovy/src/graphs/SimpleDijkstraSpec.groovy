package graphs

import graphs.SimpleDijkstra
import spock.lang.Specification

class SimpleDijkstraSpec extends Specification {
	
	def graph = [
				//	 0, 1, 2, 3, 4, 5, 6
				[ 0, 1,-1,-1,-1,-1,-1], //-1
				[-1, 0, 7, 9,-1,-1,14], // 1
				[-1, 7,-1,10,15,-1,-1], // 2
				[-1, 9,10,-1,11,-1, 2], // 3
				[-1,-1,15,11,-1, 6,-1], // 4
				[-1,-1,-1,-1, 6,-1, 9], // 5
				[-1,14,-1, 2,-1, 9,-1]  // 6
				]

	def "should find the shortest distance between every pair of edges"() {
		when:
		def result = (new SimpleDijkstra()).dijkstra(1, graph)
		
		then:
		result.distances == [1, 0, 7, 9, 20, 20, 11]
		result.paths == [0:[1], 1:[], 2:[1], 3:[1], 4:[1, 3], 5:[1, 3, 6], 6:[1, 3]]
	}

}
