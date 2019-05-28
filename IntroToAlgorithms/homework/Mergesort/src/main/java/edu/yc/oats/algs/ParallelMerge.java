package edu.yc.oats.algs;

import java.util.concurrent.ForkJoinPool;

public class ParallelMerge implements Mergesorter {

  ParallelMerge() {
  }

  @SuppressWarnings("unchecked")
  public void sortIt(Comparable[] a) {
	  Comparable[] aux = new Comparable[a.length];
	  SortThread SortThread = new SortThread(a, 0, a.length-1, aux,0);
	  ForkJoinPool pool = new ForkJoinPool();
	  pool.invoke(SortThread);
  }  
}
