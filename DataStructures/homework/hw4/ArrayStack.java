/**
 * 
 * @author Benjamin Cohen
 * @version 1320.04.01
 *
 */

public class ArrayStack<T> {

	private int top;
	private Object[] stack;
	private int size;

	public ArrayStack() {
		top = 0;
		size = 0;
		stack = new Object[1];
	}

	/**
	 * Pushes element onto the stack
	 * 
	 * @param element the element
	 */
	public void push(T element) {
		if (top <= stack.length - 1) {
			stack[top] = element;
			top++;
			size++;
		} else {
			stack = doubleStack();
			push(element);
		}

	}

	private Object[] doubleStack() {
		Object[] newStack = new Object[size * 2];
		for (int i = 0; i < size; i++) {
			newStack[i] = stack[i];
		}
		return newStack;
	}

	/**
	 * Pops off the top of the stack and returns the popped item
	 * 
	 */
	public T pop() {
		if (size == 0) {
			return null;
		}
		top--; // we are getting rid of element below the top
		@SuppressWarnings("unchecked") // the array is made of type T
		T item = (T) stack[top];
		stack[top] = null;
		size--;
		return (T) item;

	}

	/**
	 * Returns Top element
	 */
	@SuppressWarnings("unchecked")
	public T peek() {
		return (T) stack[top - 1];

	}

	/**
	 * Returns the size of the stack
	 */
	public int size() {
		return size;
	}

	public static void main(String[] args) {
		ArrayStack<Character> as = new ArrayStack<Character>();
		as.push('a');
		as.push('b');
		as.push('c');
		as.push('d');
		as.pop();
		as.push('d');
		as.push('e');

		as.printStack();

	}

	private void printStack() {
		for (int i = stack.length - 1; i >= 0; i--) { // used for testing
			System.out.println(stack[i]);
		}
	}

	@SuppressWarnings("unchecked")
	private T peekAt(int index) { // used for testing
		return (T) stack[index];
	}

}
