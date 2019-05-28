import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/*
 * A simple class that searches a directory for a string
 * 
 * @author Benjamin Cohen
 * @version 1320.02.1
 * 
 * Notes:
 *  A tab is considered 4 spaces whether it is at the start or end of a line 
 *  based on a note on piazza that said a \t should 
 *  be considered more than 1 "space" and instead multiple spaces
 *  A \t entered as str will be considered the string "\t" but in a text editor
 *  oy\tvey would match with input oy    vey
 *  
 *  The character positioned returned isn't the character position since 
 *  a \t counts as 4 characters. If I misunderstood the note on piazza change the tabLength field 
 *  and delete lines  132-136
 *  
 *  Line 0 is the first line
 *  Character 0 is the first character.       
 */

public class TextSearch {
	private String dir;
	private String searchString;
	private String extension;
	private String currentFilePath;
	private int tabLength;

	/*
	 * Constructor Parses input and begins opening up directory
	 */

	public TextSearch(String[] args) {
		dir = searchString = extension = currentFilePath = "";
		tabLength = 4;
		parseInput(args, 0);
		listFiles(dir);

	}

	private void parseInput(String[] args, int index)
	{
		if (index > args.length - 1) {
			return;
		}

		switch (args[index].substring(0, 3)) {
		case "dir":
			dir = args[index].substring(4);
			break;
		case "str":
			searchString = args[index].substring(7);
			break;
		case "ext":
			extension = args[index].substring(10);
			break;
		}

		index++;
		parseInput(args, index);
		return;

	}

	public void listFiles(String Path)
	{
		File dire = new File(Path);
		File[] contents = dire.listFiles();
		checkContents(contents, 0);
	}

	private void checkContents(File[] contents, int index)
	{
		if (index > contents.length - 1) {
			return;
		}

		if (contents[index].isFile()) {
			if (contents[index].getAbsolutePath().endsWith(extension)) {
				prepReader(contents[index].getAbsolutePath());
			}
		}

		if (contents[index].isDirectory()) {
			listFiles(contents[index].getAbsolutePath()); // open up this
															// directory
		}

		index++;
		checkContents(contents, index);
		return;
	}

	/*
	 * Opens up a BufferedReader to search for string
	 */

	private void prepReader(String file)
	{
		currentFilePath = file;
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			findString(br, 1, "", -1, 0);
			br.close();
		} catch (IOException e1) {
			System.out.println("Error processing file while preping reader");
		}
	}

	/*
	 * Searches for given string
	 */

	private void findString(BufferedReader br, int endOfLine, String currentLine, int lineNumber, int placer)
	{
		if (endOfLine == 1) {
			try {
				currentLine = br.readLine();
				lineNumber++;
			} catch (IOException e1) {
				System.out.println("Error processing file while finding string");
			}
			endOfLine = 0;
		}

		if (currentLine == null) {
			return;
		}
		for(int i = 0; i < currentLine.length(); i++) { //last minute change before I knew we were allowed loops
			if(currentLine.charAt(i) == '\t') {
			String partA = currentLine.substring(0, i);
			String partB = currentLine.substring(i+1);  
			currentLine = partA + "    " + partB;  //changes a tab into 4 spaces
					
			}
		}
		
		int index = currentLine.indexOf(searchString);
		
		for(int i = 0; i < currentLine.length() && index!= -1; i++) { //last minute change before I knew we were allowed loops
			if(currentLine.charAt(i) == '\t') {
					index += tabLength;
			}
		}

		if (index != -1) {
			System.out.println(currentFilePath + ":line " + lineNumber + ", character " + (int) (placer + index));
			placer = placer + index + 1;
			findString(br, 0, currentLine.substring(index + 1), lineNumber, placer);

		} else {
			findString(br, 1, "", lineNumber, 0);
		}
	}

	public static void main(String[] args)
	{
		new TextSearch(args);
	}

}