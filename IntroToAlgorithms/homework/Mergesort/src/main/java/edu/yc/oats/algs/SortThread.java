package edu.yc.oats.algs;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class SortThread extends RecursiveTask {
	int lo;
	int mid;
	int hi;
	private Comparable[] aux;
	private  Comparable[] a;
	private static int numberOfThreads;
	private static int numberOfProcesors;
	
	ForkJoinPool mergePool;
	
	
	
	public SortThread(Comparable[] a, int lo, int hi, Comparable[] aux, int numberOfProcesors) {
		this.a =a;
		this.lo = lo;
		this.hi = hi; 
		this.aux = aux;
		this.mergePool = mergePool;
		
	}

	@Override
	//0123456789
	protected Object compute() {

		if (hi <= lo) {
			return null;
		}
		if ((hi - lo <= 9)) {
			MergeX.insertionSort(a, lo, hi);
			return a;

		}
		int mid = lo + (hi - lo) / 2;
		
		  
			  
		SortThread leftPart = new SortThread(a, lo, mid,aux,0);
		SortThread rightPart = new SortThread(a,mid+1, hi,aux, 0);
		if(hi -lo < 100000 ) {
			leftPart.compute();
			rightPart.compute();
		}
			
		
	else {	
	  leftPart.fork();
	  rightPart.fork();
	  leftPart.join();
      rightPart.join();
	}
		 mid = lo + (hi - lo) / 2;
		 if (!(a[mid].compareTo(a[mid + 1]) < 0)) {
				Merge.merge(a, lo, mid, hi, aux);
			}

		return a;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	protected static Comparable[] parallelMerge(Comparable[] a1,Comparable[] a2, Comparable[] aux, int a1lo, int a1hi, int a2lo, int a2hi) {
        int auxPointer = a1lo;
        int a1Pointer = a1lo;
        int a2Pointer = a2lo;
        System.out.println(a1.length);
        for (int k = a1lo; k < a1hi; k++) {
			aux[k] = a1[k];
		}
        for (int k = a2lo ; k < a2hi; k++) {
			aux[k + a1hi] = a2[k];
		}
        
		while (!(a1Pointer == a1hi && a2Pointer == a2hi))  {
			if (a1Pointer >= a1hi)
				aux[auxPointer++] = a2[a2Pointer++];
			else if (a2Pointer >= a2hi)
				aux[auxPointer++] = a1[a1Pointer++];
			else if (a2[a2Pointer].compareTo(a1[a1Pointer]) < 0) {
				aux[auxPointer++] = a2[a2Pointer++];
			}
			else
				aux[auxPointer++] = a1[a1Pointer++];

		}
		return aux;

	}
		
		
	}

