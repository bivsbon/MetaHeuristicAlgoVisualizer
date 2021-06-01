package helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FactorialArray {
	private int[] fact;
	private long nFactorial;
	private int n;
	Random generator = new Random();
	
	public long getNFactorial() {
		return nFactorial;
	}
	
	public FactorialArray(int n) {
		this.n = n;
		int k = 0;
		fact = new int[n];
		fact[k] = 1;
		while (++k < n) {
			fact[k] = fact[k - 1] * k;
		}
		nFactorial = fact[k-1];
	}

 	public ArrayList<Integer> generateIthPermutaion(long i) {
		int j, k = 0;
		Integer perm[] = new Integer[n];
		
		// compute factorial code
		for (k = 0; k < n; ++k)
		{
			perm[k] = (int) (i / fact[n - 1 - k]);
			i = i % fact[n - 1 - k];
		}
		
	   // re-adjust values to obtain the permutation
	   // start from the end and check if preceding values are lower
	   for (k = n - 1; k > 0; --k)
	      for (j = k - 1; j >= 0; --j)
	         if (perm[j] <= perm[k])
	            perm[k]++;


	   ArrayList<Integer> permList = new ArrayList<>(Arrays.asList(perm));
	   return permList;
	}
 	
	public long generateNewPerm(long p1, long p2) {
		double phi = generator.nextDouble()*2 - 1;
		long newPerm = (int) (p1 + phi * (p1 - p2));
		newPerm = Math.max(0, Math.min(nFactorial, newPerm));
		return newPerm;
	}
}
