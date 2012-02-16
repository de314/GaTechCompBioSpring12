package scfg.utils;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;
import desposito6.math.*;

public class Compare {

	public static BufferedWriter bf;

	/**
	 * Pair Count True Positives : Correctly Predicted BP False Positives :
	 * Incorrectly Predicted BP False Negatives : Base pair exists in native
	 * structure
	 * 
	 * Specificity = FN / (TP + FP) Sensitivity/Recall = TP / (TP + FN)
	 * Precision = TP / (TP + FP) True Negative Rate = TN / (TN + FP) Accuracy =
	 * (TP + TN) / (TP + TN + FP + FN) F-Measure = 2*TP / (2*TP + FP + FN) = 2 *
	 * Precision * Recall / (Precision + Recall) Matthews Correlation
	 * Coefficient = (TP * TN - FP * FN) / ((TP + FP)(TP + FN)(FP + TN)(FP +
	 * FN))
	 */

	public static double specificity(int TP, int FP, int FN) {
		return FN / (TP + FP);
	}

	public static double specificity(double[] arr) {
		return arr[3] / (arr[0] + arr[1]);
	}

	public static double sensitivity(int TP, int FN) {
		return TP / (TP + FN);
	}

	public static double sensitivity(double[] arr) {
		return arr[0] / (arr[0] + arr[3]);
	}

	public static BigDouble sensitivityBD(double[] arr) {
		return new BigDouble(arr[0]).div(new BigDouble(arr[0])
				.add(new BigDouble(arr[3])));
	}

	public static double precision(int TP, int FP) {
		return TP / (TP + FP);
	}

	public static double precision(double[] arr) {
		return arr[0] / (arr[0] + arr[1]);
	}

	public static BigDouble precisionBD(double[] arr) {
		return new BigDouble(arr[0]).div(new BigDouble(arr[0])
				.add(new BigDouble(arr[1])));
	}

	/**
	 * DO NOT USE!
	 * 
	 * ***NOTE: TN is not calculated.
	 */
	public static double trueNegativeRate(int TN, int FP) {
		return TN / (TN + FP);
	}

	/**
	 * DO NOT USE!
	 * 
	 * ***NOTE: TN is not calculated.
	 */
	public static double accuracy(int TP, int TN, int FP, int FN) {
		return (TP + TN) / (TP + TN + FP + FN);
	}

	public static double fMeasure(int TP, int FP, int FN) {
		return (2 * precision(TP, FP) * sensitivity(TP, FN))
				/ (precision(TP, FP) + sensitivity(TP, FN));
	}

	public static double fMeasure(double[] arr) {
		double prec = precision(arr);
		double sens = sensitivity(arr);
		double top = 2 * prec * sens;
		double bottom = prec + sens;
		return (2 * precision(arr) * sensitivity(arr))
				/ (precision(arr) + sensitivity(arr));
	}

	public static BigDouble fMeasureBD(double[] arr) {
		BigDouble nom = new BigDouble(2);
		nom.multA(precisionBD(arr));
		nom.multA(sensitivityBD(arr));
		return nom.div(precisionBD(arr).add(precisionBD(arr)));
	}

	/**
	 * DO NOT USE!
	 * 
	 * ***NOTE: TN is not calculated.
	 */
	public static double matthewsCorrelationCoefficient(int TP, int TN, int FP,
			int FN) {
		return (TP * TN - FP * FN)
				/ Math.sqrt((TP + FP) * (TP + FN) * (FP + TN) * (FP + FN));
	}

	public static Map<String, StringBuilder> parseNative(String nat) {
		if (nat == null || !nat.matches("[(.)]+"))
			return null;
		Stack<Integer> stk = new Stack<Integer>();
		Map<String, StringBuilder> ht = new HashMap<String, StringBuilder>();
		for (int i = 0; i < nat.length(); i++) {
			if (nat.charAt(i) == '(')
				stk.push(i);
			else if (nat.charAt(i) == ')')
				ht.put(stk.pop() + ":" + i, new StringBuilder("O"));
		}
		return ht;
	}

