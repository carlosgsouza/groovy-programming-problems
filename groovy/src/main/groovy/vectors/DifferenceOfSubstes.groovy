package vectors

/*
 * Given an array of integers find a left continuous subset, ls, and a right continuous subset, rs, that maximizes sum(rs)-sum(ls) 
 */
class DifferenceOfSubstes {

	class S {
		int left, right, sum
		
		String toString() {
			return "left:$left, right:$right, sum:$sum"
		}
	}
	
	def min_solution_to = [:]
	def max_solution_from = [:]
	
	public DifferenceOfSubstes() {
		def v = [3, -5, 1, -2, 8, -2, 3, -2, 1]
		v.size().times { i -> 
			min_solution_to[i] = v[i]
			max_solution_from[i] = v[i]
		}
		
		visit_max(0, v.size()-1, v).sum
		visit_min(0, v.size()-1, v).sum
		
		def min = Integer.MAX_VALUE
		min_solution_to.each {
			if(it.value < min) {
				min = it.value
			} else {
				min_solution_to[it.key] = min
			}
		}
		
		def max = -Integer.MAX_VALUE
		max_solution_from.sort{-it.key}.each {
			if(it.value > max) {
				max = it.value
			} else {
				max_solution_from[it.key] = max
			}
		}
		
		def result = -Integer.MAX_VALUE
		(1..(v.size()-1)).each {
			def l = min_solution_to[it-1]
			def r = max_solution_from[it]
			
			if(r-l > result) {
				result = r-l
			}
		}
		println result
	}
	
	def visit_max(l, r, v) {
		if(l >= r) {
			return new S(left:l, right:l, sum:v[l])
		}
		
		int mid = l+(r-l)/2
		
		def s_l = visit_max(l, mid, v)
		def s_r = visit_max(mid+1, r, v)
		def s_m = crossing_solution_max(l, mid, r, v)
		
		def s = [s_l, s_r, s_m].max { it.sum }
		
		return s
	}
	
	def crossing_solution_max(l, mid, r, v) {
		def sumLeft = -Integer.MAX_VALUE
		def sumRight = -Integer.MAX_VALUE
		def indexLeft = 0
		def indexRight = 0
		
		def sum = 0
		
		(mid..l).each {
			sum += v[it]
			if(sum > sumLeft) {
				sumLeft = sum
				indexLeft = it
			}
		}
		
		sum = 0
		((mid+1)..r).each {
			sum += v[it]
			if(sum > sumRight) {
				sumRight = sum
				indexRight = it
			}
		}
		
		def s = new S(left:indexLeft, right:indexRight, sum:sumLeft+sumRight)
		
		if(max_solution_from[s.left] < s.sum) {
			max_solution_from[s.left] = s.sum
		}
		
		return s
	}
	
	def visit_min(l, r, v) {
		if(l >= r) {
			return new S(left:l, right:l, sum:v[l])
		}
		
		int mid = l+(r-l)/2
		
		def s_l = visit_min(l, mid, v)
		def s_r = visit_min(mid+1, r, v)
		def s_m = crossing_solution_min(l, mid, r, v)
		
		def s = [s_l, s_r, s_m].min { it.sum }
		
		return s
	}
	
	def crossing_solution_min(l, mid, r, v) {
		def sumLeft = Integer.MAX_VALUE
		def sumRight = Integer.MAX_VALUE
		def indexLeft = 0
		def indexRight = 0
		
		def sum = 0
		
		(mid..l).each {
			sum += v[it]
			if(sum < sumLeft) {
				sumLeft = sum
				indexLeft = it
			}
		}
		
		sum = 0
		((mid+1)..r).each {
			sum += v[it]
			if(sum < sumRight) {
				sumRight = sum
				indexRight = it
			}
		}
		
		def s = new S(left:indexLeft, right:indexRight, sum:sumLeft+sumRight)
		
		if(min_solution_to[s.right] > s.sum) {
			min_solution_to[s.right] = s.sum
		}
		
		return s
	}
	
	public static void main(args) {
		new DifferenceOfSubstes()
	}

}
