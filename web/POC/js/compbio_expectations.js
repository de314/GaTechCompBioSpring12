function getExpectations(exp) {
	var params = getParamsFromGrammar(exp.grammar);
	exp.rho = getRho(params);
	exp.ExH = getExH(params, exp.rho);
	exp.ExBp = getExBp(params, exp.rho);
	exp.ExHp = getExHp(params, exp.rho);
	exp.ExLb = getExLb(params, exp.rho);
	exp.ExI = getExI(params, exp.rho);
	exp.ExM = getExM(params, exp.rho);
	exp.ExR3 = getExR(params, exp.rho, 3);
	exp.ExR4 = getExR(params, exp.rho, 4);
	exp.ExR5 = getExR(params, exp.rho, 5);
	exp.ExEH = getExEH(params, exp.rho);
	exp.ExECD = getExECD(params, exp.rho);
}

function getParamsFromGrammar(g) {
	var params = new Object();
	params.p1 = g.prob.kh[S_LS];
	params.q1 = g.prob.kh[S_L];
	params.p2 = g.prob.kh[L_dFd];
	params.q2 = g.prob.kh[L_t];
	params.p3 = g.prob.kh[F_dFd];
	params.q3 = g.prob.kh[F_LS];
	return params;
}

function getRho(p) {
	var poly = new Array(5);
	poly[0] = 1;
	poly[1] = -2 * p.p1 * p.q2;
	poly[2] = Math.pow(p.p1 * p.q2, 2) - p.p3;
	poly[3] = (2 * p.p1 * p.q2 * p.p3) - (4 * p.p2 * p.q1 * p.q2 * p.q3);
	poly[4] = -Math.pow(p.p1 * p.q2, 2) * p.p3;
	var r = rpSolve2(4, poly);
	if (r) {
		var min = -1;
		for (var i=0;i<4;i++)
			min = min < 0 || (!r.zeroi[i] && r.zeror[i] > 0 && r.zeror[i] < min) ? r.zeror[i] : min;
		return min;
	}
}

/*
 * ==================================== BASE PAIRS
 * ====================================
 */
function getExBp(p, rho) {
	return (1 - (p.p1 * p.q2 * rho))
			/ (3 - (p.p1 * p.q2 * rho) - (p.p3 * rho * rho) - (p.p1 * p.p3
					* p.q2 * Math.pow(rho, 3)));
}

/*
 * ==================================== HELICES
 * ====================================
 */
function getExH(p, rho) {
	return ((1 - (p.p1 * p.q2 * rho)) * (1 - (p.p3 * rho * rho)))
			/ (3 - (p.p1 * p.q2 * rho) - (p.p3 * rho * rho) - (p.p1 * p.p3 * p.q2 * Math
					.pow(rho, 3)));
}

/*
 * ==================================== LOOPS
 * ====================================
 */
function getExHp(p, rho) {
	return ((1 - (p.p1 * p.q2 * rho) * (1 - p.p3 * rho * rho)
			* (1 + p.p1 * p.q2 * rho)))
			/ (4 * (3 - (p.p1 * p.q2 * rho) - (p.p3 * rho * rho) - (p.p1 * p.p3
					* p.q2 * Math.pow(rho, 3))));
}

function getExLb(p, rho) {
	return ((1 - Math.pow((p.p1 * p.q2 * rho), 2) * (1 - p.p3 * rho * rho)))
			/ (4 * (3 - (p.p1 * p.q2 * rho) - (p.p3 * rho * rho) - (p.p1 * p.p3
					* p.q2 * Math.pow(rho, 3))));
}

function getExI(p, rho) {
	return ((p.p1*p.q2*rho)*(1 - (p.p1 * p.q2 * rho) * (1 - p.p3 * rho * rho)))
			/ (4 * (3 - (p.p1 * p.q2 * rho) - (p.p3 * rho * rho) - (p.p1 * p.p3
					* p.q2 * Math.pow(rho, 3))));
}

function getExM(p, rho) {
	return ((1 - (p.p1 * p.q2 * rho) * (1 - p.p3 * rho * rho)))
			/ (4 * (3 - (p.p1 * p.q2 * rho) - (p.p3 * rho * rho) - (p.p1 * p.p3
					* p.q2 * Math.pow(rho, 3))));
}

function getExR(p, rho, r) {
	return ((Math.pow(p.p1,r-2)*Math.pow(p.q2,r-2)*Math.pow(rho,r-2))*(1-(p.p1*p.q2*rho)*(1-(p.p3*rho*rho))))/(4 * Math.pow(1+(p.p1*p.q2*rho),r-1)*(3-(p.p1*p.q2*rho)-(p.p3*rho*rho)-(p.p1*p.p3*p.q2*Math.pow(rho, 3))));
}

/*
 * ==================================== External Loop
 * ====================================
 */
function getExEH(p, rho) {
	return (1 + (2 * p.p1 * p.q2 * rho));
}

function getExECD(p, rho) {
	return (1 + (5*p.p1*p.q2*rho)-(2*Math.pow(p.p1*p.q2*rho, 2)))/(1-(p.p1*p.q2*rho));
}