	/**
	 * Does not produce FN. To be fixed soon.
	 * 
	 * Array is constuceted as:
	 * 
	 * arr[0] = TP arr[1] = FP arr[2] = TN arr[3] = FN
	 * 
	 * @param pred
	 * @param ht
	 * @return
	 */
	public static double[] parsePredicted(String pred,
			Map<String, StringBuilder> ht) {
		if (pred == null || !pred.matches("[(.)]+") || ht == null)
			return null;
		double[] arr = new double[] { 0, 0, 0, 0 };
		Stack<Integer> stk = new Stack<Integer>();
		for (int i = 0; i < pred.length(); i++) {
			if (pred.charAt(i) == '(')
				stk.push(i);
			else if (pred.charAt(i) == ')') {
				String bp = stk.pop() + ":" + i;
				StringBuilder sb = ht.get(bp);
				if (sb == null)
					ht.put(bp, new StringBuilder("P"));
				else
					sb.append("P");
			}
		}
		for (Entry<String, StringBuilder> me : ht.entrySet()) {
			String s = me.getValue().toString();
			// TP
			if (s.length() == 2)
				arr[0]++;
			// FN
			else if (s.contains("O"))
				arr[3]++;
			// FP
			else if (s.contains("P"))
				arr[1]++;
			// calculate all possible base pairs?
			// TN...?
		}
		return arr;
	}

	public static double getFMeasure(String nat, String pred) {
		Map<String, StringBuilder> ht = parseNative(nat);
		double[] vals = parsePredicted(pred, ht);
		if (vals[0] == 0)
			return 0;
		double fm = fMeasure(vals); 
		return fm;
	}

	public static BigDouble getFMeasureBD(String nat, String pred) {
		Map<String, StringBuilder> ht = parseNative(nat);
		double[] vals = parsePredicted(pred, ht);
		if (vals[0] == 0)
			return new BigDouble(0);
		return fMeasureBD(vals);
	}

	public static void processFile_RNA(File f) {
		if (f == null || !f.exists())
			return;
		if (f.isDirectory()) {
			processDir(f);
			return;
		}
		display("[Processing: " + f.getAbsolutePath() + "\n\n");
		try {
			Scanner scan = new Scanner(f);
			String[] arr = new String[4];
			int count = 0;
			while (scan.hasNextLine() && count < 4) {
				arr[count++] = scan.nextLine();
			}
			scan.close();
			if (arr[1] != null && arr[2] != null) {
				Map<String, StringBuilder> ht = parseNative(arr[1]);
				double[] vals = parsePredicted(arr[2], ht);
				String header = "Native Structure vs. MFE Predicted Structure\n"
						+ "--------------------------------------------\n";
				printCalculations(vals, header);
				if (arr[3] != null) {
					vals = parsePredicted(arr[3], ht);
					header = "Native Structure vs. Custom Predicted Structure\n"
							+ "-----------------------------------------------\n";
					printCalculations(vals, header);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void printCalculations(double[] arr, String header) {
		StringBuilder sb = new StringBuilder(header);
		sb.append("-Specificity\t= ").append(specificity(arr)).append("\n");
		sb.append("-Sensitivity\t= ").append(sensitivity(arr)).append("\n");
		sb.append("-Precision\t= ").append(precision(arr)).append("\n");
		sb.append("-F-Measure\t= ").append(fMeasure(arr)).append("\n\n");
		sb.append("See \"-help\" for equations.\n\n");
		display(sb.toString());
	}

	public static void display(String s) {
		if (bf != null)
			try {
				bf.write(s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		System.out.print(s);
	}

	public static void processDir(File base) {
		if (base == null || !base.exists())
			return;
		display("Opening " + base.getAbsolutePath() + "\n\n");
		File[] files = base.listFiles();
		for (File f : files) {
			if (f.isDirectory())
				processDir(f);
			else
				processFile_RNA(f);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length > 0
				&& (args[0].equals("-h") || args[0].equals("-help"))) {
			System.out
					.println("\n	\\**\n"
							+ "	 * Pair Count \n"
							+ "	 * True Positives  : Correctly Predicted BP\n"
							+ "	 * False Positives : Incorrectly Predicted BP \n"
							+ "	 * False Negatives : Base pair exists in native structure\n"
							+ "	 *\n"
							+ "	 * Specificity 		= FN / (TP + FP)\n"
							+ "	 * Sensitivity/Recall 	= TP / (TP + FN)\n"
							+ "	 * Precision 		= TP / (TP + FP)\n"
							+ "	 * True Negative Rate 	= TN / (TN + FP)\n"
							+ "	 * Accuracy 		= (TP + TN) / (TP + TN + FP + FN)\n"
							+ "	 * F-Measure 		= 2 * Precision * Recall / (Precision + Recall)\n"
							+ "	 * Matthews Correlation\n"
							+ "	 * Coefficient 		= (TP * TN - FP * FN) / ((TP + FP)(TP + FN)(FP + TN)(FP + FN))\n"
							+ "	 */\n\n");
		}
		if (args.length > 1)
			try {
				bf = new BufferedWriter(new FileWriter(args[1]));
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (args.length > 0) {
			processFile_RNA(new File(args[0]));
		} else
			display("Please enter a valid \".rna\" file.");
		if (bf != null)
			try {
				bf.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
