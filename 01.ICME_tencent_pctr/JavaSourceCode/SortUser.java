package sortuser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class SortUser {
	private Data[] root = null;
	BufferedWriter bw = null;
	int writeCount = 0;

	public static void main(String[] args) {
		SortUser inst = new SortUser();
		inst.readAndSort("sortedusers.txt");
	}
	
	public Data[] readAndSort(String writeFileName) {
		boolean writeToFile = (writeFileName != null) && (!writeFileName.isEmpty());
		
    	try {
			// images name list
	//    	Path path = FileSystems.getDefault().getPath("uheads.txt");
	    	Path path = FileSystems.getDefault().getPath("users.txt");
	    	if (! Files.isReadable(path)) {
	    		System.out.println("File \"users.txt\" does not exist!");
	    		this.root = null;
	    		return null;
	    	}
	    	
	    	// Create read and write buffers
	        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
	        
	        root = new Data[10000];
	        
	        String line = br.readLine();
	        while (line != null && !line.isEmpty()) {
		        // first line as root
	        	Data node = new Data(line);
	        	int index = node.uid / 10000;
	        	if (root[index] == null) {
	        		root[index] = node;
	        	} else {
		        	Data p = root[index];
		        	while (true) {
		        		if (p.uid > node.uid) {
		        			if (p.left == null) {
		        				break;
		        			} else {
		        				p = p.left;
		        			}
		        		} else {
		        			if (p.right == null) {
		        				break;
		        			} else {
		        				p = p.right;
		        			}
		        		}
		        	}
		        	
	        		if (p.uid > node.uid) {
	        			p.left = node;
	        		} else {
	        			p.right = node;
	        		}
	        	}

        		// Read next line
        		line = br.readLine();
	        }
	        
	    	if (writeToFile) {
		    	// file for write
		    	Path path1 = FileSystems.getDefault().getPath(writeFileName);
		    	Files.deleteIfExists(path1);
		    	Files.createFile(path1);
		    	bw = Files.newBufferedWriter(path1, StandardCharsets.UTF_8);
		    	
		    	writeCount = 0;
		    	for (Data node: root) {
		    		if (node != null)
		    			writeTree(node);
		    	}
		        bw.flush();
		        bw.close();
	    	}
	    	
	        br.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return root;
	}
	
	private void writeTree(Data node) throws Exception{
		if (node.left != null) {
			writeTree(node.left);
		}
		StringBuffer sb = new StringBuffer().append(node.uid).append('\t').append(node.ageGender);
		bw.write(sb.toString());
		bw.newLine();
		
		if (++writeCount % 20 == 0) bw.flush();
		
		if (node.right != null) {
			writeTree(node.right);
		}
	}

	class Data {
		int uid;
		int ageGender;
		Data left = null;
		Data right = null;
		
		Data (String s) {
			String[] parts = s.split("\\s");
			int gender = 0;
			int age = 0;

			uid = Integer.parseInt(parts[0]);
			
			if (parts[1].isEmpty()) {
				if (parts[2].isEmpty()) {
					ageGender = 28; // no age, no gender;
				} else { // has age, no gender
					age = Integer.parseInt(parts[2]);
					if (age <= 15)
						ageGender = 132;
					else if (age >=65) {
						ageGender = 182;
					} else {
						ageGender = age + 117;
					}
				}
			} else if (parts[2].isEmpty()) { // has gender, no age
				if (parts[1].equals("1"))
					ageGender = 29;  // male
				else
					ageGender = 30;  // female
			} else {
				gender = Integer.parseInt(parts[1]);
				age = Integer.parseInt(parts[2]);
				if (age <= 15)
					ageGender = 31;
				else if (age >=65) {
					ageGender = 131;
				} else {
					ageGender = age * 2 + gender - 1;
				}
			}
		}
	}
}
