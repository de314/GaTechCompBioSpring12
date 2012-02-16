package scfg;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import desposito6.math.*;
import scfg.output.Display;

/**
 * The Class PfoldGrammar.
 */
public class PfoldGrammar extends KHGrammar {

	/** The U. */
	public static int A = 0, C = 1, G = 2, U = 3;

	/** The total nbp. */
	private int totalNBP;

	/** The nucleotide distribution. */
	private int[] countUnmatched, nucleotideDistribution;

	/** The count base pairs. */
	private int[][] countBasePairs;

	/** The Pfold_params unmatched. */
	private iDouble[] Pfold_paramsUnmatched;

	/** The Pfold_nuc dist. */
	private iDouble[] Pfold_nucDist;

	/** The Pfold_params base pairs. */
	private iDouble[][] Pfold_paramsBasePairs;

	/** The Pfold_ratio. */
	private iDouble[][] Pfold_ratio;

	/**
	 * Gets the pfold_params unmatched.
	 * 
	 * @return the pfold_params unmatched
	 */
	public iDouble[] getPfold_paramsUnmatched() {
		return Pfold_paramsUnmatched;
	}

	/**
	 * Gets the pfold_params base pairs.
	 * 
	 * @return the pfold_params base pairs
	 */
	public iDouble[][] getPfold_paramsBasePairs() {
		return Pfold_paramsBasePairs;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.KHGrammar#reset()
	 */
	@Override
	public void reset() {
		super.reset();
		initArrays();
	}

	private void resetCountsArrays() {
		countUnmatched = new int[5];
		nucleotideDistribution = new int[5];
		countBasePairs = new int[4][4];
	}

	/**
	 * Inits the arrays.
	 */
	private void initArrays() {
		resetCountsArrays();
		Pfold_paramsUnmatched = new iDouble[4];
		Pfold_nucDist = new iDouble[4];
		Pfold_paramsBasePairs = new iDouble[4][4];
		Pfold_ratio = new iDouble[4][4];
		if (precision == PrecisionValues._52bit) {
			for (int i = 0; i < 4; i++) {
				Pfold_nucDist[i] = new BigDouble(0);
				Pfold_paramsUnmatched[i] = new BigDouble(0);
				for (int j = 0; j < 4; j++) {
					Pfold_paramsBasePairs[i][j] = new BigDouble(0);
					Pfold_ratio[i][j] = new BigDouble(0);
				}
			}
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			for (int i = 0; i < 4; i++) {
				Pfold_nucDist[i] = new MyBigDecimal(0, context);
				Pfold_paramsUnmatched[i] = new MyBigDecimal(0, context);
				for (int j = 0; j < 4; j++) {
					Pfold_paramsBasePairs[i][j] = new MyBigDecimal(0, context);
					Pfold_ratio[i][j] = new MyBigDecimal(0, context);
				}
			}
		}
		totalNBP = 0;
	}
	
	/**
	 * Instantiates a new pfold grammar.
	 * 
	 * @param filename
	 *            the filename
	 */
	public PfoldGrammar() {
		super();
		initArrays();
	}

	/**
	 * Instantiates a new pfold grammar.
	 * 
	 * @param filename
	 *            the filename
	 */
	public PfoldGrammar(String trainingSetFilename, Display output) {
		super(trainingSetFilename, output);
		initArrays();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.KHGrammar#add(java.lang.String, java.lang.String)
	 */
	@Override
	public void add(String nat, String seq) {
		if (nat.matches("[().]+") && seq.toUpperCase().matches("[ACGU]+")) {
			super.add(nat);
			int[] pairs = getPairsArray(nat);
			totalNBP += NBP;
			setCountsArray(nat, seq, pairs, this.nucleotideDistribution, this.countUnmatched, this.countBasePairs);
			setProductionProbabilities();
		} else
			output
					.eout("Invalid Structure or sequence provided to PfoldGrammar Trainer.");
	}

	/**
	 * Gets the nucleotide index array.
	 * 
	 * @param seq
	 *            the seq
	 * @return the nucleotide index array
	 */
	public static int[] getNucleotideIndexArray(String seq) {
		seq = seq.toUpperCase();
		int[] seq2 = new int[seq.length()];
		char c;
		for (int i = 0; i < seq.length(); i++) {
			c = seq.charAt(i);
			switch (c) {
			case 'A':
				seq2[i] = A;
				break;
			case 'C':
				seq2[i] = C;
				break;
			case 'G':
				seq2[i] = G;
				break;
			case 'U':
				seq2[i] = U;
				break;
			default:
				seq2[i] = -1;
			}
		}
		return seq2;
	}

	/**
	 * Set the counts in an array of size 9. The indeces are defined below and
	 * correspond to the MACROS defined at the header of this file.
	 * 
	 * Sets counts in provided unmatched array as: [ 0: a 1: c 2: g 3: u 4:
	 * total_count ]
	 * 
	 * Sets counts in provided 2D-pair array as: [ a c g u 0: a aa ac ag au 1: c
	 * ca cc cg cu 2: g ga gc gg gu 3: u ua uc ug uu ]
	 * 
	 * @param nat
	 *            the nat
	 * @param seq
	 *            - The nucleotide sequence
	 * @param pairs
	 *            - An array denoting an indexes corresponding index in a base
	 *            pair
	 */
	private void setCountsArray(String nat, String seq, int[] pairs, int[] nucleotideDistribution, int[] countUnmatched, int[][] countBasePairs) {
		int[] seq2 = getNucleotideIndexArray(seq);
		char c;
		for (int i = 0; i < seq.length(); i++) {
			c = nat.charAt(i);
			nucleotideDistribution[seq2[i]]++;
			nucleotideDistribution[4]++;
			switch (c) {
			case '.':
				countUnmatched[seq2[i]]++;
				countUnmatched[4]++;
				break;
			case '(':
				countBasePairs[seq2[i]][seq2[pairs[i]]]++;
			default:
				break;
			}
		}
	}
	
	public static HashMap<String, Integer> getCountsArray(String seq, String nat) {
		
		// TODO: 
		
//		HashMap<String, Integer> rtn = super.
		return null;
	}

	/**
	 * Converts a KH-Grammar count array (size 9) to a production probabilities
	 * parameter array (size 3). The array will be set as follows:
	 * 
	 * probs[0] = p(s->A) probs[1] = p(s->C) probs[2] = p(s->G) probs[3] =
	 * p(s->U)
	 * 
	 * So we can claculate access the probabilities in the following way:
	 * 
	 * p(S->L) = probs[0] p(S->LS) = 1 - probs[0] p(L->s) = probs[1] p(L->dFd) =
	 * 1 - probs[1] p(F->LS) = probs[2] p(F->dFd) = 1 - probs[2]
	 * 
	 * **Note: if there exists 0 productions for a given non-terminal then it
	 * needs to be specified that both productions should have zero probability.
	 * If this is the case then probs[i] will equal -1. This is handled in the
	 * probabilities string.
	 * 
	 */
	private void setProductionProbabilities() {
		if (precision == PrecisionValues._52bit) {
			iDouble invalid = new BigDouble(-1);
			iDouble zero = new BigDouble(0);
			for (int i = 0; i < 4; i++) {
				Pfold_paramsUnmatched[i] = countUnmatched[4] == 0 ? invalid
						: new BigDouble(countUnmatched[i]).div(new BigDouble(
								countUnmatched[4]));
				Pfold_nucDist[i] = nucleotideDistribution[4] == 0 ? invalid
						: new BigDouble(nucleotideDistribution[i])
								.div(new BigDouble(nucleotideDistribution[4]));
				for (int j = 0; j < 4; j++) {
					Pfold_paramsBasePairs[i][j] = totalNBP == 0 ? invalid
							: new BigDouble(countBasePairs[i][j])
									.div(new BigDouble(totalNBP));
				}
			}
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					iDouble n = Pfold_paramsBasePairs[i][j];
					iDouble d = Pfold_paramsBasePairs[2][3];
					Pfold_ratio[i][j] = d.compareTo(zero) == 0 ? invalid : n
							.div(d);
				}
			}
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			iDouble invalid = new MyBigDecimal(-1, context);
			iDouble zero = new MyBigDecimal(0, context);
			for (int i = 0; i < 4; i++) {
				Pfold_paramsUnmatched[i] = countUnmatched[4] == 0 ? invalid
						: new MyBigDecimal(countUnmatched[i], context)
								.div(new MyBigDecimal(countUnmatched[4],
										context));
				Pfold_nucDist[i] = nucleotideDistribution[4] == 0 ? invalid
						: new MyBigDecimal(nucleotideDistribution[i], context)
								.div(new MyBigDecimal(
										nucleotideDistribution[4], context));
				for (int j = 0; j < 4; j++) {
					Pfold_paramsBasePairs[i][j] = totalNBP == 0 ? invalid
							: new MyBigDecimal(countBasePairs[i][j], context)
									.div(new MyBigDecimal(totalNBP, context));
				}
			}
			for (int i = 0; i < 4; i++) {
				for (int j = 0; j < 4; j++) {
					iDouble n = Pfold_paramsBasePairs[i][j];
					iDouble d = Pfold_paramsBasePairs[2][3];
					Pfold_ratio[i][j] = d.compareTo(zero) == 0 ? invalid : n
							.div(d);
				}
			}
		}
	}

