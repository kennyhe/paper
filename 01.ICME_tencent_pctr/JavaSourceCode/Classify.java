
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Classify {
	
	private List<Keyword> keywords = null;
	private String[] categories = null;
	
	public static void main(String[] args) {
		
		try {
		
			Classify inst = new Classify();
			inst.getKeywords();
			
	    	// images name list
	    	Path path = FileSystems.getDefault().getPath("titles.txt");
	    	if (! Files.isReadable(path)) {
	    		System.out.println("File \"titles.txt\" does not exist!");
	    		return;
	    	}
	    	
	    	// file for write
	    	Path path1 = FileSystems.getDefault().getPath("titles.classify");
	    	Files.deleteIfExists(path1);
	    	Files.createFile(path1);
	    	// Create read and write buffers
	        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
	        BufferedWriter bw = Files.newBufferedWriter(path1, StandardCharsets.UTF_8);
			
	        inst.classifyByTitle(br, bw);
	        
	        br.close();
	        bw.close();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void classifyByTitle(BufferedReader br, BufferedWriter bw) throws Exception {
		String line = null;
		int row = 0;
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\\s");

			if (parts.length < 2) continue;

			String id = parts[0];
			String title = parts[1];
			int[] kwExist = new int[keywords.size()];
			int[] countCategory = new int[categories.length];
			
			for (int i = 0; i < keywords.size(); i++) {
				Keyword kw = keywords.get(i);
				if (title.indexOf(kw.keyword) >= 0) {
					kwExist[i] = 1;
					for (int j = 0; j < categories.length; j++) {
						countCategory[j] += kw.inCategory[j];
					}
				} else {
					kwExist[i] = 0;
				}
			}
			
			bw.write(id);
			for (int i = 0; i < keywords.size(); i++) {
				bw.write('\t');
				bw.write('0' + kwExist[i]);
			}
			
			for (int i = 0; i < categories.length; i++) {
				bw.write('\t');
				bw.write('0' + countCategory[i]);
			}
			
			bw.newLine();
			
			row++;
			
			if (row % 20 == 0) bw.flush();
			
		}
		bw.flush();
	}
	
	private void getKeywords() {
		
		try {
		
			// Read the keywords config
	    	Path path = FileSystems.getDefault().getPath("keywords.txt");
	    	if (! Files.isReadable(path)) {
	    		System.out.println("File \"keywords.txt\" does not exist!");
	    		return;
	    	}
	
	    	BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
	    	
	    	// init list
	    	if (keywords == null) {
	    		keywords = new ArrayList<Keyword>();
	    	} else {
	    		keywords.clear();
	    	}
	    	String line = br.readLine();
	    	categories = line.split("\\s");
	    	
	    	while ((line = br.readLine()) != null) {
	    		keywords.add(new Keyword(line));
	    	}
	    	
	    	br.close();
    	
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	
	class Keyword {
		public String keyword;
		private int[] inCategory = null;
		
		Keyword(String line) {
			String[] parts = line.split("\\s");
			keyword = parts[0];
			inCategory = new int[categories.length];
			for (int i = 1; i < parts.length; i++) {
				inCategory[i - 1] = Integer.parseInt(parts[i]);
			}
		}
	}
}


