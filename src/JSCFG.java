import java.io.*;
import java.util.*;
import java.util.Map.Entry;

import desposito6.math.*;
import desposito6.utils.CommandLine;
import scfg.*;
import scfg.output.*;
import scfg.utils.RnaFileHandler;


public class JSCFG {
	
	private static Display output;
	
	static {
		output = new Display("JSCFG");
	}
	
	public static Grammar processParameters(String filename){
		if (filename == null)
			output.ethrow("IllegalArgumentException: Null parameter filename provided");
		File param = new File(filename);
		if (!param.exists() || param.isDirectory())
			output.ethrow("IllegalArgumentException: Invalid or non-existing parameter file provided.");
		Map<String, iDouble> params = new HashMap<String, iDouble>();
		Grammar g = null;
		try {
			Scanner scan = new Scanner(new File(filename));
			String line = scan.nextLine();
			String sep = null;
			String[] kh_headers = new String[] {"S->LS","S->L","L->s","L->dFd","F->LS","F->dFd"};
			String[] pfold_headers = new String[] {"s->a","s->c","s->g","s->u",
					"ff->aa","ff->ac","ff->ag","ff->au","ff->ca","ff->cc","ff->cg","ff->cu",
					"ff->ga","ff->gc","ff->gg","ff->gu","ff->ua","ff->uc","ff->ug","ff->uu"};
			if (line.contains("="))
				sep = "=";
			if (line.contains(":")){
				if (sep == null)
					sep = ":";
				else
					output.ethrow("");
			}
			if (sep != null) {
				String[] arr = line.split(sep);
				if (arr.length == 2) {
					params.put(arr[0].trim(), new MyBigDecimal(arr[1].trim()));
				} else
					output.ethrow("");
				while (scan.hasNext()){
					arr = scan.nextLine().split(sep);
					if (arr.length == 2) {
						params.put(arr[0].trim(), new MyBigDecimal(arr[1].trim()));
					} else
						output.ethrow("");
				}
				for(int i=0;i<kh_headers.length;i++)
					if (params.get(kh_headers[i]) != null)
						kh_headers[i] = null;
				for(int i=0;i<pfold_headers.length;i++)
					if (params.get(pfold_headers[i]) != null)
						pfold_headers[i] = null;
				
			} else {
				for(int i=0;i<kh_headers.length;i++) {
					params.put(kh_headers[i], new MyBigDecimal(scan.nextLine().trim()));
					kh_headers[i] = null;
				}
				for(int i=0;i<pfold_headers.length;i++) {
					params.put(pfold_headers[i], new MyBigDecimal(scan.nextLine().trim()));
					pfold_headers[i] = null;
				}
			}
			StringBuilder kh_missingParams = new StringBuilder();
			for(String s : kh_headers)
				if (s != null)
					kh_missingParams.append(s).append(", ");
			StringBuilder pfold_missingParams = new StringBuilder();
			int missingPfoldCount = 0;
			for(String s : pfold_headers){ 
				if (s != null) {
					pfold_missingParams.append(s).append(", ");
					missingPfoldCount++;
				}
			}
			if (kh_missingParams.length() == 0 && missingPfoldCount == pfold_headers.length) {
				g = new KHGrammar();
				g.setProbabilities(params);
			} else if (kh_missingParams.length() == 0 && pfold_missingParams.length() == 0) {
				g = new PfoldGrammar();
				g.setProbabilities(params); 
			} else {
				kh_missingParams.append(pfold_missingParams);
				kh_missingParams.deleteCharAt(kh_missingParams.length() - 2);
				output.ethrow("The following parameters were missing in the provided file. Please be sure to check for correct syntax.\n\n" + 
						kh_missingParams.toString());
			}
		} catch (IOException e) { 
			
		} catch (ArrayIndexOutOfBoundsException e) {
			
		}
		return g;
	}
	
	private static void displayResults(String filename, Map<String, iDouble> results) {
		StringBuilder sb = new StringBuilder(Display.box(filename, "\t"));
		sb.append("\n\n");
		
		sb.append(Display.underline("Probability: " + results.get("prob"), ""));
		sb.append("\n\n");
		
		for (Entry<String, iDouble> me : results.entrySet()) {
			if (!me.getKey().equals("prob"))
				sb.append(me.getKey()).append("\t").append(me.getValue()).append("\n");
		}
		
		output.out(sb.toString());
	}
	
	private static boolean checkFileName(String filename) {
		return filename.endsWith(".ct") || filename.endsWith(".nopct") || filename.endsWith("rna");
	}
	
	private static void processFile(File file, Grammar g){
		if (file == null || g == null || !file.exists() || !file.isFile()) {
			output.eout("IllegalArgumentException: Invalid or non-existing scoring file was provided.");
			return;
		}
		if (!checkFileName(file.getName())) {
			output.eout("IllegalArgumentException: Invalid scoring file format was provided. (Accepts: \".ct\", \".nopct\" or \".rna\"");
			return;
		}
		RNAFormattedFile rnaff = null;
		if (file.getName().endsWith(".rna"))
			rnaff = new RNAFormattedFile(file);
		else
			rnaff = RnaFileHandler.convertCtFile(file, new RNAFormattedFile());
		if (rnaff != null && rnaff.get("seq") != null && rnaff.get("nat") != null) {
			if (rnaff.get("seq").matches("[ACGU]+")) {
				Map<String, iDouble> results = g.scoreRNAFileWithStats(rnaff.get("seq"), rnaff.get("nat"));
				displayResults(file.getName(), results);
			} else {
				output.eout("An ambiguous sequence was provided: " + file.getAbsolutePath());
				return;
			}
		} else {
			output.eout("Invalid data format within the file, could not parse the structure: " + file.getAbsolutePath());
			return;
		}
	}
	
