package yu.pl.java.oop;

import java.awt.List;
import java.util.ArrayList;

public class NonAlphabeticFilter implements NonAlpha {
	
	
	
	
	
	public ArrayList<String> nonAlphabetFilter(ArrayList<String> stringL) {
		ArrayList<String> retList = new ArrayList<String>();
		
		for(int stringNum = 0; stringNum < stringL.size(); stringNum++) {
			String string = stringL.get(stringNum);
			StringBuilder sb = new StringBuilder();

		for(int i =0; i< string.length(); i++) {
			Character c = string.charAt(i);
			if(isAlpha(c)){
				sb.append(c);
			}
		}
		if(!sb.toString().trim().isEmpty()) {
		retList.add(sb.toString());
		}
		}

	
		return retList;
			
	}

	private boolean isAlpha(Character c) {
		 if(isNumber(c)) return true;
		 if ((c >= 97 && c <= 122)){
			 return true;
		 }
		 if((c >= 65 && c<= 90)) return true;
		  return false;
	}

	private boolean isNumber(Character c) {
		return (c <= 57 && c>= 48);
	}

	@Override
	public Object process(Object obj) {
		return nonAlphabetFilter( (ArrayList<String>) obj);
	}

	

}
