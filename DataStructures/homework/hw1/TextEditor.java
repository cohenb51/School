/**
 * A 2d array that supports adding, inserting, replacing and deleting chars. It
 * can also insert and delete lines.
 * 
 * @author Benjamin Cohen
 * @version 1320.1.01
 */


public class TextEditor {

	private Character[][] text;
	private int currentLine;
	private int currentPosition;
	private int linesNull;
	private int linesFilled;

	public TextEditor() {
		this.text = new Character[1][1];
		this.currentLine = 0;
		this.currentPosition = 0;
		this.linesNull = 1;
		this.linesFilled = 0;
	}

	/**
	 * Adds a char
	 * @param c the char to add
	 */

	public void addChar(Character c) {

		if (this.currentPosition == this.text[this.currentLine].length) { 																																																		
			text[currentLine] = doubleSize(text[currentLine]);
		}

		int nullsInLine = nullsInLine(currentLine);
		if (nullsInLine == text[currentLine].length) { // if line is filled with
														// nulls before add
			linesNull--;
			linesFilled++;
		}
		text[currentLine][currentPosition] = c;
		currentPosition++; // so that next add will be in right position
	}

	/**
	 * Creates a newLine
	 */

	public void newLine() {
		if (nullsInLine(currentLine) == text[currentLine].length) {
			System.out.println("Line is already null. Nothing added"); // avoid
																		// scattering
																		// nulls
			return;
		}
		currentLine++;
		this.currentPosition = 0;
		if (currentLine == text.length) { // if this line will fill up the
											// array double it so there is
											// room to insert another array
											// after this one
			text = doubleSize(text);
		}

	}

	/**
	 * doubles the number of rows
	 */

	private Character[][] doubleSize(Character[][] oldArr) {
		Character[][] newArr = new Character[oldArr.length * 2][1];
		linesNull = linesNull + oldArr.length;
		for (int i = 0; i < oldArr.length; i++) {
			newArr[i] = oldArr[i];

		}
		return newArr;
	}

	/*
	 * doubles the number of positions
	 */
	
	private Character[] doubleSize(Character[] oldArr) {
		Character[] newArr = new Character[oldArr.length * 2];
		for (int i = 0; i < oldArr.length; i++) {
			newArr[i] = oldArr[i];
		}
		return newArr;
	}

	/**
	 * halves size of row OldArr
	 */
	
	private Character[] halveSize(Character[] oldArr) {

		Character[] newArr = new Character[oldArr.length / 2];
		for (int i = 0; i < oldArr.length / 2; i++) {
			newArr[i] = oldArr[i];

		}
		return newArr;
	}

	/**
	 * halves number of rows
	 */

	private Character[][] halveSize(Character[][] oldArr) {
		Character[][] newArr;

		linesNull = linesNull - oldArr.length / 2;
		newArr = new Character[oldArr.length / 2][];

		for (int i = 0; i < oldArr.length / 2; i++) {
			newArr[i] = oldArr[i];
		}

		return newArr;
	}

	/**
	 * Inserts a char in a given position
	 * 
	 * @param c the char
	 * @param x the row
	 * @param y the position
	 */

	public void insertChar(Character c, int x, int y) {

		if (text.length < x) {
			System.out.println("Line " + x + " doesn't exist. No changes made"); // no exceptions because no need to terminate
			return;
		}
		if (text[x].length < y) {
			System.out.println("Position " + y + " doesn't exist. No changes made");
		}
		if (text[x][y] == null) {
			System.out.println("Position" + x + "," + y + "is already null.");
		}

		if (!isEmptySpot(x)) { // if no empty spots
			text[x] = doubleSize(text[x]);
		}

		for (int i = text[x].length - 1; i > y; i--) {
			text[x][i] = text[x][i - 1]; // shift previous element right to current
											
		}
		if (currentLine == x) { // only increments position if on current line
			currentPosition++;
		}

		text[x][y] = c;
	}

	/**
	 * deletes the Char at given index
	 * 
	 * @param x  the row
	 * @param y  the position
	 */
	public void deleteChar(int x, int y) {
		if (text.length < x) {
			System.out.println("Line " + x + " doesn't exist. No changes made");
			return;
		}
		if (text[x].length < y) {
			System.out.println("Position " + y + " doesn't exist. No changes made");
			return;
		}
		if (text[x][y] == null) {
			System.out.println("Position" + x + "," + y + "is already null.");
			return;
		}

		if (currentLine == x) {
			currentPosition--;
		}

		for (int i = y; i < text[x].length - 1; i++) {
			text[x][i] = text[x][i + 1]; // shift next element leftward to current
		}

		text[x][text[x].length - 1] = null;
		
		

		if (nullsInLine(x) == text[x].length) { // if all nulls
			linesNull++;
			linesFilled--;
			
			if(currentLine == x && x != 0) {
				currentPosition = text[x-1].length - nullsInLine(x-1);
			}

			// note that if the line becomes null then all rows will have to move up

			if (currentLine != 0) { 
				currentLine--;
				

			} else { // this is the first line
				text[0] = new Character[1]; // so just reset it
				currentPosition = 0;
				return;
			}
			
			for (int k = x; k < text.length - 1; k++) {
				Character[] temp = text[k + 1];
				text[k] = temp; // move next array up to current.
				text[k + 1] = new Character[1]; // dereference next Array. If
												// last line sets it to null in
												// a new location
			}
			
		}
		
		
		if (text[x].length != 1) {
			checkPositionSize(x); // see if row needs to be cut
		}

		if (linesNull > linesFilled) {
			text = halveSize(text);
		}

	}