	/**
	 * Set the string to display a formatted version of the production
	 * probabilites. The output will be appended to the current string. The
	 * string will display:
	 * 
	 * Knudson-Hein Grammar Production Probabilites:
	 * --------------------------------------------- S -> L (0.684211) | LS
	 * (0.315789) L -> s (0.730769) | dFd (0.269231) F -> LS (0.875) | dFd
	 * (0.125)
	 * 
	 * See get_production_probabilites for how to handle special cases.
	 * 
	 * @return the string
	 */
	public String toStringPfoldNucleotideDistribution() {
		StringBuilder ss = new StringBuilder();
		String[] bases = { "s->A ", "s->C ", "s->G ", "s->U " };
		ss.append("Pfold Grammar Nucleotide Distribution:\n");
		ss.append("--------------------------------------\n");
		iDouble zero;
		if (precision == PrecisionValues._52bit) {
			zero = new BigDouble(0);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			zero = new MyBigDecimal(0, context);
		}
		for (int i = 0; i < 4; i++) {
			ss.append(bases[i]);
			if (Pfold_paramsUnmatched[i].compareTo(zero) < 0) {
				ss.append(" (*)\n");
			} else {
				ss.append(" (").append(Pfold_nucDist[i].toString()).append(
						")\n");
			}
		}
		ss.append("\n");
		return ss.toString();
	}

