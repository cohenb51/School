package yu.pl.java.oop;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;

public class CountWords implements CountW {

	public LinkedHashMap<String, Integer> countWords(List<String> words) {
		LinkedHashMap<String,Integer> hs = new LinkedHashMap<>();

		for (int i = 0; i < words.size(); i++) {
			String word = words.get(i).trim();
			if (hs.containsKey(word)) {
				hs.put(word, (hs.get(word) + 1));
			}
			else {
				hs.put(word, 1);
			}
		}
		
		return hs;
	}

	@Override
	public Object process(Object obj) {
		return countWords((ArrayList<String>) obj);
	}


}
