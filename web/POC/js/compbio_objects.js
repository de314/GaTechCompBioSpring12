// Array Indices
var A = 0;
var C = 1;
var G = 2;
var U = 3;
var nuc_arr = [ A, C, G, U ];
var S     = 0;
var S_LS  = 1;
var S_L   = 2;
var L     = 3;
var L_t   = 4;
var L_dFd = 5;
var F     = 6;
var F_dFd = 7;
var F_LS  = 8;
var kh_arr = [ S_LS, S_L, L_t, L_dFd, F_dFd, F_LS ];
// Training Count Variables
var NUN = 0;
var NBP = 0;
// Other
var num_dec = 3;

/*
 * class TrainingSet
 * 
 * Variables:
 * ----------
 * htmlDOM ele
 * string name
 * Array<Sequence> sequences
 * Color color
 * PfoldGrammar grammar 		// set in training-front.train_grammars();
 * 
 * Methods:
 * --------
 * add(Sequence seq)
 * remove(htmlDOM.id id)
 * clear()
 */
function TrainingSet(ele, name, seqs, color) {
	this.ele = ele;
	this.name = name;
	this.sequences = seqs ? seqs : {};
	this.color = color ? color : '#FFF';
	this.add = function(seq) {
		if (seq && $(seq.ele).attr('id') && seq.seq && seq.str)
			this.sequences[$(seq.ele).attr('id')] = seq;
	};
	this.remove = function(id) {
		if (this.sequences[id]) {
			delete sequences[id];
			resetLists();
		}
	};
	this.clear = function() {
		this.sequences = new Array();
	};
}

/*
 * class Sequence
 * 
 * Variables:
 * ----------
 * htmlDOM ele
 * String name
 * String seq
 * String str
 * 
 * Methods:
 * --------
 * output()
 */
function Sequence(ele, name, seq, str) {
	this.ele = ele;
	this.name = name;
	this.seq = seq;
	this.str = str;
	this.output = function() {
		return this.name + ": " + this.seq + " - " + this.str;
	};
}

/*
 * class PfoldGrammar
 * 
 * Variables:
 * ----------
 * string name
 * Object counts
 * 		int NUN		// Number Unmatched Nucleotides
 * 		int NBP		// Number of Base Pairs
 * 		int[] kh	// Knudson-Hein 
 * 		int[] nuc	// Nucleotide Distribution
 * 		int[] upn	// Unpaired Nucleotides
 * 		int[][] bp	// Base Pairs
 * Object prob
 * 		double[] kh		// Knudson-Hein
 * 		double[] nuc	// Nucleotide Distribution
 * 		double[] upn	// Unpaired Nucleotides
 * 		double[][] bp	// Base Pairs
 * 
 * Methods:
 * --------
 * 
 */
