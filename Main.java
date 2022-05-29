/**
 * Sieve of Erastothenes 
 * 
 * 
 * 
 *  [2, 3, 4, 5, 6, 7, 8, 9, 10, ...,  N  ] <- set
 *   0  1  2  3  4  5  6  7   8       N-2   <- indexes 
 * 
 *  THREADS - number of available threads
 *  (N-1)/THREADS  - number of array elements per thread
 * 
 *  In the code below instead of keep all numbers in an array 
 *  it keeps an array of booleans matching the numbers with boolean values.
 *  The approach allow to indicate if a number is a prime number or not.
 *  If false the number is not a prime or is respectively.
 *
 * 
*/


package src;

import java.util.Arrays;

class Main {
    public static void main(String[] args) {

        
        final int N = 30;      // The end number of set. 
        final int THREADS = 5; // Number of available threads

        int from = 0;
        int to = 0;

        int n = 0; // For dynamic number of elements calculation 

        int mod = 0; 
        int extra = 0;
        int div = 0;

        boolean[] bool = new boolean[N];
        Arrays.fill(bool, Boolean.TRUE);

        for(int i = 0; i < N - 1; i++)
        {
            if(!bool[i])
                continue;
            
            from = 0;
            to = 0;

            /**
             * N-1 is the number of elements in bool array, when starting iterates through this array
             * the value bool[i] means i+2 e.g, bool[0] = 2, bool[1] = 3, bool[2] = 4 etc.
             * 
             *  When we chose bool[i] number as divisor and we start looping through array to find all multiples we 
             *  start searching from bool[i+1] to bool[N-1]
             * 
             * e.g We start searching all multiples of 2 in  [2, 3, 4, 5] so we start from 3 and next value is 4 etc. 
             * so after looping the bool array should looks like [1, 1, 0 , 1]. As you saw above we are searching 
             * multiple in [3, 4, 5] set then if we chose 4 as divisor we will search just in on element  ([5]) set 
             * 
             * So the number of elements we have to check changes dynamically, and (N-1) - (i+1) is the formula how to calculate them:
             * 
             * for i = 0 we gets (N - 1) - 1 , where N - 1 is the number of all elements in the bool  
             * for i = 1 we gets (N - 1) - 2 .. etc
             *      
             * 
            */

            n = (N-1) - (i+1);
            mod = n%THREADS;
            div = n/THREADS;

            for(int t = 0; t < THREADS; t++)
            {

                /**
                 *  Sometimes it can occur that n is equal to 9 and there are 5 available threads, 
                 *  each thread should get the same number of calculations it should do, but the 
                 *  division 9/5 give mod 4 so each thread from 0 less to 4 gets one additional number
                 *  in this example threads 0, 1, 2, 3. 
                */

                if(mod > 0)
                    extra = 1;

                from = to;
                to = from + div + extra;

                new PThread().run(bool, from, to, i + 2, i + 1);

                extra = 0;
                mod = mod - 1;
            }

        }


        System.out.println("Prime numbers: ");
        for(int i = 0; i < N; i++)
        System.out.print(bool[i]? (i + 2) + " " : "");
        

    }




}

class PThread extends Thread {
    public void run(boolean[] bool, int from, int to, int divisor, int offset ) {
        for(int i = from + offset; i <= to + offset; i++){
            if( ((i + 2) % divisor)  == 0 )
                bool[i] = false;
        }
   }
}