	/**
	 * Set the string to display a formatted version of the production
	 * probabilites. The output will be appended to the current string. The
	 * string will display:
	 * 
	 * Knudson-Hein Grammar Production Probabilites:
	 * --------------------------------------------- S -> L (0.684211) | LS
	 * (0.315789) L -> s (0.730769) | dFd (0.269231) F -> LS (0.875) | dFd
	 * (0.125)
	 * 
	 * See get_production_probabilites for how to handle special cases.
	 * 
	 * @return the string
	 */
	public String toStringPfoldUnmatchedProbabilities() {
		StringBuilder ss = new StringBuilder();
		String[] unmatched = { "s->A ", "s->C ", "s->G ", "s->U " };
		ss.append("Pfold Grammar Un-Matched Production Probabilites:\n");
		ss.append("-------------------------------------------------\n");
		iDouble zero;
		if (precision == PrecisionValues._52bit) {
			zero = new BigDouble(0);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			zero = new MyBigDecimal(0, context);
		}
		for (int i = 0; i < 4; i++) {
			ss.append(unmatched[i]);
			if (Pfold_paramsUnmatched[i].compareTo(zero) < 0) {
				ss.append(" (*)\n");
			} else {
				ss.append(" (").append(Pfold_paramsUnmatched[i].toString())
						.append(")\n");
			}
		}
		ss.append("\n");
		return ss.toString();
	}

