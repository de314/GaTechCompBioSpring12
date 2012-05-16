

function train_grammar(ts) {
	var g = new PfoldGrammar(ts.name, ts.sequences);
	NUN = 0;
	NBP = 0;
	for (var ind in ts.sequences) {
		var seq = ts.sequences[ind];
		var pairs = getPairsArraySt(seq.str);
		train_on_sequences_kh(seq.str, pairs, 0, seq.str.length-1, false, g.counts.kh);
		train_on_sequences_pf(seq.seq, seq.str, pairs, g);
	}
	// Set Knudson-Hein Counts
	g.counts.NUN = NUN;
	g.counts.NBP = NBP;
	g.counts.kh[L_t] += NUN;
	g.counts.kh[F] += NBP;
	g.counts.kh[S] = g.counts.kh[S_L] + g.counts.kh[S_LS];
	g.counts.kh[L] = g.counts.kh[L_t] + g.counts.kh[F] - g.counts.kh[F_dFd];
	g.counts.kh[F_LS] = g.counts.kh[F] - g.counts.kh[F_dFd];
	// Set Knudson-Hein Probabilities
	g.prob.kh[S] = 1;
	g.prob.kh[S_L] = g.counts.kh[S_L] / g.counts.kh[S];
	g.prob.kh[S_LS] = g.counts.kh[S_LS] / g.counts.kh[S];
	g.prob.kh[L] = 1;
	g.prob.kh[L_t] = g.counts.kh[L_t] / g.counts.kh[L];
	g.prob.kh[L_dFd] = g.counts.kh[L_dFd] / g.counts.kh[L];
	g.prob.kh[F] = 1;
	g.prob.kh[F_dFd] = g.counts.kh[F_dFd] / g.counts.kh[F];
	g.prob.kh[F_LS] = g.counts.kh[F_LS] / g.counts.kh[F];
	// Set Pfold Probabilities
	var i;
	var total = 0;
	// Nucleotide Distribution
	for (i=0;i<4;i++)
		total += g.counts.nuc[i];
	for (i=0;i<4;i++)
		g.prob.nuc[i] = g.counts.nuc[i] / total;
	total = 0;
	// Unpaired Nucleotide Probabilities
	for (i=0;i<4;i++)
		total += g.counts.upn[i];
	for (i=0;i<4;i++)
		g.prob.upn[i] = g.counts.upn[i] / total;
	var j;
	total = 0;
	// Base Pair Nucleotide Probabilites
	for (i=0;i<4;i++)
		for (j=0;j<4;j++)
			total += g.counts.bp[i][j];
	for (i=0;i<4;i++)
		for (j=0;j<4;j++)
			g.prob.bp[i][j] = g.counts.bp[i][j] / total;
	
	return g;
}

function train_on_sequences_kh(nat, pairs, i, j, fromS, counts) {
	if (i > j || i >= nat.length)
		return;
	for (; i <= j; i++) {
		var c = nat.charAt(i);
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
			if (i > 0 && pairs[i - 1] == pairs[i] + 1)
				counts[F_dFd]++;
			else
				counts[L_dFd]++;
			train_on_sequences_kh(nat, pairs, i + 1, pairs[i] - 1, false, counts);
			i = pairs[i];
		}
		fromS = true;
	}
}

function train_on_sequences_pf(seq, nat, pairs, g) {
	var seq2 = getNucleotideIndexArray(seq);
	var c;
	for (var i = 0; i < seq.length; i++) {
		c = nat.charAt(i);
		g.counts.nuc[seq2[i]]++;
		g.counts.nuc[4]++;
		switch (c) {
		case '.':
			g.counts.upn[seq2[i]]++;
			g.counts.upn[4]++;
			break;
		case '(':
			g.counts.bp[seq2[i]][seq2[pairs[i]]]++;
		default:
			break;
		}
	}
}

function getPairsArraySt(nat) {
	var stk = new Array();
	var pairs = new Array();
	var size = nat.length;
	for (var i = 0; i < size; i++) {
		pairs[i] = -1;
		var c = nat.charAt(i);
		if (c == '(') {
			stk.push(i);
			NBP++;
		} else if (c == ')') {
			pairs[i] = stk.pop();
			pairs[pairs[i]] = i;
		} else
			NUN++;
	}
	return pairs;
}

function getNucleotideIndexArray(seq) {
	seq = seq.toUpperCase();
	var seq2 = new Array();
	var c;
	for (var i = 0; i < seq.length; i++) {
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