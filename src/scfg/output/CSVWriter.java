package scfg.output;

import java.io.*;
import java.util.*;

public class CSVWriter {

	private static Map<String, BufferedWriter> writers;
	private static Display output;
	
	static {
		writers = new HashMap<String, BufferedWriter>();
		output = new Display("logs/CSVWriter.log", "logs/CSVWriter_error.log", true);
	}
	
	public static void addWriter(String filename){
		if(filename != null && filename.length() > 0)
			try {
				writers.put(filename, new BufferedWriter(new FileWriter(filename.endsWith(".csv") ? filename : filename + ".csv")));
			} catch (IOException e) {
				output.eout("Cannot Create File Writer: " + filename);
			}
	}
	
	public static void finalizeWriter(String writer){
		if(writer != null && writers.get(writer) != null)
			try {
				writers.get(writer).close();
			} catch (IOException e) {
			}
	}

	public static void append(String writer, String[] contents) {
		if(writer == null || writers.get(writer) == null || contents == null || contents.length == 0){
			output.eout("Nothing to write to CSV");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < contents.length; i++) {
			sb.append("\"").append(contents[i]).append("\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		append(writer, sb.toString());
	}

	public static void append(String writer, List<String> contents) {
		if(writer == null || writers.get(writer) == null || contents == null || contents.size() == 0){
			output.eout("Nothing to write to CSV");
			return;
		}
		StringBuilder sb = new StringBuilder();
		for (String s : contents) {
			sb.append("\"").append(s).append("\",");
		}
		sb.deleteCharAt(sb.length() - 1);
		append(writer, sb.toString());
	}

	public static void append(String writer, String contents) {
		if(writer == null || writers.get(writer) == null || contents == null || contents.length() == 0){
			output.eout("Nothing to write to CSV");
			return;
		}
		BufferedWriter bf = writers.get(writer);
		try{
		bf.write(contents);
		bf.write("\n");
		} catch (IOException e){
			output.eout("There was an error writing to file: " + writer +
					"\n\tThe writer may already be closed.");
		}
	}
}