	/**
	 * Set the string to display a formatted version of the production
	 * probabilites. The output will be appended to the current string. The
	 * string will display:
	 * 
	 * Knudson-Hein Grammar Production Probabilites:
	 * --------------------------------------------- S -> L (0.684211) | LS
	 * (0.315789) L -> s (0.730769) | dFd (0.269231) F -> LS (0.875) | dFd
	 * (0.125)
	 * 
	 * See get_production_probabilites for how to handle special cases.
	 * 
	 * @return the string
	 */
	public String toStringPfoldBasePairProbabilities() {
		StringBuilder ss = new StringBuilder();
		ss.append("Pfold Grammar Base Pair Production Probabilites:\n");
		ss.append("-------------------------------------------------\n");
		int w = 10;
		ss.append(Display.buf("dFd->xFy", w, ' ')).append("\n").append(
				Display.buf(" y = ", w, ' '));
		ss.append(Display.buf("A", w, ' ')).append(Display.buf("C", w, ' '));
		ss.append(Display.buf("G", w, ' ')).append(Display.buf("U", w, ' '));
		ss.append("\n");
		String[] matchedY = { "    A", "x = C", "    G", "    U" };
		iDouble zero;
		if (precision == PrecisionValues._52bit) {
			zero = new BigDouble(0);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			zero = new MyBigDecimal(0, context);
		}
		for (int i = 0; i < 4; i++) {
			ss.append(Display.buf(matchedY[i] + "", w, ' '));
			for (int j = 0; j < 4; j++) {
				ss.append(Display.buf(Pfold_paramsBasePairs[i][j]
						.compareTo(zero) >= 0 ? Pfold_paramsBasePairs[i][j]
						.toString() : "*", w, ' '));
			}
			ss.append("\n");
		}
		ss.append("\n");
		return ss.toString();
	}

