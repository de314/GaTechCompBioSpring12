package scfg.utils;

import java.math.BigDecimal;
import java.util.*;

import desposito6.math.MyBigDecimal;
import desposito6.math.iDouble;

import scfg.Grammar;
import scfg.PfoldGrammar;

import flanagan.complex.Complex;
import flanagan.math.ArrayMaths;
import flanagan.math.Polynomial;

public class RnaExpectations {

	public static Map<String, iDouble> getParams(Grammar g) {
		Map<String, iDouble> map = new HashMap<String, iDouble>();
		iDouble[] params = g.get_kh_params();
		iDouble one = new MyBigDecimal(1);
		map.put("p1", one.sub(params[0]));
		map.put("q1", params[0]);
		map.put("p2", one.sub(params[1]));
		map.put("q2", params[1]);
		map.put("p3", one.sub(params[2]));
		map.put("q3", params[2]);
		map.put("-1", new MyBigDecimal(-1));
		map.put("0", new MyBigDecimal(0));
		return map;
	}

	/**
	 * http://www.ee.ucl.ac.uk/~mflanaga/java/Polynomial.html#construct
	 * 
	 * @return
	 */
	public static BigDecimal getRoot(Grammar g) {
		double[] coef = new double[5];
		Map<String, iDouble> p = getParams(g);
		coef[4] = p.get("p1").pow(2).mult(p.get("p3")).mult(p.get("q2"))
				.mult(p.get("q2")).mult(-1).doubleValue();
		coef[3] = p.get("p1").mult(p.get("p2")).mult(p.get("p3")).mult(2)
				.doubleValue();
		iDouble temp1 = p.get("p2").mult(p.get("q1")).mult(p.get("q2"))
				.mult(p.get("q3")).mult(4);
		coef[2] = p.get("p1").mult(p.get("q2")).sub(p.get("p3")).sub(temp1)
				.doubleValue();
		coef[1] = p.get("p1").mult(p.get("q2")).mult(2).doubleValue();
		coef[0] = 1.0;
		Polynomial poly = new Polynomial(coef);
		Complex[] comp = poly.roots();
		double min = Double.MAX_VALUE;
		for (Complex c : poly.roots())
			if (c.isReal() && c.getReal() > 0)
				min = min < c.getReal() ? min : c.getReal();
		return new BigDecimal(min);
	}

	public static void main(String[] args) {
		System.out.println(getRoot(getGrammar()));
	}
	
	private static Map<String, iDouble> getParams() {
		Map<String, iDouble> params = new HashMap<String, iDouble>();
		params.put("S->L", new MyBigDecimal(0.131466));
		params.put("L->s", new MyBigDecimal(0.894603));
		params.put("F->LS", new MyBigDecimal(0.212360));
		params.put("s->a", new MyBigDecimal(0.364097));
		params.put("s->c", new MyBigDecimal(0.151009));
		params.put("s->g", new MyBigDecimal(0.211881));
		params.put("s->u", new MyBigDecimal(0.211881));
		params.put("ff->aa", new MyBigDecimal(0.001167));
		params.put("ff->ac", new MyBigDecimal(0.001806));
		params.put("ff->ag", new MyBigDecimal(0.001058));
		params.put("ff->au", new MyBigDecimal(0.177977));
		params.put("ff->ca", new MyBigDecimal(0.001806));
		params.put("ff->cc", new MyBigDecimal(0.000391));
		params.put("ff->cg", new MyBigDecimal(0.266974));
		params.put("ff->cu", new MyBigDecimal(0.000763));
		params.put("ff->ga", new MyBigDecimal(0.001058));
		params.put("ff->gc", new MyBigDecimal(0.266974));
		params.put("ff->gg", new MyBigDecimal(0.000406));
		params.put("ff->gu", new MyBigDecimal(0.049043));
		params.put("ff->ua", new MyBigDecimal(0.177977));
		params.put("ff->uc", new MyBigDecimal(0.000763));
		params.put("ff->ug", new MyBigDecimal(0.049043));
		params.put("ff->uu", new MyBigDecimal(0.002793));
		return params;
	}
	
	public static Grammar getGrammar() {
		Grammar g = new PfoldGrammar();
		g.setProbabilities(getParams());
		return g;
	}
}
