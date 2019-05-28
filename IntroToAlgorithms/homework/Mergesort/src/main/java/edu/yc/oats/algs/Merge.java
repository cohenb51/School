package edu.yc.oats.algs;

import java.util.Arrays;

public class Merge implements Mergesorter {

	Merge() {
	}

	@SuppressWarnings("unchecked")
	public void sortIt(Comparable[] a) {
		Comparable[] aux = new Comparable[a.length];
		mergeSort(a, 0, a.length - 1, aux);
	
	}

	private static void mergeSort(Comparable[] a, int lo, int hi, Comparable[] aux) {
		if (hi <= lo) {
			return;
		}

		int mid = lo + (hi - lo) / 2;
		mergeSort(a, lo, mid, aux);
		mergeSort(a, mid + 1, hi, aux);

		merge(a, lo, mid, hi, aux);

	}

	public static Comparable[] merge(Comparable[] a, int lo, int mid, int hi, Comparable[] aux) {
		int i = lo;
		int j = mid + 1;

		for (int k = lo; k <= hi; k++) {
			aux[k] = a[k];
		}

		for (int k = lo; k <= hi; k++) {

			if (i > mid)
				a[k] = aux[j++];
			else if (j > hi)
				a[k] = aux[i++];
			else if (aux[j].compareTo(aux[i]) < 0) {

				a[k] = aux[j++];
			} else
				a[k] = aux[i++];

		}
		return a;

	}
	public static boolean isSorted(Comparable[] array) {
		for(int i = 1; i< array.length; i++) {
			if(array[i].compareTo(array[i-1]) < 0) { 
				return false;
			}
		}
		return true;
	}
}
