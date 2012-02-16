package scfg.output;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class RNAFormattedFile {

	private Map<String, String> map;
	private Display output;
	
	public String getFileName() {
		return output.getOutputFileName();
	}
	
	public RNAFormattedFile() {
		map = new HashMap<String, String>();
	}

	public RNAFormattedFile(String filename) {
		output = new Display(filename, null, false);
		map = new HashMap<String, String>();
	}

	public RNAFormattedFile(File rnaFile) {
		output = new Display(rnaFile.getAbsolutePath(), null, false);
		map = new HashMap<String, String>();
		parseRNAFormattedFile(rnaFile);
	}

	public boolean parseRNAFormattedFile(String filename) {
		return parseRNAFormattedFile(new File(filename));
	}

	public boolean parseRNAFormattedFile(File rnaFile) {
		try {
			Scanner scan = new Scanner(rnaFile);
			while (scan.hasNextLine()) {
				String[] arr = scan.nextLine().split(" :: ");
				if (arr.length == 2) {
					put(arr[0], arr[1]);
				}
			}
			scan.close();
			return true;
		} catch (FileNotFoundException e) {
		}
		return false;
	}

	public void put(String description, String value) {
		map.put(description, value);
	}

	public void update(String description, String newValue) {
		put(description, newValue);
	}

	public void remove(String key) {
		map.remove(key);
	}

	public String get(String key) {
		return map.get(key);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> me : map.entrySet()) {
			sb.append(me.getKey()).append(" :: ").append(me.getValue()).append(
					"\n");
		}
		return sb.toString();
	}

	public void writeToFile(boolean verbose) {
		output.setVerbose(verbose);
		output.out(this.toString());
		output.finalizeDisplay();
	}
}
