package desposito6.utils;

import java.io.*;
import java.util.*;

public class lines {
	public static int count = 0;
	public static String[] extensions;
	public static BufferedWriter bf;
	private static boolean verbose;

	static {
		count = 0;
		verbose = true;
		extensions = new String[] { ".java", ".cpp", ".hpp", ".c", ".h" };
	}

	private static void display(String s) {
		if (verbose)
			System.out.println(s);
		if (bf != null) {
			try {
				bf.write(s);
				bf.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void finalizeDisplay() {
		if (bf != null) {
			try {
				bf.close();
			} catch (IOException e) {
				bf = null;
				e.printStackTrace();
			}
		}
		bf = null;
	}

	private static int containsFlag(String[] args, String arg) {
		if (args == null || arg == null)
			return -1;
		for (int i = 0; i < args.length; i++)
			if (args[i].equals(arg))
				return i;
		return -1;
	}

	private static void displayHelp() {
		System.out.println("\n[ desposito6.utils.lines.java ]");
		System.out.println("-------------------------------");
		System.out
				.println("This class explores the specified directory recursively.");
		System.out
				.println("It will count the total number of lines in each file with an included");
		System.out
				.println("file extension. Other file extensions are ignored. Custom extensions lists");
		System.out
				.println("can be specified with the \"-i\" command line argument. Included file");
		System.out.println("extension packages:\n");
		System.out
				.println("\tDefault: { \".java\", \".cpp\", \".hpp\", \".c\", \".h\" }");
		System.out.println("\tJava: { \".java\", \".form\"}");
		System.out.println("\tScripting: { \".pl\", \".py\"}");
		System.out
				.println("\tWeb: { \".html\", \".htm\", \".css\", \".js\", \".php\", \".ini\", \".jsp\", \".gsp\" }");
		System.out.println("\tText: { \".txt\"}");
		System.out
				.println("\nThe total number of lines will be listed for each file and the sum of all");
		System.out
				.println("the contained files will be displayed for the directory.\n");
		System.out.println("Command Line Args:");
		System.out.println("------------------");
		System.out.println("\t-v \t\tTurn on verbose output.");
		System.out.println("\t-h \t\tDisplay help.");
		System.out
				.println("\t-d [dir] \tRoot directory to recursively search.");
		System.out
				.println("\t-i [ext1,...]\t(Optional) File extension to include. Comma separated.");
		System.out
				.println("\t-o [dir] \t(Optional) Output file. Where to save the results.");
		System.out.println("\nExtension Packages:");
		System.out.println("-------------------");
		System.out
				.println("\t-java \tParses \".java\" and related file extensions.");
		System.out
				.println("\t-scripting \tParses \".pl\", \".py\" and related file extensions.");
		System.out
				.println("\t-web \tParses \".html\" and related file extensions.");
		System.out
				.println("\t-text \tParses \".txt\" and related file extensions.");
		System.out.println("\n\n");
	}

	/*
	 * args[0] = directory name args[1] = comma seperated file extenstions
	 */
	public static void main(String[] args) {
		String base;

		// help
		if (containsFlag(args, "-h") >= 0 || containsFlag(args, "-help") >= 0) {
			displayHelp();
			return;
		}

		// output file
		int temp = containsFlag(args, "-o");
		if (temp >= 0 && temp < args.length - 1) {
			try {
				bf = new BufferedWriter(new FileWriter(args[temp + 1]));
			} catch (IOException e) {
				System.out
						.println("Could not write to file: " + args[temp + 1]);
			}
		}

		// verbose
		verbose = containsFlag(args, "-v") >= 0;

		// root directory
		temp = containsFlag(args, "-d");
		if (temp >= 0 && temp < args.length - 1)
			base = args[temp + 1];
		else
			base = ".";

		// included extensions
		temp = containsFlag(args, "-i");
		if (temp >= 0 && temp < args.length - 1)
			extensions = args[temp + 1].split(",");
		if (containsFlag(args, "-java") >= 0)
			extensions = new String[] { ".java", ".form" };
		if (containsFlag(args, "-scripting") >= 0)
			extensions = new String[] { ".pl", ".py" };
		if (containsFlag(args, "-web") >= 0)
			extensions = new String[] { ".html", ".htm", ".css", ".js", ".php",
					".ini", ".jsp", ".gsp" };
		if (containsFlag(args, "-text") >= 0)
			extensions = new String[] { ".txt" };

		try {
			File dir = new File(base);
			if (!dir.exists() || !dir.isDirectory())
				throw new Exception();
			StringBuilder sb = new StringBuilder("\n\n");
			sb.append(processDir(dir, "", sb)).append("------\n\nTotal: ")
					.append("\n\n\n");
			display(sb.toString());
		} catch (Exception e) {
		}
		finalizeDisplay();
	}

	public static int processDir(File dir, String path, StringBuilder sb) {
		List<File> dirs = new ArrayList<File>();
		List<File> files = new ArrayList<File>();
		File[] contents = dir.listFiles();
		StringBuilder fsb = new StringBuilder();
		for (File f : contents) {
			if (f.getName().length() > 2) {
				if (f.isDirectory())
					dirs.add(f);
				else
					files.add(f);
			}
		}
		int total = 0;
		for (File f : files) {
			int num = processFile(f);
			total += num;
			if (num > 0)
				fsb.append((path.equals("") ? dir.getName() + "/" : path))
						.append(f.getName()).append(": \t").append(num).append(
								"\n");
		}
		if (total > 0)
			fsb.append("\n");
		for (File f : dirs)
			total += processDir(f, path + dir.getName() + "/", sb);
		if (total > 0)
			sb.append(fsb.toString()).append(path).append(dir.getName())
					.append(":\t").append(total).append("\n");
		return total;
	}

	public static boolean checkExtension(String filename) {
		for (String ext : extensions)
			if (filename.endsWith(ext))
				return true;
		return false;
	}

	public static int processFile(File f) {
		if (checkExtension(f.getName().toLowerCase())
				&& f.getName().indexOf("svn") < 0) {
			try {
				Scanner scan = new Scanner(f);
				int count = 0;
				while (scan.hasNextLine()) {
					scan.nextLine();
					count++;
				}
				return count;
			} catch (Exception e) {
			}
		}
		return 0;
	}
}
