package scfg;

import java.io.Serializable;
import desposito6.math.*;

import scfg.utils.Compare;

/**
 * The Class RNAobj.
 */
public class RNAobj implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7703096242500384368L;
	
	/** The pred. */
	private String seq, nat, mfe, pred;

	/**
	 * Gets the seq.
	 * 
	 * @return the seq
	 */
	public String getSeq() {
		return seq;
	}

	/**
	 * Gets the nat.
	 * 
	 * @return the nat
	 */
	public String getNat() {
		return nat;
	}

	/**
	 * Gets the mfe.
	 * 
	 * @return the mfe
	 */
	public String getMfe() {
		return mfe;
	}

	/**
	 * Gets the pred.
	 * 
	 * @return the pred
	 */
	public String getPred() {
		return pred;
	}

	/**
	 * Sets the pred.
	 * 
	 * @param pred
	 *            the new pred
	 */
	public void setPred(String pred) {
		this.pred = pred;
	}

	/**
	 * Instantiates a new rN aobj.
	 * 
	 * @param seq
	 *            the seq
	 * @param nat
	 *            the nat
	 */
	public RNAobj(String seq, String nat) {
		this.seq = seq;
		this.nat = nat;
	}

	/**
	 * Instantiates a new rN aobj.
	 * 
	 * @param seq
	 *            the seq
	 * @param nat
	 *            the nat
	 * @param mfe
	 *            the mfe
	 */
	public RNAobj(String seq, String nat, String mfe) {
		this.seq = seq;
		this.nat = nat;
		this.mfe = mfe;
	}

	/**
	 * Gets the f measure mfe.
	 *
	 * @return the f measure mfe
	 */
	public BigDouble getFMeasureMFE() {
		if (nat == null || mfe == null || nat.length() != mfe.length())
			return null;
		return Compare.getFMeasureBD(nat, mfe);
	}

	/**
	 * Gets the f measure pred.
	 *
	 * @return the f measure pred
	 */
	public BigDouble getFMeasurePred() {
		if (nat == null || pred == null || nat.length() != pred.length())
			return null;
		return Compare.getFMeasureBD(nat, pred);
	}
}
