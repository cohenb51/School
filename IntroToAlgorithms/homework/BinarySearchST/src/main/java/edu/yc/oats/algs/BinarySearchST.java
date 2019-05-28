package edu.yc.oats.algs;

import java.util.LinkedList;
import java.util.Queue;

public class BinarySearchST<Key extends Comparable<Key>, Value> {

	private Key[] keys;
	private Value[] vals;
	private int N;

	public BinarySearchST() {
		keys = (Key[]) new Comparable[2];
		vals = (Value[]) new Object[2];

	}

	public BinarySearchST(Key[] initialKeys, Value[] initialValues) {
		if(initialKeys.length != initialValues.length) {
			throw new IllegalArgumentException("each key must have a value");
		}
		
		keys = (Key[]) new Comparable[initialKeys.length * 2];
		vals = (Value[]) new Object[initialKeys.length * 2];
		for (int i = 0; i < initialKeys.length; i++) {
			put(initialKeys[i], initialValues[i]);
			//System.out.println(N + "n");
		}

	}

	public BinarySearchST(int capacity) {
		keys = (Key[]) new Comparable[capacity];
		vals = (Value[]) new Object[capacity];
	}

	public int size() {
		return N;
	}
	
	public int size(Key lo, Key hi) {
		if(!contains(lo) || !contains(hi)) {
			return -1;
		}
		int loplacer = rank(lo);
		int hiplacer = rank(hi);
		int size = 0;
		
		while(loplacer <= hiplacer) {
			size++;
			loplacer++;
			
		}
		
		return size;
		
		
	}

	public Value get(Key key) {
		if( key == null) {
			throw new IllegalArgumentException("no nulls in table");
		}
		if (isEmpty()) {
			return null;
		}
		int i = rank(key);
		if (i < N && keys[i].compareTo(key) == 0) {
			return vals[i];
		} else {
			return null;
		}
	}

	public boolean isEmpty() {
		if (N == 0) {
			return true;
		}
		return false;
	}

	public int rank(Key key) {
		int lo = 0;
		int hi = N - 1;
		while (lo <= hi) {
			int mid = lo + (hi - lo) / 2;
			int cmp = key.compareTo(keys[mid]);
			if (cmp < 0) {
				hi = mid - 1;
			} else if (cmp > 0) {
				lo = mid + 1;
			} else {
				return mid;
			}
		}
		return lo;

	}

	public void put(Key key, Value val) {

		if (key == null) {
			throw new IllegalArgumentException("key to put can't be null");
		}
		if (val == null) {
			delete(key);
			return;
		}
		if (N == keys.length) {
			doubleKeys();
		}

		int i = rank(key);

		if (i < N && keys[i].compareTo(key) == 0) {
			vals[i] = val;
			return;
		}
		for (int j = N; j > i; j--) {
			keys[j] = keys[j - 1];
			vals[j] = vals[j - 1];
		}
		keys[i] = key;
		vals[i] = val;
		N++;
	}

	/*
	 * Deletes an element by finding where it is the swapping everything right left
	 */
	public void delete(Key key) {

		if (!contains(key)) {
			return;
		}

		int placer = rank(key); // where are we
		// now shift everything to the left till we get to placer

		for (int i = placer; i < N - 1; i++) {
			keys[i] = keys[i + 1];
			vals[i] = vals[i + 1];
		}
		N--;
		
		keys[N] = null; // get rid of last shifted
		vals[N] = null;
		
		//if(N * 4 < keys.length ) {
		//halve(); // maybe not needed since chances are the same number of keys will be needed again
		

	}
	
	public void deleteMin() {
		if(N == 0) {
			return;
		}
		delete(min());
	}
	
	public void deleteMax() {
		if(N ==0) {
			return;
		}
		delete(max());
	}

	public Key min() {
		return keys[0];
	}

	public Key max() {
		return keys[N - 1];
	}

	public Key select(int k) {
		return keys[k];

	}

	
	// smllest key > or equal to 
	public Key ceiling(Key key) {
		if(key == null) {
			throw new IllegalArgumentException("null keys aren't allowed");
		}
		if(N == 0) {
			return null;
		}
		
		int i = rank(key);
		return keys[i];

		
	}

	// largest key less than or equal to
	public Key floor(Key key) {
		if(key == null) {
			throw new IllegalArgumentException("null keys aren't allowed");
		}
		if(N == 0) {
			return null;
		}
		
		int placer = rank(key);
		
		if(placer > N) {
			return keys[N-1];
		}
		if(key.equals(keys[placer])) { // we are equal
			return key;
		}

		if (placer == 0) {
			return null; // no such key exists
		}
		
		

		return keys[placer - 1];

		// TODO
	}

