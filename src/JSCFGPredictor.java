import scfg.Grammar;
import scfg.PfoldGrammar;
import scfg.RNAobj;
import scfg.output.RNAFormattedFile;
import scfg.utils.RnaFileHandler;

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

import desposito6.math.MyBigDecimal;
import desposito6.math.iDouble;

/*
 * S -> LS  (0.868534) | L   (0.131466)
 * F -> dFd (0.787640) | LS  (0.212360)
 * L -> s   (0.894603) | dFd (0.105397)
 * 
 *     A        U        G        C
 * 0.364097 0.273013 0.211881 0.151009
 * 
 *       A        U        G        C
 * A 0.001167 0.177977 0.001058 0.001806 
 * U 0.177977 0.002793 0.049043 0.000763 
 * G 0.001058 0.049043 0.000406 0.266974 
 * C 0.001806 0.000763 0.266974 0.000391
 */
public class JSCFGPredictor {
	
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
	
	public static Grammar getGrammar(String filename) {
		return JSCFG.processParameters(filename);
	}
	
	public static List<File> predictAndPrint(Grammar g, String dirName) {
		File dir = new File(dirName);
		if (dir.exists() && dir.isDirectory()) {
			Map<String, RNAobj> rnas = new HashMap<String, RNAobj>();
			File[] files = dir.listFiles();
			for (File f : files) {
				if (f.isFile() && f.getName().endsWith("ct")) {
					RNAFormattedFile rnaf = RnaFileHandler.convertCtFile(f);
					rnas.put(f.getName(), g.predict(new RNAobj(rnaf.get("seq"), rnaf.get("nat"))));
				} else if (!f.isDirectory()) {
					rnas.put(f.getName(), g.predict(new RNAobj(RnaFileHandler.parseFastaFile(f), null)));
				}
			}
			List<File> rtn = new LinkedList<File>();
			new File(dirName+"/pred/").mkdirs();
			for (Entry<String, RNAobj> me : rnas.entrySet()) {
				File f = new File(dirName+"/pred/"+me.getKey());
				RnaFileHandler.writeCTFile(f.getName(), me.getValue().getSeq(), me.getValue().getPred());
				rtn.add(f);
			}
			return rtn;
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dirName = ".";
		if (args.length == 1)
			dirName = args[0];
		predictAndPrint(getGrammar(), dirName);
	}

}
