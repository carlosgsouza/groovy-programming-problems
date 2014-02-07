import java.util.ArrayList;
import java.util.List;


public class Spoj_SMPSEQ3 {

	public void run(List<Integer> S, List<Integer> Q) {
		
		int j = 0, i = 0;
		
		for(; j < Q.size(); j++) {			
			for(; i < S.size() && S.get(i) <= Q.get(j); i++) {
				if(S.get(i) != Q.get(j)) {
					System.out.println(S.get(i));
				}
			}
		}
		
	}
	
	public static void main(String args[]) {
		List<Integer> S = new ArrayList<Integer>() {{
			add(1);
			add(2);
			add(3);
			add(4);
			add(5);
			add(6);
			add(7);
		}};
		
		List<Integer> Q = new ArrayList<Integer>() {{
			add(2);
			add(4);
			add(6);
			add(8);
		}};
		
		new Spoj_SMPSEQ3().run(S, Q);
	}

}
