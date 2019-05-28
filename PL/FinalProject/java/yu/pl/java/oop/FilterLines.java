package yu.pl.java.oop;

import java.util.ArrayList;

public class FilterLines implements FilterL {
	public static String searchString;

	public FilterLines(String string) {
		this.searchString = string;
	}

	static boolean restOfWordMatches(String string, String searhString, int offSet) {
		if (string.length() < 0) {
			throw new Error("HOW IS THIS EVEN POSSIBLE.");
		}

		int searchStringCounter = 0;
		if (string.length() < searchString.length())
			return false;
		for (int i = offSet; searchStringCounter < searchString.length(); i++, searchStringCounter++) {
			if (string.charAt(i) != searhString.charAt(searchStringCounter)) {
				return false;
			}

		}
		return true;
	}

	protected ArrayList<String> splitIntoLines(String string) {
		ArrayList<String> retList = new ArrayList<String>();
		int start = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '\r') {
				if (string.charAt(i) == '\n') {
					retList.add(string.substring(0, i));
					start = i + 1;
					i = i + 1;
				}
			}
			if (string.charAt(i) == '\n') {
				retList.add(string.substring(start, i));
				start = i;
			}
		}
		retList.add(string.substring(start, string.length()));
		return  retList;

	}

	/*
	 * Filters a given input string into lines, seperated with terminators and a
	 * space
	 */
	public String filterLines(String string) {
		StringBuilder sb = new StringBuilder();
		//String lines[] = string.split("\\r?\\n");//todo ask if we could use split		// https://stackoverflow.com/questions/9059483/splitting-string-in-case-of-a-new-line
		ArrayList<String> lines = splitIntoLines(string);								
		boolean found = false;
		//todo ask someone if we can use contain
		for (int i = 0; i < lines.size(); i++) {
			for (int lineCounter = 0; lineCounter < lines.get(i).length(); lineCounter++) {
				if (lines.get(i).charAt(lineCounter) == searchString.charAt(0) && !found) {
					if (restOfWordMatches(lines.get(i), searchString, lineCounter)) {
						found = true;
						break;
					}
				}
			}
			if (found) { // At end of line
				sb.append(lines.get(i));
				sb.append(" ");
			}
			found = false;
		}
		if (sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString(); // get rid of the last space
	}

	@Override
	public Object process(Object obj) {
		return filterLines((String) obj);
	}

}
