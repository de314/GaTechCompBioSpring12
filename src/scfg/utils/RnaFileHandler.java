package scfg.utils;

import java.io.*;
import java.util.*;

import scfg.Grammar;
import scfg.RNAobj;
import scfg.output.Display;
import scfg.output.RNAFormattedFile;

public class RnaFileHandler {
	
	public static String parseFastaFile(File f) {
		try {
			Scanner scan = new Scanner(f);
			StringBuilder sb = new StringBuilder();
			scan.nextLine();
			while(scan.hasNextLine()) {
				sb.append(scan.nextLine().trim());
			}
			if (sb.toString().matches("[acguACGU]+"))
				return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	
	public static void writeCTFile(String filename, String seqStr, String predStr) {
		int[] pairs = Grammar.getPairsArraySt(predStr);
		char[] seq = seqStr.toCharArray();
		Display out = new Display(filename, null, false);
		out.outC(pairs.length + "\t").out(filename);
		for(int i=0;i<pairs.length;i++) {
			int n = i+1;
			out.outC(n+"\t").outC(seq[i]+"\t").outC(i+"\t");
			out.outC((n+1)+"\t").outC((pairs[i]+1)+"\t").out(n+"\t");
		}
		out.finalizeDisplay();
	}
	
	public static RNAFormattedFile convertCtFile(File f) {
		return convertCtFile(f, new RNAFormattedFile());
	}

	public static RNAFormattedFile convertCtFile(File f,
			RNAFormattedFile rnaFile) {
		if (!f.exists()) {
//			output.eout("File does not exist");
			return null;
		}
		if (!f.getAbsolutePath().replace("\n", "").endsWith("nopct")) {
//			output.eout("Invalid Filename");
			return null;
		}
		try {
//			output.out("\tConverting...");
			Scanner scan = new Scanner(f);
			String line = scan.nextLine();
			if (line.contains("html"))
				return null;
			String[] arr = line.split("\\s+");
			if (!arr[1].equals("1")) {
				scan.nextLine();
				scan.nextLine();
				scan.nextLine();
				scan.nextLine();
			}
			StringBuilder structure = new StringBuilder();
			StringBuilder sequence = new StringBuilder();
			recurseOnBP(scan, sequence, structure);
			rnaFile.put("seq", sequence.toString());
			rnaFile.put("nat", structure.toString());
			return rnaFile;
		} catch (Exception e) {
//			output.eout(f.getAbsolutePath());
//			output.eoutST(e);
		}
		return null;
	}
	
	public static RNAFormattedFile convertCtFile(Scanner scan,
			RNAFormattedFile rnaFile) {
		try {
//			output.out("\tConverting...");
			String line = scan.nextLine();
			if (line.contains("html"))
				return null;
			String[] arr = line.split("\\s+");
			if (!arr[1].equals("1")) {
				scan.nextLine();
				scan.nextLine();
				scan.nextLine();
				scan.nextLine();
			}
			StringBuilder structure = new StringBuilder();
			StringBuilder sequence = new StringBuilder();
			recurseOnBP(scan, sequence, structure);
			rnaFile.put("seq", sequence.toString());
			rnaFile.put("nat", structure.toString());
			return rnaFile;
		} catch (Exception e) {
//			output.eout(f.getAbsolutePath());
//			output.eoutST(e);
		}
		return null;
	}
	
	public static void recurseOnBP(Scanner scan,
			StringBuilder sequence, StringBuilder structure) {
		while (scan.hasNext()) {
			String line = scan.nextLine().trim();
			String[] arr = line.split("\\s+");
			int count = Integer.parseInt(arr[0]);
			sequence.append(arr[1]);
			if (arr[4].equals("0"))
				structure.append(".");
			else {
				Integer i = Integer.parseInt(arr[4]);
				if (count < i)
					structure.append("(");
				else
					structure.append(")");
			}
			count++;
		}
	}
}
