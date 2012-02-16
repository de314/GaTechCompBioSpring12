package scfg.output;

import java.io.*;

import desposito6.math.BigDouble;

public class Display implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1130293337763783215L;

	private String outputFileName, outputErrName;
	private BufferedWriter bf, bferr;
	private boolean verbose;

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public Display(String className) {
		this(className, true);
	}

	public Display(String className, boolean verbose) {
		this("logs/" + className + ".log", "logs/" + className + "_error.log",
				verbose);
	}

	public Display(String outputName, String errorName, boolean verbose) {
		this.outputFileName = outputName;
		this.outputErrName = errorName;
		this.verbose = verbose;
	}

	public String getOutputFileName() {
		return outputFileName;
	}

	public void setBufferedWriter(BufferedWriter bfwtr) {
		bf = bfwtr;
	}

	public static void checkDir(String filename) {
		File f = new File(filename);
		File dir = new File(f.getAbsolutePath().replace(f.getName(), ""));
		if (!dir.exists())
			dir.mkdir();
	}

	private boolean checkWriter() {
		if (outputFileName == null && bf == null)
			return false;
		if (bf == null) {
			try {
				checkDir(outputFileName);
				bf = new BufferedWriter(new FileWriter(outputFileName));
				return true;
			} catch (IOException e) {
				eout("The program cannot write to " + outputFileName);
				e.printStackTrace();
			}
		} else
			return true;
		return false;
	}

	public void setErrLog(String filename) {
		outputErrName = filename;
	}

	public String getErrLog() {
		return outputErrName;
	}

	public void setErrWriter(BufferedWriter bfwtr) {
		bferr = bfwtr;
	}

	private boolean checkErrLog() {
		if (outputErrName == null && bferr == null)
			return false;
		if (bferr == null) {
			try {
				checkDir(outputErrName);
				bferr = new BufferedWriter(new FileWriter(outputErrName));
				return true;
			} catch (IOException e) {
				eout("The program cannot write to " + outputErrName);
				e.printStackTrace();
			}
		} else
			return true;
		return false;
	}

	public void finalizeDisplay() {
		if (bf != null)
			try {
				bf.close();
			} catch (IOException e) {
				eout("The filewriter was already closed.");
				e.printStackTrace();
			}
		bf = null;
		if (bferr != null)
			try {
				bferr.close();
			} catch (IOException e) {
				eout("The filewriter was already closed.");
				e.printStackTrace();
			}
		bferr = null;
	}
	
	public void flushDisplay() {
		if (bf != null)
			try {
				bf.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	public Display out(String s) {
		if (verbose)
			System.out.println(s);
		if (checkWriter()) {
			try {
				bf.write(s);
				bf.write("\n");
			} catch (IOException e) {
			}
		}
		return this;
	}

	public Display outC(String s) {
		if (verbose)
			System.out.print(s);
		if (checkWriter()) {
			try {
				bf.write(s);
			} catch (IOException e) {
			}
		}
		return this;
	}
	
	public Display console(String s){
		if(verbose)
			System.out.println(s);
		return this;
	}
	
	public void ethrow(String s) {
		eout(s);
		System.exit(0);
	}

	public Display eout(String e) {
		System.err.println(e);
		if (checkErrLog()) {
			try {
				bferr.write(e);
				bferr.write("\n");
			} catch (IOException ex) {
			}
		}
		return this;
	}

	public Display eoutST(Exception e) {
		e.printStackTrace();
		if (checkErrLog()) {
			try {
				StackTraceElement[] ste = e.getStackTrace();
				for (int i = 0; i < ste.length; i++) {
					bferr.write(ste[i].toString());
					bferr.write("\n");
				}
			} catch (IOException ex) {
			}
		}
		return this;
	}

	public static String buf(String s, int buf, char c) {
		if (s.length() >= buf)
			s = s.substring(0, buf - 2);
		StringBuilder sb = new StringBuilder(s);
		while (sb.length() < buf)
			sb.insert(0, c);
		return sb.toString();
	}

	/**
	 * Prints a 3D array of doubles. This should only be used for debugging.
	 * Expected size of array is arr[size][size][3].
	 * 
	 * @param arr
	 *            - 3D array to be printed
	 * @param size
	 *            - The size of the first two dimensions. The trird is assumed
	 *            to be 3.
	 */
	public static String print_3D_Array(BigDouble[][][] arr) {
		String[] nt = { "S", "L", "F" };
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < arr[0][0].length; k++) {
			sb.append("(" + nt[k] + ")\n");
			for (int i = 0; i < 2/* arr.length */; i++) {
				for (int j = 0; j < arr[0].length; j++) {
					sb.append("(" + i + "," + j + "," + k + ","
							+ arr[i][j][k].toString() + "\t");
				}
				sb.append("\n");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	


	public static String box(String contents, String prefix) {
		if (contents == null)
			return null;
		if (prefix == null)
			prefix = "";
		StringBuilder sb = new StringBuilder(prefix);
		for (int i = 0; i < contents.length() + 4; i++)
			sb.append("-");
		sb.append("\n").append(prefix);
		sb.append("| ").append(contents).append(" |");
		sb.append("\n").append(prefix);
		for (int i = 0; i < contents.length() + 4; i++)
			sb.append("-");
		return sb.toString();
	}

	public static String underline(String contents, String prefix) {
		if (contents == null)
			return null;
		if (prefix == null)
			prefix = "";
		StringBuilder sb = new StringBuilder(prefix);
		sb.append(contents);
		sb.append("\n").append(prefix);
		for (int i = 0; i < contents.length(); i++)
			sb.append("-");
		return sb.toString();
	}

	public static String postBuffer(String contents, int maxLength, int ARG_SPACE) {
		StringBuilder sb = new StringBuilder(contents);
		while (sb.length() < maxLength + ARG_SPACE) {
			sb.append(" ");
		}
		return sb.toString();
	}

	public static String wordWrap(String contents, int length, int currentIndex,
			String wrapPrefix) {
		StringBuilder sb = new StringBuilder();
		while (sb.length() < currentIndex)
			sb.append(" ");
		String front = sb.toString();
		sb = new StringBuilder();
		int left = length - currentIndex;
		if (contents.length() > length) {
			sb.append(contents.substring(0, left)).append("\n");
			contents = contents.substring(left);
			while (contents.length() + currentIndex > length) {
				sb.append(front).append(wrapPrefix).append(
						contents.substring(0, left - wrapPrefix.length()))
						.append("\n");
				contents = contents.substring(left - wrapPrefix.length());
			}
			sb.append(front).append(wrapPrefix).append(contents);
		} else
			sb.append(contents);
		return sb.toString();
	}
}
