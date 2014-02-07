class MyClass {
    
    public static void main(String args[]) {
    	
    	
        // Write your code here
        // To print results to the standard output you can use System.out.println()
        // Example: System.out.println("Hello world!");
    	
    	Integer[] v = new Integer[] {3, 5, 1, -2, 8};
        
        int mid = v.length / 2;
        
        visit(0, mid, v);
        visit(mid+1, v.length -1, v);
    }
    
    
    
    public static void visit(int l, int r, Integer[] v) {
        if(l >= r) {
            System.out.println(l);
        }
        
        int mid = (r-l)/2;
        visit(l, mid, v);
        visit(mid+1, r, v);
    }
}