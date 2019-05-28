import java.io.File;

public class TextSearch {

	public static void main(String[] args) {
		TextSearch ts = new TextSearch();
		ts.listFiles("C:/Users/cohen/DataStructures/HW/2/textFiles");
		
		

	}
	
	public void listFiles(String path) {
		File dir = new File(path);
		File[] contents = dir.listFiles();
		checkContents(contents, 0);
	}

	private int checkContents(File[] contents, int index) {
		
		

		if(index > contents.length-1) {
			return 0;
		}
		if(contents[index].isFile()) {
			index++;
			checkContents(contents, index);
			return 0;
			
		}
	    
		if(contents[index].isDirectory()) {
			System.out.println(contents[index].getAbsolutePath());
			listFiles(contents[index].getAbsolutePath());
			index++;
			checkContents(contents, index);
			
			
		}
	
		
		return 1;
		
		
	}
	
	

}
