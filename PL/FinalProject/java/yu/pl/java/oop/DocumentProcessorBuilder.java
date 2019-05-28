package yu.pl.java.oop;

import java.util.LinkedHashSet;
import java.util.LinkedHashMap;



public class DocumentProcessorBuilder  {

	private ConvertCase convertCase;
	private CountWords countWords;
	private FilterLines filterLines;
	private FindWords findWords;
	private NonAlphabeticFilter nonAlphabeticFilter;

	public void setConvertCase(ConvertCase convertCase) {
		this.convertCase = convertCase;
	}

	public void setCountWords(CountWords countWords) {
		this.countWords = countWords;
	}

	public void setFindWords(FindWords findWords) {
		this.findWords = findWords;
	}

	

	public void setFilterLines(FilterLines filterLines) {
		this.filterLines = filterLines;
	}

	public void setNonAlphabeticFilter(NonAlphabeticFilter nonAlphabeticFilter) {
		this.nonAlphabeticFilter = nonAlphabeticFilter;
	}

	public DocumentProcesor build() {
		return new DocumentProcesor(this);

	}

	public static class DocumentProcesor {
		LinkedHashSet<TextTask> tasks = new LinkedHashSet<TextTask>();
		private DocumentProcesor(DocumentProcessorBuilder builder) {
			/*this.convertCase = builder.convertCase;
			this.countWords = builder.countWords;
			this.filterLines = builder.filterLines;
			this.findWords = builder.findWords;
			this.nonAlphabeticFilter = builder.nonAlphabeticFilter;*/
			tasks = new LinkedHashSet<>();
			tasks.add(builder.filterLines);
			tasks.add(builder.convertCase);
			tasks.add(builder.findWords);
			tasks.add(builder.nonAlphabeticFilter);
			tasks.add(builder.countWords);
			
		}

		public LinkedHashMap process(String textFile) {

			Object temp = textFile;
			for(TextTask task: tasks) {
				if(task!= null) {
				temp = task.process(temp);
			}
			}
			if (!(temp instanceof LinkedHashMap)) {
				String str = new String((String)temp);
				 temp = new LinkedHashMap();
                      ((LinkedHashMap) temp).put(str,0);
			}
			return (LinkedHashMap) temp;


		}

	}

}
