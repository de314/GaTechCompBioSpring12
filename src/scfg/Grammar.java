package scfg;

import java.util.*;

import desposito6.math.iDouble;
import scfg.output.Display;
import scfg.output.RNAFormattedFile;

/**
 * The Class Grammar.
 */
public abstract class Grammar {

	/** Indexes into arrays. */
	public static final int S_L = 0, S_LS = 1, S = 2, L_s = 3, L_dFd = 4, L = 5,
			F_LS = 6, F_dFd = 7, F = 8;
	
	public static final String[] kh_params_names = { "S_L", "L_s", "F_LS", "S_LS", "L_dFd", "F_dFd" };
	public static final String[] kh_nucleotide_names = { "a", "c", "g", "u" };
	public static final String[][] pfold_basePair_names ={ { "aa", "ac", "ag", "au" }, 
														{ "ca", "cc", "cg", "cu" }, 
														{ "ga", "gc", "gg", "gu" }, 
														{ "ua", "uc", "ug", "uu" } };

	/** The NUN. */
	protected int NBP = 0, NUN = 0;

	protected Display output;

	/**
	 * Gets the nBP.
	 * 
	 * @return the nBP
	 */
	public int getNBP() {
		return NBP;
	}

	/**
	 * Gets the nUN.
	 * 
	 * @return the nUN
	 */
	public int getNUN() {
		return NUN;
	}

	/** The avg helix length group. */
	protected double avgSeqLength, avgHelixLength, avgHelixLengthGroup;

	/** The helix frequency group. */
	protected int helixFrequency, helixFrequencyGroup;

	/** The decimal precision. */
	public static int decimalPrecision = 1000;

	protected String name;
	
	public String getName(){
		return name;
	}

	/** The training set. */
	private List<RNAFormattedFile> trainingSet;

	public List<RNAFormattedFile> getTrainingSet() {
		return trainingSet;
	}

	/**
	 * Instantiates a new grammar.
	 * 
	 * @param trainingSetFilename
	 *            the filename
	 */
	public Grammar(String name, Display output) {
		this.output = output;
		this.name = name;
	}

	/**
	 * Record ave seq length.
	 * 
	 * @param length
	 *            the length
	 * @param count
	 *            the count
	 */
	private void recordAveSeqLength(int length, int count) {
		avgSeqLength = ((avgSeqLength * count) + length) / (count + 1);
		count++;
	}

	/**
	 * Record ave helix length group.
	 * 
	 * @param length
	 *            the length
	 */
	private void recordAveHelixLengthGroup(int length) {
		avgHelixLengthGroup = ((avgHelixLengthGroup * helixFrequencyGroup) + length)
				/ (helixFrequencyGroup + 1);
		helixFrequencyGroup++;
	}

	/**
	 * Record helix length.
	 * 
	 * @param length
	 *            the length
	 */
	protected void recordHelixLength(int length) {
		recordAveHelixLengthGroup(length);
		avgHelixLength = ((avgHelixLength * helixFrequency) + length)
				/ (helixFrequency + 1);
		helixFrequency++;
	}

	/**
	 * Trains the Knudson-Hein grammar given a vector of String representing
	 * structures. The calculated probabilities and percentages will fill the
	 * corresponding provided arrays.
	 * 
	 * @param grammar
	 *            the grammar
	 * @param structureList
	 *            the structure list
	 * @param sequencesList
	 *            the sequences list
	 * @param verbose
	 *            the verbose
	 */
	public void train_grammar(Grammar grammar, List<RNAFormattedFile> trainingSet,
			boolean verbose) {
		if (trainingSet.size() > 0) {
			this.trainingSet = trainingSet;
			grammar.reset();
			avgSeqLength = avgHelixLength = 0;
			helixFrequency = 1;
			int count = 1;
			for (int i = 0; i < trainingSet.size(); i++) {
				helixFrequencyGroup = 1;
				avgHelixLengthGroup = 0;
				recordAveSeqLength(trainingSet.get(i).get("seq").length(),
						count++);
				grammar.add(trainingSet.get(i).get("nat"), trainingSet.get(i)
						.get("seq"));
			}
			output.out(grammar.toString());
		} else
			output.eout("Conflicting training set size for Grammar.");
	}

