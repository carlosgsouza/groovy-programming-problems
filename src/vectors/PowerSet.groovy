package vectors;

import spock.lang.Specification
import spock.lang.Unroll

public class PowerSet extends Specification {

	@Unroll
	def "the power set of #values is #result"() {
		given:
		int[] valuesArray = toArray(values)
		int[][] resultArray = toArrayOfArrays(result)

		expect:
		powerset(valuesArray) == resultArray

		where:
		values 	| result
		[]		| [[]]
		[1]		| [[], [1]]
		[1, 2]	| [[], [2], [1], [1, 2]]
		[1, 1]	| [[], [1], [1], [1, 1]]
	}

	private int[] toArray(List<Integer> list) {
		int[] result = new int[list.size()];
		for(int i = 0; i < list.size(); i++) {
			result[i] = list[i];
		}
		return result;
	}

	private int[][] toArrayOfArrays(List<List<Integer>> listOfLists) {
		int[][] result = new int[listOfLists.size()];
		for(int i = 0; i < listOfLists.size(); i++) {
			result[i] = toArray(listOfLists[i]);
		}
		return result;
	}

	private static class Result {
		int[][] sets;
		int currentSet;

		public Result(int[] values) {
			sets =  new int[2 ** values.length][];
			currentSet = 0;
		}

		public void addSet(int[] values, boolean[] include) {
			int setSize = 0;
			for(int i = 0; i < include.length; i++) {
				if(include[i] == true) {
					setSize++;
				}
			}

			int[] set = new int[setSize];
			int j = 0;
			for(int i=0; i < values.size(); i++) {
				if(include[i] == true) {
					set[j++] = values[i];
				}
			}			

			sets[currentSet++] = set;
		}
	}

	public int[][] powerset(int[] values) {
		Result R = new Result(values);

		boolean[] include = new boolean[values.length];

		powerset(R, values, include, 0);

		return R.sets;
	}

	public void powerset(Result R, int[] values, boolean[] include, Integer current) {
		if(current == values.length) {
			R.addSet(values, include);
			return;
		}

		include[current] = false;
		powerset(R, values, include, current+1);

		include[current] = true;
		powerset(R, values, include, current+1);
	}

}
