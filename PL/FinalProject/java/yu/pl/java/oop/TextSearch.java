package yu.pl.java.oop;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import yu.pl.java.oop.DocumentProcessorBuilder.DocumentProcesor;

public class TextSearch {
	String filePath;
	DocumentProcessorBuilder dpb;
	String text;
	boolean isWc = false;

	public TextSearch(String[] args) throws IOException {
		
		dpb = new DocumentProcessorBuilder();
		setBuildByParsing(args);
		DocumentProcesor dp = dpb.build();
		
		LinkedHashMap map = dp.process(text);
			for(Object a: map.keySet()){
			System.out.print(a);
			if(isWc) System.out.println(" " + map.get(a));
		
		}
				
		
		
	}

	private void setBuildByParsing(String[] args) throws IOException {
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("grep")) {
				dpb.setFilterLines(new FilterLines(args[i + 1]));
				i++;
			}
			if (args[i].contains(".txt")) {
				setFileString(args[i]);
			}

			if (args[i].equals("wc")) {
				{
					setwc();
					isWc = true;
				}
			}
		}
	}

	private void setFileString(String string) throws IOException {
		FileReader fileReader = new FileReader(string);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		StringBuilder sb = new StringBuilder();	
	    while(1 == 1) {
			String line = bufferedReader.readLine();
        	if(line == null) break;
			sb.append(line);
			sb.append("\n");
            } 
	    text = sb.toString();
	}
	
	
	private void setwc() {
		dpb.setNonAlphabeticFilter(new NonAlphabeticFilter());
		dpb.setCountWords(new CountWords());
		dpb.setConvertCase(new ConvertCase());
		dpb.setFindWords(new FindWords());
	}

}
