package edu.yc.oats.algs;

/**
 * An Alphabet class. Input a string of Characters belonging to some user
 * defined alphabet and they will be indexed based on order they are entered
 * from str[0] to str[R-1]
 * 
 * I choose to allow an empty alphabet as representative of an empty alphabet
 * Most of the methods in a case like that are nonsensical and will lead to
 * exceptions
 * 
 * @author Benjamin Cohen
 * @version 2545.01
 * 
 */

public class Alphabet {

	private int R; // the size
	private Character[] alphabet; // to get char with index
	private int[] index; // uses char value to map to the charIndex

	public Alphabet(String s) {
		if (s == null) {
			throw new IllegalArgumentException("s can't be null");
		}

		R = s.length();
		index = new int[Character.MAX_VALUE];
		alphabet = new Character[s.length()];

		if (s.length() == 0) { // empty set
			return;
		}

		for (int i = 0; i < s.length(); i++) {
			if (this.contains(s.charAt(i))) {
				throw new IllegalArgumentException("No Duplicates" + s.charAt(i) + "is repeated");
			}

			alphabet[i] = s.charAt(i);
			index[s.charAt(i)] = i; // that way access is constant although it wastes memory nowhere does it say I
									// can't do this

		}
	}

	/**
	 * 
	 * converts index to corresponding alphabet char
	 */

	public char toChar(int index) {
		if (index >= R || index < 0) {
			throw new IllegalArgumentException("index " + index + "not in range");
		}
		return alphabet[index];

	}

	/**
	 * converts c to an index between 0 and R()-1
	 */
	public int toIndex(char c) {
		if (R == 0) {
			throw new IllegalArgumentException("this alphabet is empty so " + c + "can't exist");
		}

		if (index[c] == 0 && alphabet[0] != c) { // It can only be 0 if it's the first char
			throw new IllegalArgumentException(c  + "doesnt exist");
		}
		return index[c];
	}

	/**
	 * Is c in the alphabet?
	 * 
	 * @param c the char
	 */
	public boolean contains(char c) {
		if (R == 0 || alphabet[0] == null) { // empty
			return false;
		}
		return !(index[c] == 0 && alphabet[0] != c); // reverse of the if statement in toIndex
	}

	/**
	 * Alphabet size
	 * 
	 */
	public int radix() {
		return R;
	}

	/**
	 * Number of bits to represent an index
	 */
	public int lgR() {

		if (R == 0) {
			return 0; // technically lg(0) is NAN but it requires 0 bits to represent an empty
						// alphabet
		}

		if (R == 1) {
			return 1;
		}

		int i = R;
		int counter = 0;
		while (i >= 2) {
			i /= 2;
			counter++;

		}
		if (Math.pow(2, counter) < R) {
			counter++;
		}

		return (int) counter;
	}

	/**
	 * Converts s to a base-R integer
	 * @param s the string
	 */

	public int[] toIndices(String s) {
		if(s == null) {
			throw new IllegalArgumentException("s can't be null");
		}
		int[] arr = new int[s.length()];
		for (int i = 0; i < s.length(); i++) {
			arr[i] = this.toIndex(s.charAt(i));
		}

		return arr;
	}

	/**
	 * Converts indices to a string
	 * 
	 * @param indices the indices
	 * 
	 */

	public String toChars(int[] indices) {
		if(indices == null) {
			throw new IllegalArgumentException("indices can't be null");
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indices.length; i++) {
			sb.append(this.toChar(indices[i]));
		}

		return sb.toString();
	}
	
	
	/*
	 * This shows how the class should be used
	 */
	
	public static void main(String[] args) {
		 Alphabet alphabet = new Alphabet("ABCD");
		 System.out.println(alphabet.radix());
		 System.out.println(alphabet.lgR());
		 System.out.println(alphabet.toChar(1));
		 System.out.println(alphabet.toIndex('A'));
		 int[] indices = new int[] {3 ,0 , 2};
		 System.out.println(alphabet.toChars(indices));
		 int[] result = alphabet.toIndices("BAD");
		 for(int i= 0; i< result.length; i++) {
			 System.out.print(result[i]);
		 }
		 System.out.println();
		 System.out.println(alphabet.contains('k'));
		 System.out.println();
		 
		

	}
}