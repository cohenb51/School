package yu.pl.java.functional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ProcessorMain {

	private static boolean isWc(String[] args) {
		if (args[0].equals("wc"))
			return true;
		if (args.length > 4) {
			if (args[4].equals("wc")) {
				return true;
			}
		}
		return false;
	}
	
	private static boolean isGrep(String[] args) {
		return args[0].equals("grep");
		
	}

	private static String lower(String s) {
		return s.toLowerCase();
	}

	public static String filterLine(String s, String searchStr) {
		return (String.valueOf(Arrays.asList(new String("" + s).split("\n")).stream().filter(t -> t.contains(searchStr))
				.collect(Collectors.toList())).substring(1).replace("]", "").replace("\r, ", "\n"));
	}

	public static List<String> findWords(String s) {
		return Arrays.asList(s.replace("\n", " ").split(" "));
	}

	static boolean isAlpha(int c) {
		if (isNumber(c))
			return true;
		if ((c >= 97 && c <= 122))
			return true;

		if ((c >= 65 && c <= 90))
			return true;
		return false;
	}

	private static boolean isNumber(int c) {
		return (c <= 57 && c >= 48);
	}

	public static String removeNonAlphabetic(String string) {
		 return string.chars().filter(t -> isAlpha(t)).mapToObj(i -> (char) i).collect(Collectors.toList()).stream()
				.map(String::valueOf).collect(Collectors.joining()).trim();

	}

	public static boolean Empty(String string) {
		return !string.isEmpty();
	}


	private static String getFileTxt(String[] args) throws IOException {
		File file;

		if (args[0].equals("wc")) {
			file = new File(args[1]); // file always must be arg2
		} else {
			file = new File(args[2]);
		}
		@SuppressWarnings("resource")
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(char) file.length()]; // I need the first local
														// variable to see the
														// length
		fis.read(data);
		String str = new String(data, "UTF-8");
		return str;
	}

	@SuppressWarnings("unchecked")
	private static void printMap(Map map) {
		printMap( map, 0);
	}

	private static void printMap(Map<String, Integer> map, int i) {
		map.forEach((k, v) -> printEntry(k, v));

	}

	private static void printEntry(Object k, Object v) {
		System.out.println(k.toString() + " " + v.toString());
	}

	private static Object countWords(ArrayList<String> words) {
		return (Map<String, Integer>) words.stream()
				.collect(Collectors.toMap(Function.identity(), t -> seeNumber(t, words), (p2, p1) -> p1));

	}

	@SuppressWarnings("unchecked")
	private static Integer seeNumber(String t, @SuppressWarnings("rawtypes") ArrayList words) {

		return (Integer) (words.stream().reduce(0, (Object a, Object b) -> b.equals(t) ? (Integer) a + 1 : a));

	}
	
	public static Map doGrepAndWc(String string, String searchSring) {
		return  (Map)((Arrays
				.asList((Arrays.asList(string).stream().map(s -> filterLine(s, searchSring)) //When first done I thought it needed to be its own funtion. It is an antipattern but doesnt violate anything
						.map(s -> lower(s))// .// STEP 2. Yes I'm mapping over 1 element. to call to lower case (which you said on piazza is okay method)
						.map(i -> findWords(i)) // step 3 It's clearer what I'm doing like this anyways. 
						.collect(Collectors.toList()).get(0).stream().map(t -> removeNonAlphabetic((String) t)))
								.filter(t -> Empty((String) t)).collect(Collectors.toList()))
				.stream(). // step4
				map(t -> countWords((ArrayList<String>) t)).collect(Collectors.toList()).get(0)) // step// 5
				);
	}
	
	public static Map doWc(String string) {
		return  (Map)((Arrays
				.asList((Arrays.asList(string).stream()
						.map(s -> lower(s))// .// STEP 2
						.map(i -> findWords(i)) // step 3
						.collect(Collectors.toList()).get(0).stream().map(t -> removeNonAlphabetic((String) t)))
								.filter(t -> Empty((String) t)).collect(Collectors.toList()))
				.stream(). // step4
				map(t -> countWords((ArrayList<String>) t)).collect(Collectors.toList()).get(0)) // step// 5
				);
	}
	
	public static String doGrep(String string, String searchString) {
		return Arrays.asList(string).stream().map(s -> filterLine(s, searchString)).collect(Collectors.toList()).get(0).toString();
		
		
	}
	
	
	public static void main(String[] args) throws IOException {
		
		// honestly, when I first did this I thought you wanted free standing functions to do the jobs in the handout but it works and I don't think
		//I violated any requirements
		if(isWc(args) &&  isGrep(args)) {
			printMap(doGrepAndWc(getFileTxt(args),args[1]));
		}
		if(isWc(args) && !isGrep(args)){
			printMap(doWc(getFileTxt(args)));
		}
		if(!isWc(args) && isGrep(args)) {
			System.out.println(doGrep(getFileTxt(args), args[1]));
		}
		
	}

}
