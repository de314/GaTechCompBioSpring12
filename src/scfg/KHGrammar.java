package scfg;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import desposito6.math.*;

import scfg.output.Display;

/**
 * The Class KHGrammar.
 */
public class KHGrammar extends Grammar {

	public enum PrecisionValues {
		_128bit, _64bit, _52bit, _32bit
	}

	public static PrecisionValues precision = PrecisionValues._128bit;

	/** The curr helix length. */
	private int currHelixLength = 0;

	/** The counts. */
	private int[] counts;

	/** The K h_percentages. */
	protected iDouble[] KH_params, KH_alt_params, KH_percentages;
	
	private Map<String, iDouble> trainingCounts;

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.Grammar#reset()
	 */
	@Override
	public void reset() {
		initArrays();
		NBP = 0;
		NUN = 0;
		currHelixLength = 0;
	}
	
	private void resetCountsArray(){
		counts = new int[9];
	}

	/**
	 * Inits the arrays.
	 */
	private void initArrays() {
		resetCountsArray();
		if (precision == PrecisionValues._52bit) {
			KH_params = new BigDouble[3];
			KH_alt_params = new BigDouble[4];
			KH_percentages = new BigDouble[3];
			for (int i = 0; i < 3; i++) {
				KH_params[i] = new BigDouble(0);
				KH_alt_params[i] = new BigDouble(0);
				KH_percentages[i] = new BigDouble(0);
			}
			KH_alt_params[3] = new BigDouble(0);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			KH_params = new MyBigDecimal[3];
			KH_alt_params = new MyBigDecimal[4];
			KH_percentages = new MyBigDecimal[3];
			for (int i = 0; i < 3; i++) {
				KH_params[i] = new MyBigDecimal(0, context);
				KH_alt_params[i] = new MyBigDecimal(0, context);
				KH_percentages[i] = new MyBigDecimal(0, context);
			}
			KH_alt_params[3] = new MyBigDecimal(0, context);
		}
	}
	
	/**
	 * Instantiates a new kH grammar.
	 * 
	 * @param trainingSetFilename
	 *            the filename
	 */
	public KHGrammar() {
		super("", new Display("KHGrammar"));
		initArrays();
	}

	/**
	 * Instantiates a new kH grammar.
	 * 
	 * @param trainingSetFilename
	 *            the filename
	 */
	public KHGrammar(String trainingSetFilename, Display output) {
		super(trainingSetFilename, output);
		initArrays();
	}

	/**
	 * Instantiates a new kH grammar.
	 * 
	 * @param nat
	 *            the nat
	 * @param trainingSetFilename
	 *            the filename
	 */
	public KHGrammar(String nat, String trainingSetFilename, Display output) {
		super(trainingSetFilename, output);
		initArrays();
		add(nat);
	}