	private static void processFile(String filename, Grammar g) {
		processFile(new File(filename), g);
	}
	
	private static void processDir(File base, Grammar g){
		if (base == null || !base.exists() || !base.isDirectory())
			output.ethrow("IllegalArgumentException: Invalid or non-existing scoring directory was provided.");
		Queue<File> dirs = new LinkedList<File>();
		for(File f : base.listFiles()) {
			if (f.isDirectory())
				dirs.add(f);
			else if (f.isFile() && checkFileName(f.getName()))
				processFile(f, g);
		}
		while (!dirs.isEmpty())
			processDir(dirs.poll(), g);
	}
	
	private static void processDir(String dirname, Grammar g){
		processDir(new File(dirname), g);
	}
	
	private static void displayParametersHelp() {
		CommandLineMenu cm = new CommandLineMenu("JSCFG Parameters");
		StringBuilder sb = new StringBuilder("The grammar probabilities should be listed one per line. ");
		sb.append("They can be provided with labels (ex. \"S->L=1.0\" and \"L->dFd=0.123\") or the probabilities ");
		sb.append("can be listed in the following order each on a single line: {S->L, S->LS, L->s, L->dFd, F->LS, ");
		sb.append("F->dFd, s->a, s->c, s->g, s->u, ff->aa, ff->ac, ff->ag, ff->au, ff->ca, ff->cc, ff->cg, ff->cu, ");
		sb.append("ff->ga, ff->gc, ff->gg, ff->gu, ff->ua, ff->uc, ff->ug, ff->uu}.");
		cm.setDescription(sb.toString());
		sb = new StringBuilder("The data structure used for calculation provides 128bits of precision which supports about 33 base-10 digits. ");
		sb.append("You can specify only the Knudsen Hein Grammar parameters or Pfold Grammar parameters as well.");
		cm.setComments(sb.toString());
		output.out(cm.toString());
	}
	
	private static void displayHelp(){
		CommandLineMenu cm = new CommandLineMenu("JSCF Main");
		cm.setDescription("Uses probabilities to score RNA secondary structures. Last Edited 08-18-2011.");
		cm.add("-h", "Display the h(elp) text");
		cm.add("-v", "Display v(erbose) output.");
		cm.add("-o", "Specify o(utput) logfile. Default \"log/JSCFG.log\"");
		cm.add("-p", "Specify p(arameters) file. Enter \"-h parameters\" for more information about formatting.");
		cm.add("-i", "Specifies i(nput) file. Accepts \".ct\", \".nopct\" and \".rna\".");
		cm.add("-d", "Specifies a source d(irectory). Each valid file in this directory will be scored. The directory is explored recursively.");
		cm.setComments("Contact David Esposito with any questions or problems. desposito6[at]gatech.edu");
		output.out(cm.toString());
	}
	
	public static void processHelp(int index, String[] args) {
		if(index < 0 || args == null)
			throw new IllegalArgumentException();
		if (index + 1 < args.length && args[index + 1].equals("parameters"))
			displayParametersHelp();
		else 
			displayHelp();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int temp = Math.max(CommandLine.containsArg(args, "-h"), CommandLine.containsArg(args, "-help"));
		if(temp >= 0) {
			processHelp(temp, args);
			System.exit(0);
		}
		boolean verbose = Math.max(CommandLine.containsArg(args, "-v"), CommandLine.containsArg(args, "-verbose")) > 0;
		temp = Math.max(CommandLine.containsArg(args, "-o"), CommandLine.containsArg(args, "-output"));
		if (temp >= 0 && temp + 1 < args.length)
			output = new Display(args[temp], args[temp] + "_error.log", verbose);
		String params = null;
		temp = Math.max(CommandLine.containsArg(args, "-p"), CommandLine.containsArg(args, "-parameters"));
		if (temp >= 0 && temp + 1 < args.length)
			params = args[temp + 1];
		String input = null;
		temp = Math.max(CommandLine.containsArg(args, "-i"), CommandLine.containsArg(args, "-input"));
		if (temp >= 0 && temp + 1 < args.length)
			input = args[temp + 1];
		String directory = null;
		temp = Math.max(CommandLine.containsArg(args, "-d"), CommandLine.containsArg(args, "-directory"));
		if (temp >= 0 && temp + 1 < args.length)
			directory = args[temp + 1];
		if (params != null) {
			Grammar g = processParameters(params);
			if (input != null)
				processFile(input, g);
			else if (directory != null)
				processDir(directory, g);
		} else 
			output.eout("You did not provide a parameter file. There are currently no default probabilties.");
	}

}
