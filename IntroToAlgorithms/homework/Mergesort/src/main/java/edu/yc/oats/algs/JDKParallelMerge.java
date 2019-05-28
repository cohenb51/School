package edu.yc.oats.algs;

import java.util.Arrays;

public class JDKParallelMerge implements Mergesorter {

  JDKParallelMerge() {
  }

  @SuppressWarnings("unchecked")
  public void sortIt(Comparable[] a) {
      Arrays.parallelSort(a);
      }  
}
