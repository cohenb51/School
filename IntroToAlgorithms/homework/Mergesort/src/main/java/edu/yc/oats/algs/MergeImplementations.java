package edu.yc.oats.algs;

/** Enum defines set of classes that implement the Mergesorter interface
 *
 * @author Avraham Leff
 */

import java.util.Comparator;

public enum MergeImplementations {
  ParallelMerge, Merge, MergeX, JDKParallelMerge;

  public static Mergesorter MergesorterFactory (final MergeImplementations impl) {
    Mergesorter sorter = null;
    switch(impl) {
    case Merge:
      sorter = new Merge();
      break;
    case MergeX:
      sorter = new MergeX();
      break;
    case ParallelMerge:
      sorter = new ParallelMerge();
      break;
    case JDKParallelMerge:
      sorter = new JDKParallelMerge();
      break;
    default:
      throw new IllegalArgumentException("Unknown Mergesorter implementation: "
                                         +impl);
    }

    return sorter;
  }
  
  /** is v < w ?
   *
   * @author Sedgewick
   */
  @SuppressWarnings("unchecked")
  static boolean less(Comparable v, Comparable w) {
    return v.compareTo(w) < 0;
  }
        
  /** is a[i] < a[j]?
   *
   * @author Sedgewick
   */

  @SuppressWarnings("unchecked")
  static boolean less(Object a, Object b, Comparator comparator) {
    return comparator.compare(a, b) < 0;
  }

  /** exchange a[i] and a[j]
   *
   * @author Sedgewick
   */
  static void exch(Object[] a, int i, int j) {
    Object swap = a[i];
    a[i] = a[j];
    a[j] = swap;
  }
}