function PfoldGrammar(name, seqs) {
	this.name = name;
	this.seqs = seqs ? seqs : new Array();
	this.counts = new Object();
	this.counts.NUN = 0;
	this.counts.NBP = 0;
	this.counts.kh = new Array(0,0,0,0,0,0,0,0,0);
	this.counts.nuc = new Array(0,0,0,0);
	this.counts.upn = new Array(0,0,0,0);
	this.counts.bp = new Array();
	for (var i=0;i<4;i++)
		this.counts.bp[i] = new Array(0,0,0,0);
	
	this.prob = new Object();
	this.prob.kh = new Array(0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0,0.0);
	this.prob.nuc = new Array(0.0,0.0,0.0,0.0);
	this.prob.upn = new Array(0.0,0.0,0.0,0.0);
	this.prob.bp = new Array();
	for (var i=0;i<4;i++)
		this.prob.bp[i] = new Array(0.0,0.0,0.0,0.0);
	this.output = function() {
		var out = "<b>" + this.name + " (probabilities):</b>\n\n";
		out += "Knudson-Hein:\n";
		out += "S->LS\t" + this.prob.kh[S_LS].toFixed(num_dec) + "\tS->L\t" + this.prob.kh[S_L].toFixed(num_dec);
		out += "\nL->t\t" + this.prob.kh[L_t].toFixed(num_dec) + "\tL->dFd\t" + this.prob.kh[L_dFd].toFixed(num_dec);
		out += "\nF->dFd\t" + this.prob.kh[F_dFd].toFixed(num_dec) + "\tF->LS\t" + this.prob.kh[F_LS].toFixed(num_dec);
		out += "\n\nPfold Unpaired Nucleotides:\nA\tC\tG\tU\n";
		var i,j;
		for (i=0;i<4;i++)
			out += this.prob.upn[i].toFixed(num_dec) + "\t";
		out += "\n\nPfold Base Pairs:\n\tA\tC\tG\tU\n";
		var arr = new Array("A", "C", "G", "U");
		var i,j;
		for (i=0;i<4;i++) {
			out += arr[i] + "\t";
			for (j=0;j<4;j++)
				out += this.prob.bp[i][j].toFixed(num_dec) + "\t";
			out += "\n";
		}
		out += "\n\nNucleotide Distribution:\nA\tC\tG\tU\n";
		var i,j;
		for (i=0;i<4;i++)
			out += this.prob.nuc[i].toFixed(num_dec) + "\t";
		return out;
	};
	this.output_counts = function() {
		var out = "<b>" + this.name + " (Counts):</b>\n\n";
		out += "Knudson-Hein Counts:\n";
		out += "S->LS\t" + this.counts.kh[S_LS] + "\tS->L\t" + this.counts.kh[S_L];
		out += "\nL->t\t" + this.counts.kh[L_t] + "\tL->dFd\t" + this.counts.kh[L_dFd];
		out += "\nF->dFd\t" + this.counts.kh[F_dFd] + "\tF->LS\t" + this.counts.kh[F_LS];
		out += "\n\nPfold Unpaired Nucleotides Counts:\nA\tC\tG\tU\n";
		var i,j;
		for (i=0;i<4;i++)
			out += this.counts.upn[i] + "\t";
		out += "\n\nPfold Base Pair Counts:\n\tA\tC\tG\tU\n";
		var arr = new Array("A", "C", "G", "U");
		var i,j;
		for (i=0;i<4;i++) {
			out += arr[i] + "\t";
			for (j=0;j<4;j++)
				out += this.counts.bp[i][j] + "\t";
			out += "\n";
		}
		out += "\n\nNucleotide Counts:\nA\tC\tG\tU\n";
		var i,j;
		for (i=0;i<4;i++)
			out += this.counts.nuc[i] + "\t";
		return out;
	};
	this.csv_counts = function() {
		var out = this.name + "\n";
		out += "Knudson-Hein:\n";
		out += "S->LS,\tS->L,\tL->t,\tL->dFd,\tF->dFd,\tF->LS\n";
		
		out += this.counts.kh[S_LS] + ",\t" + this.counts.kh[S_L] + ",\t" + this.counts.kh[L_t] + "\t,";
		out += this.counts.kh[L_dFd] + ",\t" + this.counts.kh[F_dFd] + ",\t" + this.counts.kh[F_LS];
		out += "\nPfold Unpaired Nucleotides:\nA,\tC,\tG,\tU\n";
		var i;
		for (i=0;i< 3;i++)
			out += this.counts.upn[i] + ",\t";
		out += this.counts.upn[3];
		// A*
		out += "\nPfold Base Pair:\nAA,\tAC,\tAG,\tAU\n";
		for (i=0;i< 3;i++)
			out += this.counts.bp[A][i] + ",\t";
		out += this.counts.bp[A][3];
		// C*
		out += "\nCA,\tCC,\tCG,\tCU\n";
		for (i=0;i< 3;i++)
			out += this.counts.bp[C][i] + ",\t";
		out += this.counts.bp[C][3];
		// G*
		out += "\nGA,\tGC,\tGG,\tGU\n";
		for (i=0;i< 3;i++)
			out += this.counts.bp[G][i] + ",\t";
		out += this.counts.bp[G][3];
		// U*
		out += "\nUA,\tUC,\tUG,\tUU\n";
		for (i=0;i< 3;i++)
			out += this.counts.bp[U][i] + ",\t";
		out += this.counts.bp[U][3];
		
		out += "\nNucleotide Distribution:\nA,\tC,\tG,\tU\n";
		var i,j;
		for (i=0;i< 3;i++)
			out += this.counts.nuc[i] + ",\t";
		out += this.counts.nuc[3];
		return out;
	};
}

function Expectations(grammar) {
	this.grammar = grammar;
	this.rho = 0;
	this.ExH = 0;
	this.ExBp = 0;
	this.ExHp = 0;
	this.ExLb = 0;
	this.ExI = 0;
	this.ExM = 0;
	this.ExR3 = 0;
	this.ExR4 = 0;
	this.ExR5 = 0;
	this.ExEH = 0;
	this.ExECD = 0;
	this.calculate = function() {
		getExpectations(this);
		return this;
	};
	this.output = function() {
		var out = "RNA Motif Expectations for " + this.grammar.name + "\n\n\t";
		out += "\\rho_0 = " + this.rho;
		out += "\n\n\tHelices: " + this.ExH;
		out += " * n\n\n\tBasepairs: " + this.ExBp;
		out += " * n\n\n\tHairpin Loops: " + this.ExHp;
		out += " * n\n\n\tBulge Loops: " + this.ExLb;
		out += " * n\n\n\tInternal: " + this.ExI;
		out += " * n\n\n\tMulti-Loops: " + this.ExM;
		out += " * n\n\n\tMulti-Loops (degree 3): " + this.ExR3;
		out += " * n\n\n\tMulti-Loops (degree 4): " + this.ExR4;
		out += " * n\n\n\tMulti-Loops (degree 5): " + this.ExR5;
		out += " * n\n\n\tHelices in External Loop: " + this.ExEH;
		out += " * n\n\n\t5'-3' Distance in External Loop: " + this.ExECD + "* n";
		return out;
	}
}
