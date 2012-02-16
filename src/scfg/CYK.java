package scfg;

import scfg.output.Display;
import scfg.utils.Compare;

import desposito6.math.*;
import desposito6.utils.CommandLine;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class CYK {

	private static Display output;

	public static void setOutput(Display output) {
		CYK.output = output;
	}

	/**
	 * Fills the structure (str) string with the predicted structure according
	 * to running the CYK algorithm and traceback on the provided sequence
	 * string (seq). The parameters should be double[3] including the following
	 * probabilities:
	 * 
	 * params[0] = p(S->L) params[1] = p(L->s) params[2] = p(F->LS)
	 * 
	 * @param seq
	 *            - The sequence to predict from.
	 * @param str
	 *            - The string to fill with the predicted value.
	 * @param params
	 *            - The Knudson-Hein Grammar parameters as described above.
	 * @param verbose
	 *            - Display output to command line if true.
	 */
	public static String predictKH(String seq, BigDouble[] params,
			boolean verbose) {
		int size = seq.length();
		BigDouble[][][] parr = new BigDouble[size][size][3];
		int[][][] tau = new int[size][size][3];
		String pred = null;

		if (size < 1) {
			output.out("Invalid sequence provided: \n\t[" + seq + "]");
			return pred;
		}

		output.out("Predicting secondary structure for \n\t\t[" + seq + "]");

		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				for (int k = 0; k < 3; k++)
					tau[i][j][k] = -1;

		// Fill Array
		kh_CYK(seq, params, parr, tau);
		BigDouble prob = parr[0][size - 1][0];

		output.out("\t-KH Maximum Probability for sequence is " + prob);

		// Trace Back
		if (prob.compareTo(0) > 0) {
			pred = kh_trace_back(tau);

			output.out("\t-KH most likely structure for sequence is\n\t\t[ "
					+ pred + " ]");
		}

		return pred;
	}

	/**
	 * Fills the structure (str) string with the predicted structure according
	 * to running the CYK algorithm and traceback on the provided sequence
	 * string (seq). The parameters should be double[3] including the following
	 * probabilities:
	 * 
	 * params[0] = p(S->L) params[1] = p(L->s) params[2] = p(F->LS)
	 * 
	 * @param seq
	 *            - The sequence to predict from.
	 * @param str
	 *            - The string to fill with the predicted value.
	 * @param params
	 *            - The Knudson-Hein Grammar parameters as described above.
	 * @param verbose
	 *            - Display output to command line if true.
	 */
	public static String predictFromGrammar(String seq,
			Grammar grammar) {
		int size = seq.length();
		BigDecimal[][][] parr = new BigDecimal[size][size][3];
		int[][][] tau = new int[size][size][3];
		String pred = null;
		if (size < 1) {
			return pred;
		}
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				for (int k = 0; k < 3; k++) {
					tau[i][j][k] = -1;
					parr[i][j][k] = new BigDecimal(0);
				}
			}
		}
		if (grammar instanceof PfoldGrammar) {
			PfoldGrammar pfold = (PfoldGrammar) grammar;
			
//			BigDouble[] kh_params = new BigDouble[pfold.get_kh_params().length];
//			for(int i=0;i<kh_params.length;i++)
//				kh_params[i] = new BigDouble(pfold.get_kh_params()[i].doubleValue());
//			BigDouble[] Pfold_paramsUnmatched = new BigDouble[pfold.getPfold_paramsUnmatched().length];
//			for(int i=0;i<Pfold_paramsUnmatched.length;i++)
//				Pfold_paramsUnmatched[i] = new BigDouble(pfold.getPfold_paramsUnmatched()[i].doubleValue());
//			BigDouble[][] Pfold_paramsBasePairs = new BigDouble[pfold.getPfold_paramsBasePairs().length][pfold.getPfold_paramsBasePairs()[0].length];
//			for(int i=0;i<Pfold_paramsBasePairs.length;i++)
//				for(int j=0;j<Pfold_paramsBasePairs[i].length;j++)
//					Pfold_paramsBasePairs[i][j] = new BigDouble(pfold.getPfold_paramsBasePairs()[i][j].doubleValue());
			
			BigDecimal[] kh_params = new BigDecimal[pfold.get_kh_params().length];
			for(int i=0;i<kh_params.length;i++)
				kh_params[i] = new BigDecimal(pfold.get_kh_params()[i].doubleValue());
			BigDecimal[] Pfold_paramsUnmatched = new BigDecimal[pfold.getPfold_paramsUnmatched().length];
			for(int i=0;i<Pfold_paramsUnmatched.length;i++)
				Pfold_paramsUnmatched[i] = new BigDecimal(pfold.getPfold_paramsUnmatched()[i].doubleValue());
			BigDecimal[][] Pfold_paramsBasePairs = new BigDecimal[pfold.getPfold_paramsBasePairs().length][pfold.getPfold_paramsBasePairs()[0].length];
			for(int i=0;i<Pfold_paramsBasePairs.length;i++)
				for(int j=0;j<Pfold_paramsBasePairs[i].length;j++)
					Pfold_paramsBasePairs[i][j] = new BigDecimal(pfold.getPfold_paramsBasePairs()[i][j].doubleValue());
			
			
			Pfold_CYK(seq, kh_params, Pfold_paramsUnmatched, Pfold_paramsBasePairs, parr, tau);
		}
