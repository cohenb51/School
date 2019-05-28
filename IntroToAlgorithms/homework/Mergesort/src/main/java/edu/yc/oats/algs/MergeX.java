package edu.yc.oats.algs;

public class MergeX implements Mergesorter {

	MergeX() {
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
		if ((hi - lo <= 9)) {
			insertionSort(a, lo, hi);
			return;

		}
		int mid = lo + (hi - lo) / 2;
		mergeSort(a, lo, mid, aux);
		mergeSort(a, mid + 1, hi, aux);

	

		if (!(a[mid].compareTo(a[mid + 1]) < 0)) {
			merge(a, lo, mid, hi, aux);
		}

	}

public static Comparable[] insertionSort(Comparable[] a, int lo, int hi) {
	int N = hi - lo +1;
	for(int i= lo +1; i < N + lo; i++) {
	
		for(int j =i; (j> lo && a[j].compareTo(a[j-1]) < 0); j--) {
			Comparable t = a[j];
			a[j] = a[j-1];
			a[j-1] = t;
			
		}
	}
	
	return a;
}

	private static Comparable[] merge(Comparable[] a, int lo, int mid, int hi, Comparable[] aux) {
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


}