	/**
	 * Gets the frequencies of each production and adds each to the already
	 * existing values in the count array.
	 * 
	 * @param nat
	 *            - The structure to process.
	 */
	public void add(String nat) {
		if (nat != null && nat.length() > 0 && nat.matches("[().]+")) {
			NUN = 0;
			NBP = 0;
			int[] pairs = getPairsArray(nat);
			setCountsArray(nat, pairs);
			setProductionProbabilities();
			setStateProbabilities();
		} else
			output
					.eout("Invalid Structure or sequence provided to Knudsen-Hein Trainer.");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.Grammar#add(java.lang.String, java.lang.String)
	 */
	@Override
	public void add(String nat, String seq) {
		add(nat);
	}

	/**
	 * Set the counts in an array of size 9. The indeces are defined below and
	 * correspond to the MACROS defined at the header of this file.
	 * 
	 * Sets counts in provided array as: [ 
	 * 0: a = c(S->L) 
	 * 1: b = c(S->LS) 
	 * 2: 	  c(S) 		= a + b 
	 * 3: c = c(L->s) 	= NUN 
	 * 4: 	  c(L->dFd) = e - d 
	 * 5: 	  c(L) 		= c + (e - d) 
	 * 6: 	  c(F->LS) 	= e - d 
	 * 7: d = c(F->dFd) 
	 * 8: e = c(F) 		= NBP
	 * 
	 * @param nat
	 *            the nat
	 * @param pairs
	 *            - An array denoting an indexes corresponding index in a base
	 *            pair
	 */
	private void setCountsArray(String nat, int[] pairs) {
		// Recursively evaluate the entire structure
		setCountsArray(nat, pairs, 0, (nat.length() - 1), true, counts);

		// Calculate other production frequences with relationships defined
		// above.
		counts[L_s] += NUN;
		counts[F] += NBP;
		counts[S] = counts[S_L] + counts[S_LS];
		counts[L] = counts[L_s] + counts[F] - counts[F_dFd];
		counts[F_LS] = counts[F] - counts[F_dFd];
	}
	
	public static HashMap<String, Integer> getCountsArray(String nat) {
		int[] counts = new int[9];
		
		// Recursively evaluate the entire structure
		KHGrammar temp = new KHGrammar();
		temp.setCountsArray(nat, temp.getPairsArray(nat), 0, (nat.length() - 1), true, counts);

		// Calculate other production frequences with relationships defined
		// above.
		HashMap<String, Integer> countsMap = new HashMap<String, Integer>();
		int NUN = 0;
		int NBP = 0;
		for (Character c : nat.toCharArray()){
			if (c == '.')
				NUN++;
			else if (c == ')')
				NBP++;
		}
		countsMap.put("NUN", NUN);
		countsMap.put("NBP", NBP);
		countsMap.put("seqlength", nat.length());
		countsMap.put("S", counts[S_L] + counts[S_LS]);
		countsMap.put("S_LS", counts[S_LS]);
		countsMap.put("S_L", counts[S_L]);
		countsMap.put("L", NUN + NBP - counts[F_dFd]);
		countsMap.put("L_s", NUN);
		countsMap.put("L_dFd", NBP - counts[F_dFd]);
		countsMap.put("F", NBP);
		countsMap.put("F_LS", NBP - counts[F_dFd]);
		countsMap.put("F_dFd", counts[F_dFd]);
		return countsMap;
	}
	
	@Override
	public HashMap<String, Integer> getCountsArray() {		
		// Calculate other production frequences with relationships defined
		// above.
		HashMap<String, Integer> countsMap = new HashMap<String, Integer>();
		
		countsMap.put("NUN", counts[L_s]);
		countsMap.put("NBP", counts[F]);
		countsMap.put("S", counts[S]);
		countsMap.put("S_LS", counts[S_LS]);
		countsMap.put("S_L", counts[S_L]);
		countsMap.put("L", counts[L]);
		countsMap.put("L_s", counts[L_s]);
		countsMap.put("L_dFd",counts[L_dFd]);
		countsMap.put("F", counts[F]);
		countsMap.put("F_LS", counts[F_LS]);
		countsMap.put("F_dFd", counts[F_dFd]);
		return countsMap;
	}

	/**
	 * Recursive algorithm which counts the number of productions from S->L and
	 * S->LS. It also counts the number of productions to dFd from L of F. Along
	 * with the number of base-pairs and number of unmatched nucleotides, all
	 * other grammar frequencies can be calculated.
	 * 
	 * @param nat
	 *            the nat
	 * @param pairs
	 *            - An array denoting an indexes corresponding index in a base
	 *            pair
	 * @param i
	 *            - The starting index of the substructure to be evaluated.
	 *            Recurse on each base pair taking i+1.
	 * @param j
	 *            - The ending index of the substructure to be evaluated.
	 *            Recurse on each base pair taking j-1.
	 * @param fromS
	 *            the from s
	 * @see get_kh_counts()
	 */
//	private void setCountsArray(String nat, int[] pairs, int i, int j,
//			boolean fromS, int[] counts) {
//		if (i > j || i >= nat.length())
//			return;
//		for (;i<=j;i++) {
//			if (nat.charAt(i) == '('){
//				if (fromS) {
//					if (pairs[i] == j)
//						counts[S_L]++;
//					else
//						counts[S_LS]++;
//					counts[L_dFd]++;
//				} else {
//					if (i > 0 && pairs[i-1] == pairs[i]+1)
//						counts[F_dFd]++;
//					else
//						counts[L_dFd]++;
//				}
//				setCountsArray(nat, pairs, i+1, pairs[i]-1, false, counts);
//				i = pairs[i]+1;
//			} else if (nat.charAt(i) == '.') {
//				if (i == j)
//					counts[S_L]++;
//				else if (fromS)
//					counts[S_LS]++;
//				else
//					counts[F_LS]++;
//			}
//			fromS = true;
//		}
//	}
	private void setCountsArray(String nat, int[] pairs, int i, int j,
			boolean fromS, int[] counts) {
		if (i > j || i >= nat.length())
			return;
		for (; i <= j; i++) {
			char c = nat.charAt(i);
			if (c == '.') {
				if (i == j)
					counts[S_L]++;
				else if (fromS)
					counts[S_LS]++;
			} else if (c == '(') {
				if (fromS) {
					if (pairs[i] == j)
						counts[S_L]++;
					else
						counts[S_LS]++;
				}
				if (i > 0 && pairs[i - 1] == pairs[i] + 1) {
					counts[F_dFd]++;
//					currHelixLength++;
				} else {
					counts[L_dFd]++;
//					recordHelixLength(currHelixLength);
//					currHelixLength = 1;
				}
				setCountsArray(nat, pairs, i + 1, pairs[i] - 1, false, counts);
				i = pairs[i];
			}
			fromS = true;
		}
		// Record the last helix. If no helices, we are recording 0.
//		recordHelixLength(currHelixLength);
	}

	/**
	 * Converts a KH-Grammar count array (size 9) to a production probabilities
	 * parameter array (size 3). The array will be set as follows:
	 * 
	 * probs[0] = p(S->L) 
	 * probs[1] = p(L->s) 
	 * probs[2] = p(F->LS)
	 * 
	 * So we can claculate access the probabilities in the following way:
	 * 
	 * q1 = p(S->L)   = probs[0]
	 * p1 = p(S->LS)  = 1 - probs[0]
	 * q2 = p(L->s)   = probs[1] 
	 * q2 = p(L->dFd) = 1 - probs[1] 
	 * p3 = p(F->LS)  = probs[2] 
	 * q3 = p(F->dFd) = 1 - probs[2]
	 * 
	 * **Note: if there exists 0 productions for a given non-terminal then it
	 * needs to be specified that both productions should have zero probability.
	 * If this is the case then probs[i] will equal -1. This is handled in the
	 * probabilities String.
	 * 
	 */
	private void setProductionProbabilities() {
		if (precision == PrecisionValues._52bit) {
			iDouble invalid = new BigDouble(-1);
			KH_params[0] = counts[2] == 0 ? invalid
					: (new BigDouble(counts[0])).div(new BigDouble(counts[2]));
			KH_params[1] = counts[5] == 0 ? invalid
					: (new BigDouble(counts[3])).div(new BigDouble(counts[5]));
			KH_params[2] = counts[8] == 0 ? invalid
					: (new BigDouble(counts[6])).div(new BigDouble(counts[8]));
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			iDouble invalid = new MyBigDecimal(-1, context);
			KH_params[0] = counts[2] == 0 ? invalid : (new MyBigDecimal(
					counts[0], context)).div(new MyBigDecimal(counts[2],
					context));
			KH_params[1] = counts[5] == 0 ? invalid : (new MyBigDecimal(
					counts[3], context)).div(new MyBigDecimal(counts[5],
					context));
			KH_params[2] = counts[8] == 0 ? invalid : (new MyBigDecimal(
					counts[6], context)).div(new MyBigDecimal(counts[8],
					context));
		}
	}

	/**
	 * Set the String to display a formatted version of the production
	 * probabilites. The output will be appended to the current String. The
	 * String will display:
	 * 
	 * Knudson-Hein Grammar Production Probabilites:
	 * --------------------------------------------- 
	 * S -> L (0.684211) | LS (0.315789) 
	 * L -> s (0.730769) | dFd (0.269231) 
	 * F -> LS (0.875) | dFd (0.125)
	 * 
	 * See get_production_probabilites for how to handle special cases.
	 * 
	 * @return the string
	 */
	public String toStringKH_Probabilites() {
		StringBuilder ss = new StringBuilder();
		ss.append("Knudson-Hein Grammar Production Probabilites:").append("\n");
		ss.append("---------------------------------------------").append("\n");
		String[] arr = { "S -> L", "| LS", "L -> s", "| dFd", "F -> LS",
				"| dFd", };
		iDouble zero = new BigDouble(0), one = new BigDouble(1);
		if (precision == PrecisionValues._52bit) {
			zero = new BigDouble(0);
			one = new BigDouble(1);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			zero = new MyBigDecimal(0, context);
			one = new MyBigDecimal(1, context);
		}
		for (int i = 0; i < KH_params.length; i++)
			ss.append(arr[2 * i]).append("  (").append(
					KH_params[i].compareTo(zero) < 0 ? "*" : KH_params[i]
							.toString()).append(") \t")
					.append(arr[(2 * i) + 1]).append(" (").append(
							KH_params[i].compareTo(zero) < 0 ? "*" : (one
									.sub(KH_params[i])).toString()).append(
							")\n");
		ss.append("\n");
		return ss.toString();
	}

	/**
	 * Converts a KH-Grammar count array (size 9) to a state percentage array
	 * (size 3). This represents the percentage of productions rooted at the
	 * specified non-terminal state. The array will be set as follows:
	 * 
	 * perc[0] = p(S) perc[1] = p(L) perc[2] = p(F)
	 * 
	 */
	private void setStateProbabilities() {
		double sum = counts[2] + counts[5] + counts[8];
		if (precision == PrecisionValues._52bit) {
			iDouble zero = new BigDouble(0), sumBD = new BigDouble(sum);
			KH_percentages[0] = sum == 0 ? zero : new BigDouble(counts[2])
					.div(sumBD);
			KH_percentages[1] = sum == 0 ? zero : new BigDouble(counts[5])
					.div(sumBD);
			KH_percentages[2] = sum == 0 ? zero : new BigDouble(counts[8])
					.div(sumBD);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			iDouble zero = new MyBigDecimal(0, context), sumBD = new MyBigDecimal(
					sum, context);
			KH_percentages[0] = sum == 0 ? zero : new MyBigDecimal(counts[2],
					context).div(sumBD);
			KH_percentages[1] = sum == 0 ? zero : new MyBigDecimal(counts[5],
					context).div(sumBD);
			KH_percentages[2] = sum == 0 ? zero : new MyBigDecimal(counts[8],
					context).div(sumBD);
		}
	}

	/**
	 * Set the String to display a formatted version of the percentage of
	 * productions from each non-terminal. The output will be appended to the
	 * current String.The String will display:
	 * 
	 * S -> L (0.684211) | LS (0.315789) L -> s (0.730769) | dFd (0.269231) F ->
	 * LS (0.875) | dFd (0.125)
	 * 
	 * See get_production_probabilites for how to handle special cases.
	 * 
	 * @return the string
	 */
	public String toStringStateProbabilities() {
		StringBuilder ss = new StringBuilder();
		ss.append("Knudson-Hein Grammar State Production Percentages:\n");
		ss.append("--------------------------------------------------\n");
		iDouble cent;
		if (precision == PrecisionValues._52bit) {
			cent = new BigDouble(100);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			cent = new MyBigDecimal(100, context);
		}
		ss.append("S: ").append(KH_percentages[0].mult(cent)).append("%\n");
		ss.append("L: ").append(KH_percentages[1].mult(cent)).append("%\n");
		ss.append("F: ").append(KH_percentages[2].mult(cent)).append("%\n");
		ss.append("\n");
		return ss.toString();
	}

	/**
	 * Converts dot bracket notation to a "ds" ambiguous sequences.
	 * 
	 * @param nat
	 *            - Dot bracket structure to be converted
	 * @return the ambiguous native structure
	 */
	public static String getAmbiguousNativeStructure(String nat) {
		char[] arr = new char[nat.length()];
		for (int i = 0; i < nat.length(); i++)
			arr[i] = nat.charAt(i) == '.' ? 's' : 'd';
		return new String(arr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.Grammar#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(toStringKH_Probabilites());
		sb.append(this.toStringStateProbabilities());
		return sb.toString();
	}

	/**
	 * Copy array.
	 * 
	 * @param arr
	 *            the arr
	 * @return the double[]
	 */
	public iDouble[] copyArray(iDouble[] arr) {
		iDouble[] temp = new iDouble[arr.length];
		for (int i = 0; i < arr.length; i++)
			temp[i] = arr[i].deepCopy();
		return temp;
	}

	/**
	 * Copy array.
	 * 
	 * @param arr
	 *            the arr
	 * @return the double[][]
	 */
	public iDouble[][] copyArray(iDouble[][] arr) {
		iDouble[][] temp = new iDouble[arr.length][arr[0].length];
		for (int i = 0; i < arr.length; i++)
			for (int j = 0; j < arr.length; j++)
				temp[i][j] = arr[i][j].deepCopy();

		return temp;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.Grammar#deepCopy()
	 */
	@Override
	public Grammar deepCopy() {
		KHGrammar temp = new KHGrammar(super.name, super.output);
		temp.KH_params = copyArray(KH_params);
		temp.KH_percentages = copyArray(KH_percentages);
		return temp;
	}

	@Override
	public iDouble scoreRNAFile(String seq, String str) {
		int[] pairs = getPairsArray(str);
		resetCountsArray();
		setCountsArray(str, pairs);
		
		iDouble prob  = KH_params[0].pow(counts[S_L]);
		prob = prob.mult(KH_params[0].sub(1).abs().pow(counts[S_LS]));
		prob = prob.mult(KH_params[1].pow(counts[L_s]));
		prob = prob.mult(KH_params[1].sub(1).abs().pow(counts[L_dFd]));
		prob = prob.mult(KH_params[2].pow(counts[F_LS]));
		prob = prob.mult(KH_params[2].sub(1).abs().pow(counts[F_dFd]));
		
		return prob;
	}
	
	@Override
	public Map<String, iDouble> scoreRNAFileWithStats(String seq, String str) {
		Map <String, Integer> counts = getCountsArray(str);
		Map <String, iDouble> rtn = new HashMap<String, iDouble>();
		
		iDouble prob  = KH_params[0].pow(counts.get("S_L"));
		prob = prob.mult(KH_params[0].sub(1).abs().pow(counts.get("S_LS")));
		prob = prob.mult(KH_params[1].pow(counts.get("L_s")));
		prob = prob.mult(KH_params[1].sub(1).abs().pow(counts.get("L_dFd")));
		prob = prob.mult(KH_params[2].pow(counts.get("F_LS")));
		prob = prob.mult(KH_params[2].sub(1).abs().pow(counts.get("F_dFd")));
		
		
		rtn.put("prob", prob);
		for (Entry<String, Integer> me : counts.entrySet())
			rtn.put(me.getKey(), new MyBigDecimal(me.getValue()));	
		return rtn;
	}

	public static void main(String[] args) {
		KHGrammar kh = new KHGrammar("none", new Display("KHGrammar"));
		kh.add("...((((((((((.....((((((((....(((((((.............))))..)))...)))))).)).(((((((..((((....))))..)))))))...))))))))))...");
		kh.add(".");
		kh.add("..");
		kh.add("(..)");
		kh.add("((..))");
		kh.add("(((...)))");
		kh.add("((((...))))");
		kh.add("(((((...)))))");
		kh.add("((((((...))))))");
		kh.add("(.(..))");
		System.out.println(kh);
		
		System.out.println(kh.scoreRNAFile(null, ".."));
		System.out.println(kh.scoreRNAFile(null, "(..)"));
		System.out.println(kh.scoreRNAFile(null, "(..(.((..))).)"));
	}

	@Override
	public iDouble[] get_kh_params() {
		return KH_params;
	}

	@Override
	public iDouble[] get_kh_alt_params() {
		return KH_alt_params;
	}

	@Override
	public iDouble[] get_kh_perc() {
		return KH_percentages;
	}

	@Override
	public iDouble[] get_nucleotide_dist() {
		return null;
	}

	@Override
	public iDouble[][] get_pfold_params_p() {
		return null;
	}

	@Override
	public iDouble[] get_pfold_params_u() {
		return null;
	}

	@Override
	public iDouble[][] get_pfold_ratio() {
		return null;
	}

	@Override
	public boolean setProbabilities(Map<String, iDouble> params) {
		iDouble S_L = params.get("S->L");
		iDouble L_s = params.get("L->s");
		iDouble F_LS = params.get("F->LS");
		if (S_L != null && L_s != null && F_LS != null) {
			KH_params[0] = S_L;
			KH_params[1] = L_s;
			KH_params[2] = F_LS;
			return true;
		}
		return false;
	}
	
	@Override
	public int[] getTrainingCounts() {
		return counts;
	}

	@Override
	public RNAobj predict(RNAobj rna) {
		throw new UnsupportedOperationException("Prediction not implemented for a Knudsen Hein Grammar");
	}
}