	public Iterable<Key> keys(Key lo, Key hi) {
		Queue<Key> q = new LinkedList<Key>();
		for (int i = rank(lo); i < rank(hi); i++) {
			q.add(keys[i]);
		}
		if (contains(hi)) {
			q.add(keys[rank(hi)]);
		}

		return q;

	}

	public Iterable<Key> keys() {
		Queue<Key> q = new LinkedList<Key>();
		for (int i = rank(keys[0]); i < rank(keys[N - 1]); i++) {
			q.add(keys[i]);
		}
		if (contains(keys[N - 1])) {
			q.add(keys[rank(keys[N - 1])]);
		}

		return q;

	}
	
	

	public boolean contains(Key key) {
		
		return get(key) != null;

	}

	public void doubleKeys() {

		Key[] tempKeys = (Key[]) new Comparable[N * 2];
		Value[] tempValue = (Value[]) new Comparable[N * 2];
		for (int i = 0; i < N; i++) {
			tempKeys[i] = keys[i];
			tempValue[i] = vals[i];
		}

		keys = tempKeys;
		vals = tempValue;

	}
	
	public void halve() {
		Key[] tempKeys = (Key[]) new Comparable[N / 2];
		Value[] tempValue = (Value[]) new Comparable[N / 2];
		for (int i = 0; i < N; i++) {
			tempKeys[i] = keys[i];
			tempValue[i] = vals[i];
		}

		keys = tempKeys;
		vals = tempValue;
		
		
		
	}
	
	private Key[] getKeys(){
		return keys;
	}

	public static void main(String args[]) {
		BinarySearchST<Character, Integer> bsst = new BinarySearchST<>();
		bsst.put('c', 8);
		bsst.put('e', 2);
		bsst.put('a', 3);
		bsst.put('d', 5);
		bsst.put('f', 4);
		bsst.put('b', 6);
		bsst.put('g', 2);
		bsst.put('i', 3);
		bsst.put('a', 9);
		System.out.println(bsst.floor('h'));
		
		
		Iterable<Character> b = bsst.keys();
		for(Character c: b) {
			System.out.print(c);
		}
		System.out.println();
		int size = bsst.size();
		Object[] keys = bsst.getKeys();
		for(int i=0; i< size; i++) {
			System.out.print(bsst.get((Character) keys[i]));
		}
		System.out.println();
		System.out.println(size);
		System.out.println();
		
		bsst.delete('c');
		bsst.delete('a');
		bsst.delete('i');
		System.out.println(bsst.size());
		
		for(int i=0; i< keys.length; i++) {
			System.out.print(keys[i]);
		}
		
		System.out.println();
	    b = bsst.keys();
		for(Character c: b) {
			System.out.print(c);
		}
		System.out.println();
		size = bsst.size();
		keys = bsst.getKeys();
		for(int i=0; i< size; i++) {
			System.out.print(bsst.get((Character) keys[i]));
		}
		//System.out.println();
		//System.out.println(bsst.floor(('b')));
		
		
		BinarySearchST bsst2 = new BinarySearchST<>();
		System.out.println();
		System.out.println(bsst2.get('b') + "test");
		
		bsst2.put('b', 3);
		System.out.println(bsst2.get('b'));
		
		

		Character[] test = { 'h', 'a', 'm', 'b', 'y' };
		Integer[] testV = { 12, 235, 54, 1, 3 };
		
		BinarySearchST<Character, Integer> bs = new BinarySearchST<>(test, testV);
		b = bs.keys();
		for(Character c: b) {
			System.out.print(c);
		}
		System.out.println();
		size = bs.size();
	    keys = bs.getKeys();
		for(int i=0; i< size; i++) {
			System.out.print(bs.get((Character) keys[i]));
		}
		bs.put('a', null);
		bs.put('b', null);
		bs.put('h', null);
		bs.delete('m');
		   
		size = bs.size();
		System.out.println(size);
	    keys = bs.getKeys();
		for(int i=0; i< size; i++) {
			System.out.print(keys[i]);
		}
		System.out.println();
		for(int i=0; i< size; i++) {
			System.out.print(bs.get((Character) keys[i]));
		}
		System.out.println();
		System.out.println(bs.floor('z'));
		
		
		
		
		

		
	}
	
	

}