//		BigDouble prob = parr[0][size - 1][0];
		BigDecimal prob = parr[0][size - 1][0];
		int compVal = prob.compareTo(BigDecimal.ZERO);
		if (compVal > 0)
			pred = kh_trace_back(tau);
		else
			output.out("Probability of highest probability parse is <= 0. ("
					+ prob.toString() + " :: " + compVal + ")");

		return pred;
	}

	public static String predictFromGrammar23S(String seq, String nat,
			Grammar grammar, boolean verbose) {
		int size = seq.length();
		Map<String, BigDouble> parr = new HashMap<String, BigDouble>();
		int[][][] tau = new int[size][size][3];

		output.out("\t\tTau allocated");
		String pred = null;

		if (size < 1) {
			output.out("\nInvalid sequence provided: \n\t[ " + seq + " ]");
			return pred;
		}

		output
				.out("\nPredicting secondary structure for \n\t\t[ " + seq
						+ " ]");

		long start = System.currentTimeMillis();
		for (int i = 0; i < size; i++) {
			// Progress Bar
			CommandLine.DisplayBar(seq.length(), i, ((long) (System
					.currentTimeMillis() - start) / 1000));
			for (int j = i; j < size; j++) {
				String ij = i + ":" + (i + j);
				for (int k = 0; k < 3; k++) {
					tau[i][j][k] = -1;
					parr.put(ij + ":" + k, new BigDouble(0));
				}
			}
		}
		CommandLine.DisplayBarFinish();

		// Fill Array
		if (grammar instanceof PfoldGrammar) {
			PfoldGrammar pfold = (PfoldGrammar) grammar;
//			_23S_CYK(seq, pfold.getKH_params(), pfold
//					.getPfold_paramsUnmatched(), pfold
//					.getPfold_paramsBasePairs(), parr, tau);
		}
		BigDouble prob = parr.get("0:" + (size - 1) + ":" + 0);

		output
				.out("\t-Probability of highest probability parse for sequence is \n\t\t"
						+ prob);

		if (prob != null) {
//			grammar.recordProbability(prob);

			// Trace Back
			if (prob.compareTo(0) > 0) {
				pred = kh_trace_back(tau);
				{
					output
							.out("\t-Highest probability parse generates structure\n\t\t[ "
									+ pred + " ]");
					if (nat != null)
						output
								.out("\t-FMeasure for predicted structure is\n\t\t"
										+ Compare.getFMeasureBD(nat, pred));
				}
			}
		}

		return pred;
	}

	/**
	 * Runs the CYK algorithm on the provided sequence with the provided
	 * parameters. The int array should be array size int[n-1][n-1][3] where "n"
	 * is the length of the sequence.
	 * 
	 * The maximum probability for the provided sequence can be found at index
	 * arr[0][n-1][0]
	 * 
	 * @param seq
	 *            - The sequence to calculate the maximum probability.
	 * @param params
	 *            - The Knudson-Hein grammar probabilities.
	 * @param arr
	 *            - The probability array to fill for the algorithm.
	 * @param arr
	 *            - The traceback array to fill for the algorithm.
	 */
	private static void kh_CYK(String seq, BigDouble[] p, BigDouble[][][] arr,
			int[][][] tau) {
		int S = 0, L = 1, F = 2;
		BigDouble temp, tempProd;
		BigDouble S_LS, L_dFd, F_dFd;
		S_LS = new BigDouble(p[S]);
		L_dFd = new BigDouble(p[L]);
		F_dFd = new BigDouble(p[F]);
		for (int i = 0; i < seq.length(); i++) {
			if (seq.charAt(i) == 's') {
				arr[i][i][L] = p[L];
				tau[i][i][S] = 0;
				tau[i][i][L] = 0;
				tau[i][i][F] = 0;
			}
		}
		for (int j = 0; j < seq.length(); j++) {
			for (int i = 0; i + j < seq.length(); i++) {
				int ij = i + j;
				if (seq.charAt(i) == 'd' && seq.charAt(ij) == 'd' && j > 2) {
					// ///////
					// L->dFd
					temp = L_dFd.mult(arr[i + 1][ij - 1][F]);
					if (arr[i][ij][L].compareTo(temp) <= 0) {
						arr[i][ij][L] = temp;
						tau[i][ij][L] = (F << 8) | 0xff;
					}
					// ///////
					// F->dFd
					temp = F_dFd.mult(arr[i + 1][ij - 1][F]);
					if (arr[i][ij][F].compareTo(temp) <= 0) {
						arr[i][ij][F] = temp;
						tau[i][ij][F] = (F << 8) | 0xff;
					}

				}
				// /////
				// S->L
				temp = p[S].mult(arr[i][ij][L]);
				if (arr[i][ij][S].compareTo(temp) <= 0) {
					arr[i][ij][S] = temp;
					tau[i][ij][S] = (L << 8) | 0xff;
				}
				for (int k = i; k < ij; k++) {
					tempProd = arr[i][k][L].mult(arr[k + 1][ij][S]);
					// //////
					// S->LS
					temp = S_LS.mult(tempProd);
					if (arr[i][ij][S].compareTo(temp) <= 0) {
						arr[i][ij][S] = temp;
						tau[i][ij][S] = (k << 16) | (L << 8) | (S);
					}
					// //////
					// F->LS
					temp = p[F].mult(tempProd);
					if (arr[i][ij][F].compareTo(temp) <= 0) {
						arr[i][ij][F] = temp;
						tau[i][ij][F] = (k << 16) | (L << 8) | (S);
					}
				}
			}
		}
	}

	/**
	 * Runs the CYK algorithm on the provided sequence with the provided
	 * parameters. The int array should be array size int[n-1][n-1][3] where "n"
	 * is the length of the sequence.
	 * 
	 * The maximum probability for the provided sequence can be found at index
	 * arr[0][n-1][0]
	 * 
	 * @param seq
	 *            - The sequence to calculate the maximum probability.
	 * @param params
	 *            - The Knudson-Hein grammar probabilities.
	 * @param arr
	 *            - The probability array to fill for the algorithm.
	 * @param arr
	 *            - The traceback array to fill for the algorithm.
	 */