	/**
	 * To string pfold base pair ratio.
	 * 
	 * @return the string
	 */
	public String toStringPfoldBasePairRatio() {
		StringBuilder ss = new StringBuilder();
		ss.append("Pfold Grammar Base Pair Ratio to G-U:\n");
		ss.append("-------------------------------------\n");
		int w = 6;
		ss.append(Display.buf("dFd->xFy", w, ' ')).append("\n").append(
				Display.buf(" y = ", w, ' '));
		ss.append(Display.buf("A", w, ' ')).append(Display.buf("C", w, ' '));
		ss.append(Display.buf("G", w, ' ')).append(Display.buf("U", w, ' '));
		ss.append("\n");
		String[] matchedY = { "    A", "x = C", "    G", "    U" };
		iDouble zero;
		if (precision == PrecisionValues._52bit) {
			zero = new BigDouble(0);
		} else {
			MathContext context = MathContext.DECIMAL128;
			switch (precision) {
			case _32bit:
				context = MathContext.DECIMAL32;
			case _64bit:
				context = MathContext.DECIMAL64;
			}
			zero = new MyBigDecimal(0, context);
		}
		for (int i = 0; i < 4; i++) {
			ss.append(Display.buf(matchedY[i] + "", w, ' '));
			for (int j = 0; j < 4; j++) {
				ss
						.append(Display
								.buf(
										Pfold_ratio[i][j].compareTo(zero) >= 0 ? Pfold_ratio[i][j]
												.toString()
												: "*", w, ' '));
			}
			ss.append("\n");
		}
		ss.append("\n");
		return ss.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.KHGrammar#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder(super.toString());
		sb.append(toStringPfoldNucleotideDistribution());
		sb.append(this.toStringPfoldUnmatchedProbabilities());
		sb.append(this.toStringPfoldBasePairProbabilities());
		sb.append(this.toStringPfoldBasePairRatio());
		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see scfg.KHGrammar#deepCopy()
	 */
	@Override
	public Grammar deepCopy() {
		PfoldGrammar temp = new PfoldGrammar(super.name, output);
		temp.KH_params = copyArray(this.KH_params);
		temp.KH_percentages = copyArray(this.KH_percentages);
		temp.Pfold_nucDist = copyArray(this.Pfold_nucDist);
		temp.Pfold_paramsUnmatched = copyArray(this.Pfold_paramsUnmatched);
		temp.Pfold_paramsBasePairs = copyArray(this.Pfold_paramsBasePairs);
		temp.Pfold_ratio = copyArray(this.Pfold_ratio);
		return temp;
	}

	@Override
	public iDouble scoreRNAFile(String seq, String str) {
		iDouble prob = super.scoreRNAFile(seq, str);
		int[] pairs = getPairsArray(str);
		resetCountsArrays();
		setCountsArray(str, seq, pairs, this.nucleotideDistribution, this.countUnmatched, this.countBasePairs);
		for (int i = 0; i < Pfold_paramsUnmatched.length; i++)
			prob = prob.mult(Pfold_paramsUnmatched[i].pow(countUnmatched[i]));
		for (int i = 0; i < Pfold_paramsBasePairs.length; i++)
			for (int j = 0; j < Pfold_paramsBasePairs[i].length; j++)
				prob = prob.mult(Pfold_paramsBasePairs[i][j]
						.pow(countBasePairs[i][j]));
		return prob;
	}
	
	@Override
	public Map<String, iDouble> scoreRNAFileWithStats(String seq, String str) {
		iDouble prob = super.scoreRNAFile(seq, str);
		Map <String, Integer> counts = getCountsArray(str);
		Map <String, iDouble> rtn = new HashMap<String, iDouble>();
		
		for (int i = 0; i < Pfold_paramsUnmatched.length; i++)
			prob = prob.mult(Pfold_paramsUnmatched[i].pow(countUnmatched[i]));
		for (int i = 0; i < Pfold_paramsBasePairs.length; i++)
			for (int j = 0; j < Pfold_paramsBasePairs[i].length; j++)
				prob = prob.mult(Pfold_paramsBasePairs[i][j]
						.pow(countBasePairs[i][j]));
		
		rtn.put("prob", prob);
		for (Entry<String, Integer> me : counts.entrySet())
			rtn.put(me.getKey(), new MyBigDecimal(me.getValue()));	
		return rtn;
	}

	public static void main(String[] args) {
		PfoldGrammar pf = new PfoldGrammar("name", new Display("PfoldGrammar"));
		pf.add(".", "a");
		pf.add("..", "aa");
		pf.add("(..)", "gaac");
		pf.add("((..))", "ggaacu");
		pf.add("(.(..))", "gaaaauc");
		System.out.println(pf);

		System.out.println(pf.scoreRNAFile("ggaacc", "((..))"));
	}

	@Override
	public iDouble[] get_nucleotide_dist() {
		return Pfold_nucDist;
	}

	@Override
	public iDouble[][] get_pfold_params_p() {
		return Pfold_paramsBasePairs;
	}

	@Override
	public iDouble[] get_pfold_params_u() {
		return Pfold_paramsUnmatched;
	}

	@Override
	public iDouble[][] get_pfold_ratio() {
		return Pfold_ratio;
	}

	@Override
	public boolean setProbabilities(Map<String, iDouble> params) {
		if (super.setProbabilities(params)) {
			String[] headers1 = new String[] {"s->a","s->c","s->g","s->u"};
			iDouble[] temp = new iDouble[4];
			for(int i=0;i<headers1.length;i++) {
				temp[i] = params.get(headers1[i]);
				if (temp[i] == null)
					return false;
			}
			String[][] headers2 = new String[][] { new String[]{"ff->aa","ff->ac","ff->ag","ff->au"},
					new String[] {"ff->ca","ff->cc","ff->cg","ff->cu"},
					new String[] {"ff->ga","ff->gc","ff->gg","ff->gu"},
					new String[] {"ff->ua","ff->uc","ff->ug","ff->uu"}};
			iDouble[][] temp2 = new iDouble[4][4];
			for(int i=0;i<headers2.length;i++) {
				for(int j=0;j<headers2[0].length;j++){
					temp2[i][j] = params.get(headers2[i][j]);
					if (temp2[i][j] == null)
						return false;
				}
			}
			Pfold_paramsUnmatched = temp;
			Pfold_paramsBasePairs = temp2;
			return true;
		}
		return false;
	}

	@Override
	public RNAobj predict(RNAobj rna) {
		String pred = CYK.predictFromGrammar(rna.getSeq(), this);
		RNAobj rna2 = new RNAobj(rna.getSeq(), rna.getNat());
		rna2.setPred(pred);
		return rna2;
	}
}
