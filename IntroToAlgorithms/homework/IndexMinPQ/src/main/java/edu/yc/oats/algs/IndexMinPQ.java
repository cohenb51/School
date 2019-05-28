package edu.yc.oats.algs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;



public class IndexMinPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
	private int[] pq;
	private int[] qp; // index into pq
	final private Key[] keys;
	private int N = 0;
	
	public IndexMinPQ(int maxN) {
		keys = (Key[]) new Comparable[maxN+1];
		pq = new int[maxN+1];
		qp = new int[maxN +1];
		for(int i=0; i<maxN; i++ ) {
			qp[i] = -1;		
		}
	}
	
	public int IndexMinPQ(int maxN) {
		return maxN;
		
	}
	
	public void insert(int i, Key key) {
		if(key == null) {
			throw new IllegalArgumentException();
		}
		N++;
		qp[i] = N;
		pq[N] = i;
		keys[i] = key;
		swim(N); 

	}
	
	private void swim(int k) {
		// k tells us the priority we are looking at
		while ( k> 1 && less(k/2,k)) {
			exch(k/2,k); // swap the priorities. 
			k = k/2;
		}
	}
	
	private void sink(int k) {
		while(2*k <= N) {
			int j = 2 * k;
			if(j < N && less(j,j+1)) {
				j++;
			}
			if(!less(k,j)) {
				break;
			}
			exch(k,j);
			k = j;
		}		
	}
	
	private boolean less(int i, int j) { // less != less 
		return keys[pq[i]].compareTo(keys[pq[j]]) > 0; 
	
	}

	
	
	
	private void exch(int i, int j) {
		int p = pq[i]; // swap priorities. key array is constant. We just change indexes
		pq[i] = pq[j];
		pq[j] = p;   // but we need the inverse array to index into pq
		
		// qp[pq[i]] = pq[qp[i]] 
		int q = qp[pq[i]];
		qp[pq[i]] = qp[pq[j]]; 
		qp[pq[j]] = q;
		
		
		
		


		
	}

	public Key keyOf(int i) {
		
		return keys[i];
	}
	
	public int size() {
		return N;
	}
	
	public boolean isEmpty() {
		return N ==0;
	}
	
	public int delMin() {
		int indexOfMin = pq[1];
		exch(1,N--);
		sink(1);
		keys[pq[N+1]] = null;
		qp[pq[N+1]] = -1; // this is basically exange with nulls instead of valuse
		return indexOfMin;
	}
	
	public Key minKey() {
		return keys[pq[1]];
		
	}
	
	public int minIndex() {
		return pq[1];
		
	}
	
	public void changeKey(int i, Key key) {
		keys[i] = key;
		swim(qp[i]);
		sink(qp[i]);
		
	}
	
	public boolean contains(int i) {
		return qp[i] != -1;
	}
	
	public void delete(int i) {
		int index = qp[i];
		exch(index,N--);
		swim(index);
		sink(index);
		keys[i] = null;
		qp[i] = -1;
		
	}
	
	
		
		

	public static void main(String[] args) {
		IndexMinPQ pq = new IndexMinPQ<>(8);

		pq.insert(0,'M');
		
		pq.insert(1, 'D');
		pq.insert(2,'B');
		pq.insert(3, 'A');


		Iterator ary = pq.iterator();
		pq.insert(4,'C');
		pq.insert(5, 'G');
		pq.insert(6, 'F');
		pq.insert(7, 'E');
		//pq.changeKey(3, 'Z');
		//pq.delMin();
		Iterator<Integer> list = pq.iterator();
		System.out.println();
		
		while(list.hasNext()) {
			System.out.print(list.next());
		}
		System.out.println();
		System.out.println(pq.minKey());
		//pq.delMin();
		//System.out.println(pq.contains(4));
		//System.out.println(pq.size());
		

		

	
		

	/*	System.out.println(pq.delMin());
		System.out.println(pq.delMin());
		System.out.println(pq.delMin());*/
	

		



	}

	@Override
	public Iterator iterator() {
		ArrayList<Comparable> list = new ArrayList<Comparable>();
		int[] arr = pq.clone();
		for(int i =1; i< arr.length; i++) {
			Comparable temp = keys[pq[i]];
			Integer test = -1;
			System.out.println(keys[pq[i]]);
			if(temp !=  test && keys[pq[i]] != null)
			list.add(keys[pq[i]]);
		}
	 /*   System.out.println("pq");

		for(int i= 0; i < pq.length; i++) {

			System.out.print(pq[i]);
	}
		System.out.println();
		System.out.println("qp");*/
		
		
		
		/*for(int i= 0; i < pq.length; i++) {
			System.out.print(qp[i]);
	}
	*/
		
	return list.iterator();
	}

}