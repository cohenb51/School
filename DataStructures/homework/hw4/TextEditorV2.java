/**
 * 
 * @author Benjamin Cohen
 * @version 1320.04.01
 *
 */

public class TextEditorV2 {
	private ArrayStack<ArrayStack<Character>> lines;
	private ArrayStack<Character> chars;
	int addLine;
	StringBuilder sb;
	ArrayStack<String> stringStack;

	public TextEditorV2() {
		lines = new ArrayStack<>();
		chars = new ArrayStack<Character>();
		addLine = 1;
	}

	/**
	 * Adds a char
	 * @param the char
	 */
	public void addChar(Character c) {
		if (addLine == 1) {
			lines.push(chars);
			addLine = 0;
		}
		chars.push(c);
	}

	/**
	 * Adds a new line
	 */
	
	public void newLine() {
		addLine = 1;
		chars = new ArrayStack<Character>();
	}

	/**
	 * Inserts a Char
	 * 
	 * @param c the char
	 * @param x the row
	 * @param y the position
	 */
	public void insertChar(Character c, int x, int y) {
		if(x >= this.lines.size()) {
			System.out.println("There is no line. " + x + "No changes");
			System.out.println();
			return;
		}
		ArrayStack<Character> line = findLine(x);
		
		if(y > line.size()) {
			System.out.println("There is no position " + y + " on line. " + x + "No changes");
			System.out.println();
			return;
		}
		
		
		ArrayStack<Character> temp = new ArrayStack<>();
		int placer = line.size() - y;
		for (int i = 0; i < placer; i++) {
			temp.push(line.pop());
		}

		line.push(c);
		for (int i = temp.size(); i != 0; i--) {
			line.push(temp.pop());
		}

	}

	/**
	 * Deletes a char
	 * 
	 * @param the row
	 * @param the position
	 */
	public void deleteChar(int x, int y) {
		if(x >= this.lines.size()) {
			System.out.println("There is no line " + x + ". No changes");
			System.out.println();
			return;
		}
		
		
		ArrayStack<Character> line = findLine(x);
		
		if(y >= line.size()) {
			System.out.println("There is no position " + y + "on line " + x + ". No changes");
			System.out.println();
			return;
		}
		ArrayStack<Character> temp = new ArrayStack<>();
		int placer = line.size() - y;
		for (int i = 1; i < placer; i++) {
			temp.push(line.pop());
			

		}

		line.pop();


		for (int i = temp.size(); i != 0; i--) {
			line.push(temp.pop());

		}

	}

	/**
	 * Deletes a line
	 * 
	 * @param the line
	 */

	public void deleteLine(int line) {
		if(line >= this.lines.size()) {
			System.out.println("There is no line " + line + ". No changes");
			System.out.println();
			return;
		}
		
		
		
		ArrayStack<ArrayStack<Character>> temp = new ArrayStack<>();
		int placer = lines.size() - line;
		for (int i = 1; i < placer; i++) {
			temp.push(lines.pop());

		}
		lines.pop();

		for (int i = temp.size(); i != 0; i--) {
			lines.push(temp.pop());
		}

	}

	/**
	 * Replaces a char
	 * 
	 * @param the char
	 * @param the row
	 * @param the position
	 */

	public void replaceChar(Character c, int x, int y) {
		if(x >= this.lines.size()) {
			System.out.println("There is no line " + x + ". No changes");
			System.out.println();
			return;
		}
		
		ArrayStack<Character> line = findLine(x);
		
		if(y >= line.size()) {
			System.out.println("There is no position " + y + "on line " + x + ". No changes");
			System.out.println();
			return;
		}
			
		ArrayStack<Character> temp = new ArrayStack<>();
		int placer = line.size() - y - 1;
		for (int i = 0; i < placer; i++) {
			temp.push(line.pop());
		}
		line.pop();
		line.push(c);
		for (int i = temp.size(); i != 0; i--) {
			line.push(temp.pop());
		}

	}

	/**
	 * Inserts a line
	 * 
	 * @param the row
	 * @param the position
	 */
	public void insertLine(int x, String s) {
		
		if(x >= this.lines.size() -1) {
			System.out.println("There is no line after " + x + ". No changes");
			System.out.println();
			return;
		}
		ArrayStack<Character> newLine = new ArrayStack<Character>();
		for (int i = 0; i < s.length(); i++) {
			newLine.push(Character.valueOf(s.charAt(i)));
		}

		ArrayStack<ArrayStack<Character>> temp = new ArrayStack<>();

		int numberToPop = lines.size() - x - 1;
		for (int i = 0; i < numberToPop; i++) {
			temp.push(lines.pop());
		}

		lines.push(newLine);

		while (temp.size() != 0) {
			lines.push(temp.pop());
		}
	}

	/**
	 * Reverses the lines
	 * 
	 */
	public ArrayStack<ArrayStack<Character>> ReverseLines() {
		ArrayStack<Character> tempLine = new ArrayStack<Character>();
		ArrayStack<ArrayStack<Character>> temp = new ArrayStack<>();
		int size = lines.size();
		for (int i = 0; i < size; i++) {
			tempLine = lines.pop();
			temp.push(tempLine);

		}

		lines = temp;
		chars = lines.peek();
		return temp;

	}

	/**
	 * Reverses the chars
	 * 
	 * @param the line to reverse
	 */
	public ArrayStack<Character> Reverse(int line) {
		if(line >= this.lines.size()) {
			System.out.println("There is no line " + line + ". No changes");
			System.out.println();
			return null;
		}
		ArrayStack<Character> lineToChange = findLine(line);
		ArrayStack<Character> temp1 = new ArrayStack<>();
		ArrayStack<Character> temp2 = new ArrayStack<>();
		Character tempChar = null;
		int size = lineToChange.size();
		for (int i = 0; i < size; i++) {
			tempChar = lineToChange.pop();
			temp1.push(tempChar);
		}

		for (int i = 0; i < size; i++) {
			temp2.push(temp1.pop());
		}

		for (int i = 0; i < size; i++) {
			lineToChange.push(temp2.pop());
		}

		return lineToChange;
	}

	private ArrayStack<Character> findLine(int line) {
		if (line > lines.size()) {
			System.out.println("problem");
		}
		ArrayStack<ArrayStack<Character>> temp = new ArrayStack<>();
		ArrayStack<Character> returnLine = null;
		int numberToPop = lines.size() - line - 1;
		for (int i = 0; i < numberToPop; i++) {
			temp.push(lines.pop());

		}

		returnLine = lines.peek();

		for (int i = temp.size(); i != 0; i--) {
			lines.push(temp.pop());
		}

		return returnLine;

	}

	private void printL(ArrayStack<Character> characters) {
		StringBuilder sb = new StringBuilder();
		int size = characters.size();
		ArrayStack<Character> temp = new ArrayStack<>();
		for (int i = 0; i < size; i++) {
			Character c = characters.pop();
			temp.push(c);
			sb.insert(0, Character.valueOf(temp.peek()));
		}

		for (int i = 0; i < size; i++) {
			characters.push(temp.pop());

		}
		System.out.print(sb);
	}

	public void Print() {
		ArrayStack<ArrayStack<Character>> temp = new ArrayStack<>();
		ArrayStack<ArrayStack<Character>> stack = ReverseLines();

		while (lines.size() != 0) {
			temp.push(stack.pop());
			printL(temp.peek());
			System.out.println();
		}

		for (int i = temp.size(); i != 0; i--) {
			stack.push(temp.pop());
		}
		ReverseLines();

	}

	public static void main(String[] args) {
		
	}

}
