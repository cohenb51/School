package yu.pl.java.oop;

public class ConvertCase implements ConvertC {
	
	protected static char chartoLowerCase(char c) {
		if(c  <= 90 && c >= 65) {//https://www.cs.cmu.edu/~pattis/15-1XX/common/handouts/ascii.html
			return (char) (c + 32);
		}
		return c;
	}
	
	public String convertCase(String string) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < string.length(); i++) {
			sb.append(chartoLowerCase(string.charAt(i)));
		}
		return sb.toString();
			
	}
	
	// test 
	/*public static void main(String[] args) {
		String str = "I HAVE TOO MUCH WORK. IM GOING TO FAIL EVERYTHING";
		//System.out.println(process(str));
	}*/

	@Override
	public Object process(Object obj) {
		return convertCase((String) obj);
	}

}