//	private static void Pfold_CYK(String seq, BigDouble[] p, BigDouble[] p_u,
//			BigDouble[][] p_bp, BigDouble[][][] arr, int[][][] tau) {
	private static void Pfold_CYK(String seq, BigDecimal[] p, BigDecimal[] p_u,
			BigDecimal[][] p_bp, BigDecimal[][][] arr, int[][][] tau) {
		int S = 0, L = 1, F = 2;
		int[] seq2 = PfoldGrammar.getNucleotideIndexArray(seq);
		BigDecimal temp, tempProd, one = new BigDecimal(1);
		BigDecimal S_LS, L_dFd, F_dFd;
		S_LS = one.subtract(p[S]);
		L_dFd = one.subtract(p[L]);
		F_dFd = one.subtract(p[F]);
		MathContext precision = MathContext.DECIMAL128;
		for (int i = 0; i < seq.length(); i++) {
			temp = p[L].multiply(p_u[seq2[i]]);
			arr[i][i][L] = p[L].multiply(p_u[seq2[i]]);
			tau[i][i][S] = 0;
			tau[i][i][L] = 0;
			tau[i][i][F] = 0;
		}
		System.out.println();
		long start = System.currentTimeMillis();
		for (int j = 0; j < seq.length(); j++) {
			// Progress Bar
			CommandLine.DisplayBar(seq.length(), j, ((long) (System
					.currentTimeMillis() - start) / 1000));
			for (int i = 0; i + j < seq.length(); i++) {
				int ij = i + j;
				if (j > 2) {
					tempProd = p_bp[seq2[i]][seq2[ij]]
							.multiply(arr[i + 1][ij - 1][F], precision);
					// ///////
					// L->dFd
					temp = L_dFd.multiply(tempProd, precision);
					if (arr[i][ij][L].compareTo(temp) <= 0) {
						arr[i][ij][L] = new BigDecimal(temp.toPlainString());
						tau[i][ij][L] = (F << 8) | 0xff;
					}
					// ///////
					// F->dFd
					temp = F_dFd.multiply(tempProd, precision);
					if (arr[i][ij][F].compareTo(temp) <= 0) {
						arr[i][ij][F] = new BigDecimal(temp.toPlainString());
						tau[i][ij][F] = (F << 8) | 0xff;
					}
				}
				// /////
				// S->L
				temp = p[S].multiply(arr[i][ij][L], precision);
				if (arr[i][ij][S].compareTo(temp) <= 0) {
					arr[i][ij][S] = new BigDecimal(temp.toPlainString());
					tau[i][ij][S] = (L << 8) | 0xff;
				}
				for (int k = i; k < ij; k++) {
					tempProd = arr[i][k][L].multiply(arr[k + 1][ij][S], precision);
					// //////
					// S->LS
					temp = S_LS.multiply(tempProd, precision);
					if (arr[i][ij][S].compareTo(temp) <= 0) {
						arr[i][ij][S] = new BigDecimal(temp.toPlainString());
						tau[i][ij][S] = (k << 16) | (L << 8) | (S);
					}
					// //////
					// F->LS
					temp = p[F].multiply(tempProd, precision);
					if (arr[i][ij][F].compareTo(temp) <= 0) {
						arr[i][ij][F] = new BigDecimal(temp.toPlainString());
						tau[i][ij][F] = (k << 16) | (L << 8) | (S);
					}
				}
			}
		}
		CommandLine.DisplayBarFinish();
	}

	/**
	 * Traces back through the provided array to find the maximum probability
	 * parse. The resulting structure will be stored into the str variable.
	 * 
	 * @param arr
	 *            - Array to traceback through.
	 * @param seqLength
	 *            - length of the array[n][n][3]
	 * @param str
	 *            - String to fill with the resulting structure.
	 */
	public static String kh_trace_back(int[][][] tau) {
		Stack<Long> stk = new Stack<Long>();
		char[] pcarr = new char[tau.length];
		for (int i = 0; i < tau.length; i++)
			pcarr[i] = '.';
		int S = 0, F = 2, k, y, z, i, j, v;
		long temp = ((tau.length - 1) << 8) | S;
		boolean push_second = false;
		stk.push(temp);
		while (stk.size() > 0) {
			temp = stk.pop();
			i = (int) ((temp >>> 24) & 0xffff);
			j = (int) ((temp >>> 8) & 0xffff);
			v = (int) (temp & 0xff);
			k = tau[i][j][v];
			// terminal: s
			if (k > 0) {
				y = (k & 0xff00) >> 8;
				z = k & 0xff;
				k = (k >> 16) & 0xffff;
				if (y == F) {
					pcarr[i] = '(';
					pcarr[j] = ')';
					long t0 = (((long) (i + 1)) << 24) | ((j - 1) << 8) | F;
					stk.push(t0);

				} else {
					push_second = (z == S);
					long t0 = ((long) (i) << 24) | ((push_second ? k : j) << 8)
							| y;
					stk.push(t0);
					if (push_second) {
						long t1 = (((long) (k + 1)) << 24) | (j << 8) | z;
						stk.push(t1);
					}
				}
			}
		}
		return new String(pcarr);
	}
}
