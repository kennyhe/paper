//package keyword;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AnalysisKeywords {
	
	private KeyCounterComparator<String> strKeyComp = new KeyCounterComparator<String>();
	private KeyCounterComparator<Character> charKeyComp = new KeyCounterComparator<Character>();
	private int LOW_FREQ_COUNT = 5;
	
	public static void main(String[] args) {
		
		try {
		
			AnalysisKeywords inst = new AnalysisKeywords();
			
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
	    	// file for write
	    	Path path2 = FileSystems.getDefault().getPath("title_keywords.txt");
	    	Files.deleteIfExists(path2);
	    	Files.createFile(path2);
	    	// Create read and write buffers
	        BufferedReader br = Files.newBufferedReader(path, StandardCharsets.UTF_8);
	        BufferedWriter bw = Files.newBufferedWriter(path1, StandardCharsets.UTF_8);
	        BufferedWriter bw2 = Files.newBufferedWriter(path2, StandardCharsets.UTF_8);
			
	        inst.statByWord(br, bw, bw2);
	        
	        br.close();
	        bw.close();
	        bw2.close();
	    	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private void statByWord(BufferedReader br, BufferedWriter bw, BufferedWriter bw2) throws Exception {
		String line = null;
		
		
		Map<String, KeyCounter<String>> titleCount = new HashMap<String, KeyCounter<String>>();
		Map<Character, KeyCounter<Character>> charCount = new HashMap<Character, KeyCounter<Character>>();
		
		// Statistics the count of the titles and characters
		while ((line = br.readLine()) != null) {
			String[] parts = line.split("\\s");
			
			if (parts.length < 2) continue; // If no title, go to next
			
			String title = parts[1];
			
			KeyCounter<String> ks = titleCount.get(title);
			if (ks == null) {
				titleCount.put(title, new KeyCounter<String>(title));
			} else {
				ks.incCount();
				continue; // If dup title, go to next.
			}
			
			// Add keywords count in the dictionary
			for (char ch: title.toCharArray()) {
				KeyCounter<Character> kc = charCount.get(ch);
				if (kc == null) {
					charCount.put(ch, new KeyCounter<Character>(ch));
				} else {
					kc.incCount();
				}
			}
		}
		
		ArrayList<KeyCounter<Character>> kcList = new ArrayList<KeyCounter<Character>>(charCount.values());
		ArrayList<KeyCounter<String>> titleList = new ArrayList<KeyCounter<String>>(titleCount.values());
		
		Collections.sort(kcList, charKeyComp);
		writeValues(kcList, bw, "# single characters:");
		
		// Remove those characters which only occur once
		removeLowOccurrence(kcList);
		
		ArrayList<KeyCounter<String>> wordList = new ArrayList<KeyCounter<String>>();
		for (KeyCounter<Character> c1: kcList) {
			for (KeyCounter<Character> c2: kcList) {
				// combine two character into a word
				StringBuffer s = new StringBuffer().append(c1.getKey()).append(c2.getKey());
				int count = 0;

				for (KeyCounter<String> titleKC : titleList) {
					if (titleKC.getKey().contains(s)) {
						count++;
					}
				}
				
				if (count > 0) {
					KeyCounter<String> ks = new KeyCounter<String>(s.toString(), count);
					wordList.add(ks);
					ks.left = c1;
					ks.right = c2;
				}
			}
		}
		
		Collections.sort(wordList, strKeyComp);
		writeValues(wordList, bw, "# 2-char words:");
		
		boolean hasLongerWord = removeLowOccurrence(wordList);
		ArrayList<ArrayList<KeyCounter<String>>> dict = new ArrayList<ArrayList<KeyCounter<String>>>();
		dict.add(wordList);

		int wordLen = 3;
		while (hasLongerWord) {
			ArrayList<KeyCounter<String>> oldWordList = wordList; 
			wordList = new ArrayList<KeyCounter<String>>();
			Set<String> words = new HashSet<String>();
			
			// Combine minWordLen-char words
			int subWordLen = wordLen - 2;
			for (KeyCounter<String> kcs1: oldWordList) {
				for (KeyCounter<String> kcs2: oldWordList) {
					if (kcs1.getKey().substring(1).equals(kcs2.getKey().substring(0, subWordLen))) {
						// combine two words into a single word
						String s = kcs1.getKey().substring(0, 1) + kcs2.getKey();
						
						// skip duplicated combined words
						if (words.contains(s))
							continue;
						
						words.add(s);
						
						int count = 0;
						for (KeyCounter<String> titleKC : titleList) {
							if (titleKC.getKey().contains(s)) {
								count++;
							}
						}
						
						if (count > 0) {
							KeyCounter<String> ks = new KeyCounter<String>(s.toString(), count);
							wordList.add(ks);
							ks.left = kcs1;
							ks.right = kcs2;
						}
					}
				}
			}
			
			Collections.sort(wordList, strKeyComp);
			writeValues(wordList, bw, "# " + wordLen + "-char words:");
			
			hasLongerWord = removeLowOccurrence(wordList);
			dict.add(0, wordList); // Add to the head

			if (hasLongerWord) {
				wordLen++;
			}
		}
		
		int row = 0;
		// Write keywords statistics results to bw2
		for (ArrayList<KeyCounter<String>> wList: dict) {
			for (KeyCounter<String> ks : wList) {
				updateCounter(ks);

				if (ks.getCount() > LOW_FREQ_COUNT) {
					StringBuffer sb = new StringBuffer();
					sb.append(ks.getKey()).append('\t').append(ks.getCount());
					bw2.write(sb.toString());
					bw2.newLine();
					
					row++;
					if (row % 20 == 0) bw2.flush();
				}
			}
		}
		
		// Write characters statistics results to bw2
		for (KeyCounter<Character> kc : kcList) {
			if (kc.getCount() > LOW_FREQ_COUNT) {
				StringBuffer sb = new StringBuffer();
				sb.append(kc.getKey()).append('\t').append(kc.getCount());
				bw2.write(sb.toString());
				bw2.newLine();
				
				row++;
				if (row % 20 == 0) bw2.flush();
			}
		}
		bw2.flush();
	}
	
	
	// Update the counter: substract the count of current word from all its sub words
	private void updateCounter(KeyCounter<String> ks) {
                if (ks.left.left != null) {
			updateCounter(ks.left, ks.getCount(), false);
			updateCounter(ks.right, ks.getCount(), true);
		}
                ks.left.decCountBy(ks.getCount());
                ks.right.decCountBy(ks.getCount());
	}
	
	
	private void updateCounter(KeyCounter ks, int c, boolean rightMost) {
		if (ks.left.left != null) {
			updateCounter(ks.left, c, false);
			updateCounter(ks.right, c, rightMost);
		}
		
		ks.left.decCountBy(c);
		if (rightMost) ks.right.decCountBy(c);
	}
	
	// Remove the low occurence keys from the list (to reduce the complexity)
	private<T> boolean removeLowOccurrence(ArrayList<KeyCounter<T>> counterList, int count) {
		if (counterList.isEmpty())
			return false;
		
		if (counterList.get(0).getCount() <= count) { // If all keys occurs no more than "count" times
			counterList.clear();
			return false;
		}
		
		int i = counterList.size() - 1;
		while (counterList.get(i).getCount() <= count) {
			counterList.remove(i);
			i--;
		}
		
		return true;
	}

	
	// Remove the low occurrence keys from the list (to reduce the complexity)
	private<T> boolean removeLowOccurrence(ArrayList<KeyCounter<T>> counterList) {
		return removeLowOccurrence(counterList, LOW_FREQ_COUNT);
	}
	

	private<T> void writeValues(ArrayList<KeyCounter<T>> counterList, BufferedWriter bw, String caption) throws Exception{
		int row = 0;
		
		// Write caption
		bw.write(caption);
		bw.newLine();

		for (KeyCounter<T> kc : counterList){
			StringBuffer sb = new StringBuffer();
			sb.append(kc.getKey()).append('\t').append(kc.getCount());
			bw.write(sb.toString());
			bw.newLine();
			
			row++;
			
			if (row % 20 == 0) bw.flush();
			
		}
		bw.flush();
	}
	
	
	class KeyCounter<T> {
		private T key;
		private int count;
		public KeyCounter left = null;
		public KeyCounter right = null;
		
		public KeyCounter(T k) {
			key = k;
			count = 1;
		}
		
		public KeyCounter(T k, int c) {
			key = k;
			count = c;
		}
		
		public void incCount() {
			count++;
		}
		
		public T getKey() {
			return key;
		}
		
		public int getCount() {
			return count;
		}
		
		public void decCountBy(int c) {
			count -= c;
		}
	}
	
	class KeyCounterComparator<T> implements Comparator<KeyCounter<T>> {
		@Override
		public int compare(KeyCounter<T> kc1, KeyCounter<T> kc2) {
			// Sort in descending order
			return kc2.getCount() - kc1.getCount();
		}
	}
}


