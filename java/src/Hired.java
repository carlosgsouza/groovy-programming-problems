class MyClass {
    
    static class R {
        public R(int min, int max) {
            this.min = min;
            this.max = max;
        }
        
        public R(R left, R right) {
            if(left.min < right.min) {
                this.min = left.min;
                this.max = max(left.max, right.max);
            } else {
                this.min = right.min;
                this.max = right.max;
            }
        }
        
         int min;
        int max;
        
    }
    
    static int max(int... g) {
        int max = -Integer.MAX_VALUE;
        for(int i : g) {
            if(i > max) {
                max = i;
            }
        }
        return max;
    }
    
    public static void maxdiff(Integer[] v) {
        // Write your code here
        // To print results to the standard output you can use System.out.println()
        // Example: System.out.println("Hello world!");
        
        int l = v.length / 2;
        R left = visit(0, l, v);
        R right = visit(l+1, v.length-1, v);
        
        R result = new R(left, right);
        System.out.println(result.max-result.min);
    }
    
    public static R visit(int l, int r, Integer[] v) {
        if(l>=r) {
            return new R(v[l], v[l]);
        }
        
        int length = r-l;
        
        R left = visit(l, length/2, v);
        //R right = visit(length/2+1, r, v);
    return null;
        //return new R(left, right);
    }
}