	/**
	 * Deletes the given line and shifts all lines below upwards to take its
	 * place
	 * 
	 * @param x the given line
	 */

	public void deleteLine(int x) {

		if (text.length < x) {
			System.out.println("Line " + x + " doesn't exist. No changes made.");
		}

		if (x == currentLine && currentLine != 0) {
			currentPosition = text[x - 1].length - nullsInLine(x - 1);
		}
		if (x > text.length) {
			System.out.println("Line " + x + "doesn't exist");
			return;
		}

		if (currentLine != 0) {
			currentLine--;
		} else {
			currentPosition = 0;
			text[0] = new Character[1];
			return;
		}
		
		if(currentLine == x && x!= 0) { 
			currentPosition = text[x-1].length - nullsInLine(x-1);
		}

		if (!(nullsInLine(x) == text[x].length)) { // if this line is filled
			linesNull++; // since getting rid of a full line
			linesFilled--;
		}

		for (int i = 0; i < text[x].length; i++) {
			text[x][i] = null;
		}

		for (int i = x; i < text.length - 1; i++) {
			Character[] temp = text[i + 1];
			text[i] = temp;
			text[i + 1] = new Character[1]; // dereference so objects don't
											// refer to same place
		}

		if (linesNull > linesFilled) {
			text = halveSize(text);
		}
		
		

	}

	/**
	 * Replaces a char at given index
	 * 
	 * @param c the char to put in
	 * @param x  the row
	 * @param y  the position
	 */

	public void replaceChar(Character c, int x, int y) {

		if (text.length < x) {
			System.out.println("Line" + x + " doesn't exist");
			return;
		}
		if (text[x].length < y) {
			System.out.println("Position" + x + ", " + y + "doesn't exist");
			return;
		}
		if (text[x][y] == null) {
			System.out.println("Position" + x + ", " + y + "is null");
			return;
		}
		text[x][y] = c;
	}

	/**
	 * Inserts a string into the array after a given index
	 * 
	 * @param x the place to insert
	 * @param s the string to insert
	 */

	public void insertLine(int x, String s) {
		if (x >= text.length) {
			System.out.println("line" + x + "doesn't exist. No changes made");
			return;
		}
		if (x == currentLine) {
			System.out.println("Only new line can add a newLine at the end of the document");
			return;
		}

		x = x + 1; // to insert after line instead of before

		currentLine++;
		if (currentLine == text.length) { // if this line will fill up the
			text = doubleSize(text);
		}

		if (linesFilled >= text.length) {
			text = doubleSize(text);
		}
		linesFilled++;
		linesNull--;
		for (int i = text.length - 1; i > x; i--) {
			Character[] temp = text[i - 1]; // shift
			text[i] = temp;
			text[i - 1] = new Character[1]; // dereference
		}
		text[x] = new Character[s.length()];
		for (int i = 0; i < text[x].length; i++) {
			(text[x][i]) = s.charAt(i); // fill line with string
		}
	}

	
	/*
	 *  Returns number of nulls in line
	 */
	
	private int nullsInLine(int x) {
		int nulls = 0;
		for (int i = 0; i < text[x].length; i++) {
			if (text[x][i] == null) {
				nulls++;
			}
		}
		return nulls;
	}
	
	/**
	 * Checks whether the lines need to be halved
	 * 
	 * @param line The line to check
	 */
	
	private void checkPositionSize(int line) {
		int empty = nullsInLine(line); // get number of nulls in line
		int full = text[line].length - empty;
		if (empty > full) {
			text[line] = halveSize(text[line]);
		}
	}

	/*
	 * Returns true if there is an empty spot
	 */

	private boolean isEmptySpot(int x) {
		for (int i = 0; i < text[x].length; i++) {
			if (text[x][i] == null) {
				return true;
			}

		}
		return false;
	}

	/**
	 * Returns the text Array. Used for testing
	 */
	
	public Character[][] getArray() {
		return text;
	}

	public static void main(String[] args) {

	}

}