	/**
	 * Sets the elements of the provided array to denote the indecies of the
	 * corresponding nucleotide in each base pair.Given pair(i, j) the array
	 * will be set to P[i] = j and P[j] = i. If nucleotide "k" is unpaired we
	 * will see P[k] = -1.
	 * 
	 * @param nat
	 *            the nat
	 * @return the pairs array
	 */
	public int[] getPairsArray(String nat) {
		if (nat == null || !nat.matches("[(.)]+"))
			throw new IllegalArgumentException(nat);
		Stack<Integer> stk = new Stack<Integer>();
		int[] pairs = new int[nat.length()];
		int size = nat.length();
		NBP = 0;
		NUN = 0;
		for (int i = 0; i < size; i++) {
			pairs[i] = -1;
			char c = nat.charAt(i);
			if (c == '(')
				stk.push(i);
			else if (c == ')') {
				pairs[i] = stk.pop();
				pairs[pairs[i]] = i;
				NBP++;
			} else
				NUN++;
		}
		return pairs;
	}
	
	public static int[] getPairsArraySt(String nat) {
		if (nat == null || !nat.matches("[(.)]+"))
			throw new IllegalArgumentException(nat);
		Stack<Integer> stk = new Stack<Integer>();
		int[] pairs = new int[nat.length()];
		int size = nat.length();
		for (int i = 0; i < size; i++) {
			pairs[i] = -1;
			char c = nat.charAt(i);
			if (c == '(')
				stk.push(i);
			else if (c == ')') {
				pairs[i] = stk.pop();
				pairs[pairs[i]] = i;
			}
		}
		return pairs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "\t" + name + "\n\tAvg. Seq. Length = " + avgSeqLength + "\n";
	}

	/**
	 * Reset.
	 */
	public abstract void reset();

	/**
	 * Adds the.
	 * 
	 * @param nat
	 *            the nat
	 * @param seq
	 *            the seq
	 */
	public abstract void add(String nat, String seq);

	/**
	 * Deep copy.
	 * 
	 * @return the grammar
	 */
	public abstract Grammar deepCopy();
	
	public abstract boolean setProbabilities(Map<String, iDouble> params);

	public abstract iDouble scoreRNAFile(String seq, String str);
	public abstract Map<String, iDouble> scoreRNAFileWithStats(String seq, String str);
	
	public abstract iDouble[] get_kh_params();
	public abstract iDouble[] get_kh_alt_params();
	public abstract iDouble[] get_kh_perc();
	public abstract iDouble[] get_nucleotide_dist();
	public abstract iDouble[] get_pfold_params_u();
	public abstract iDouble[][] get_pfold_params_p();
	public abstract iDouble[][] get_pfold_ratio();
	
	static RNAFormattedFile getRNAFF(String seq, String nat){
		RNAFormattedFile rnaff = new RNAFormattedFile("temp");
		rnaff.put("seq", seq);
		rnaff.put("nat", nat);
		return rnaff;
	}
	
	public static void main(String[] args){
		Grammar g = new PfoldGrammar("name", new Display("Grammar"));
		boolean verbose = true;
		List<RNAFormattedFile> trainingSet = new ArrayList<RNAFormattedFile>();
		
		trainingSet.add(getRNAFF("a", "."));
		trainingSet.add(getRNAFF("ac", ".."));
		trainingSet.add(getRNAFF("GacC", "(..)"));
		trainingSet.add(getRNAFF("GuAacUC", "(.(..))"));
		trainingSet.add(getRNAFF("GAacUC", "((..))"));
		g.train_grammar(g, trainingSet, verbose);
	}

	public abstract HashMap<String, Integer> getCountsArray();

	public abstract int[] getTrainingCounts();
	
	public abstract RNAobj predict(RNAobj rna);
}
