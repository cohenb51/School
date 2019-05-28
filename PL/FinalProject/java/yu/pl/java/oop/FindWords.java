package yu.pl.java.oop;
import java.util.ArrayList;

public class FindWords implements FindW {
	
	public ArrayList<String> findWords(String string) {

		ArrayList<String> list = new ArrayList<String>();
		int start = 0;
		for(int i = 0; i< string.length(); i++) {
			if(Character.isWhitespace(string.charAt(i))) {
				list.add(string.substring(start, i));
				start = i;
			}
		}
		list.add(string.substring(start, string.length()));
		return list;	
	}
	
	/*public static void main(String[] args) {
		String str = "A LOT OF WORK AND LittLE timE";
		//System.out.println(process(str));
		
		
	}*/

	@Override
	public Object process(Object obj) {
		return findWords((String) obj);
	}

	

}
