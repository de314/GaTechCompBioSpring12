package desposito6.math;

import java.io.Serializable;
import java.math.BigInteger;

/**
 * <pre>
 * The Class BigDouble. Faster Custom implementation of java.math.BigDecimal.
 * Initial tests show...
 * Construction Speedup:			 3.225 times faster
 * Addition Speedup:			24.414 times faster
 * Addition Accumulation Speedup:		46.857 times faster
 * Multiplication Speedup:		39.461 times faster
 * Multiplication Accumulation Speedup: 	61.961 times faster
 * </pre>
 * @author David Esposito - desposito6[at]gatech.edu
 * @version 1.0
 */
public class BigDouble extends iDouble implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 3687157586669413818L;

	/** The precision. */
	private double precision;

	/** The power. */
	private short power;

	/** The TOLERANCE. */
	public static double TOLERANCE;

	static {
		TOLERANCE = 0.00000001;
	}

	/*
	 * ------------------------------------------------------------------------
	 * CONSTRUCTORS
	 * ------------------------------------------------------------------------
	 */
	
	public static iDouble newInstance(){
		return new BigDouble();
	}
	
	public static iDouble newInstance(double num){
		return new BigDouble(num);
	}
	
	/**
	 * Instantiates a new big double.
	 */
	public BigDouble() {
		this(0.0, (short) 0);
	}

	/**
	 * Instantiates a new big double.
	 * 
	 * @param precision
	 *            the precision
	 */
	public BigDouble(double precision) {
		this(precision, (short) 0);
	}

	/**
	 * Instantiates a new big double.
	 * 
	 * @param copy
	 *            BigDouble to copy
	 */
	public BigDouble(BigDouble copy) {
		this(copy.precision, copy.power);
	}

	/**
	 * Instantiates a new big double.
	 * 
	 * @param precision
	 *            the precision
	 * @param power
	 *            the power
	 */
	public BigDouble(double precision, short power) {
		this.precision = precision;
		this.power = power;
		this.normalize();
	}

	/**
	 * Converts a double to the form $d.########$ where $d$ is a base
	 * 10 digit and $#$ is an optional digit. The double should always have an
	 * exponent of 0 while BigDouble stores the exponent in a 16bit short.
	 */
	private void normalize() {
		double abs = Math.abs(precision);
		if (abs == 0 || (abs < 10 && abs >= 1))
			return;
		double temp = Math.log10(abs);
		if (temp < 0)
			temp--;
		short newPower = (short) (temp + (temp < 0 ? -TOLERANCE : TOLERANCE));
		power += newPower;
		// TODO: possible to know when its supposed to be an integer????
		if (newPower != 0)
			precision /= Math.pow(10, newPower);
		abs = Math.abs(precision);
		if (abs < 1) {
			precision *= 10;
			power--;
			normalize();
		}
		if (Math.abs(precision) >= 10 && !Double.isInfinite(precision)) {
			precision /= 10;
			power++;
			normalize();
		}
	}

	/*
	 * ------------------------------------------------------------------------
	 * ARITHMATIC
	 * ------------------------------------------------------------------------
	 */
	/**
	 * Flips the precision sign of the BigDouble.
	 */
	public void negate() {
		precision *= -1;
	}

	/**
	 * Returns the BigDouble representing the square root. Default 50
	 * accuracy iterations is used.
	 * 
	 * @return the square root
	 */
	public BigDouble sqrt() {
		return sqrt(50);
	}

	/**
	 * Returns the BigDouble representing the square root.
	 * 
	 * @param accuracyIterations
	 *            the accuracy iterations to be used
	 * @return the square root
	 */
	public BigDouble sqrt(int accuracyIterations) {
		BigDouble rtn = new BigDouble(this);
		rtn.sqrtA(accuracyIterations);
		return rtn;
	}

	/**
	 * Changed the current object to equal its square root.
	 * Default 50 accuracy iterations are used.
	 */
	public void sqrtA() {
		sqrtA(50);
	}

	/**
	 * Changed the current object to equal its square root.
	 * 
	 * @param accuracyIterations
	 *            the accuracy iterations
	 */
	public void sqrtA(int accuracyIterations) {
		BigDouble x = this.div(3);
		BigDouble lastX = new BigDouble();
		for (int i = 0; i < accuracyIterations && x.compareTo(lastX) != 0; i++) {
			x = this.add(x.mult(x)).div(x.mult(2));
			lastX = x;
		}
		this.precision = x.precision;
		this.power = x.power;
		normalize(); // shouldnt need this, but just in case.
	}

	// [ SHIFT ]

	/*
	 * ------------------------------------------------------------------------
	 * ADDITION
	 * ------------------------------------------------------------------------
	 */
	/**
	 * Returns a new BigDouble representing the sum.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble add(int operand) {
		return add(new BigDouble(operand));
	}

	/**
	 * Returns a new BigDouble representing the sum.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble add(double operand) {
		return add(new BigDouble(operand));
	}

	/**
	 * Returns a new BigDouble representing the sum.
	 * 
	 * @param that
	 *            the that
	 * @return the big double
	 */
	public BigDouble add(BigDouble that) {
		BigDouble newBD = new BigDouble(this);
		newBD.addA(that);
		return newBD;
	}

	/**
	 * Changed the current object to equal the sum.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void addA(int operand) {
		addA(new BigDouble(operand));
	}

	/**
	 * Changed the current object to equal the sum.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void addA(double operand) {
		addA(new BigDouble(operand));
	}

	/**
	 * Changed the current object to equal the sum.
	 * 
	 * @param that
	 *            the that
	 */
	public void addA(BigDouble that) {
		if (this.power == that.power)
			this.precision += that.precision;
		else if (this.power > that.power) {
			this.precision += that.precision
					* Math.pow(10.0, that.power - this.power);
		} else {
			this.precision *= Math.pow(10.0, this.power - that.power);
			this.power = that.power;
			this.precision += that.precision;
		}
		normalize();
	}

	/*
	 * ------------------------------------------------------------------------
	 * SUBTRACTION
	 * ------------------------------------------------------------------------
	 */
	/**
	 * Returns a new BigDouble representing the difference.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble sub(BigDouble operand) {
		operand = operand.deepCopy();
		operand.negate();
		return add(operand);
	}

	/**
	 * Returns a new BigDouble representing the difference.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble sub(int operand) {
		return add(new BigDouble(-operand, (short) 0));
	}

	/**
	 * Returns a new BigDouble representing the difference.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble sub(double operand) {
		return add(new BigDouble(-operand, (short) 0));
	}

	/**
	 * Changed the current object to equal the difference.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void subA(BigDouble operand) {
		operand = operand.deepCopy();
		operand.negate();
		addA(operand);
	}

	/**
	 * Changed the current object to equal the difference.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void subA(int operand) {
		addA(new BigDouble(-operand, (short) 0));
	}

	/**
	 * Changed the current object to equal the difference.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void subA(double operand) {
		addA(new BigDouble(-operand, (short) 0));
	}

	/*
	 * ------------------------------------------------------------------------
	 * MULTIPLICATION
	 * ------------------------------------------------------------------------
	 */
	/**
	 * Returns a new BigDouble representing the product.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble mult(BigDouble operand) {
		BigDouble newBD = new BigDouble(this);
		newBD.multA(operand);
		return newBD;
	}

	/**
	 * Returns a new BigDouble representing the product.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble mult(int operand) {
		return mult(new BigDouble(operand));
	}

	/**
	 * Returns a new BigDouble representing the product.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble mult(double operand) {
		return mult(new BigDouble(operand));
	}

	/**
	 * Changed the current object to equal the product.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void multA(BigDouble operand) {
		this.precision *= operand.precision;
		this.power += operand.power;
		normalize();
	}

	/**
	 * Changed the current object to equal the product..
	 * 
	 * @param operand
	 *            the operand
	 */
	public void multA(int operand) {
		this.precision *= operand;
		normalize();
	}

	/**
	 * Changed the current object to equal the product.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void multA(double operand) {
		this.precision *= operand;
		normalize();
	}

	/*
	 * ------------------------------------------------------------------------
	 * DIVISION
	 * ------------------------------------------------------------------------
	 */
	/**
	 * Returns a new BigDouble representing the quotient.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble div(BigDouble operand) {
		BigDouble newBD = new BigDouble(this);
		newBD.divA(operand);
		return newBD;
	}

	/**
	 * Returns a new BigDouble representing the quotient.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble div(int operand) {
		return div(new BigDouble(operand));
	}

	/**
	 * Returns a new BigDouble representing the quotient.
	 * 
	 * @param operand
	 *            the operand
	 * @return the big double
	 */
	public BigDouble div(double operand) {
		return div(new BigDouble(operand));
	}

	/**
	 * Changed the current object to equal the quotient.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void divA(BigDouble operand) {
		this.precision /= operand.precision;
		this.power -= operand.power;
		normalize();
	}

	/**
	 * Changed the current object to equal the quotient.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void divA(int operand) {
		this.precision /= operand;
		normalize();
	}

	/**
	 * Changed the current object to equal the quotient.
	 * 
	 * @param operand
	 *            the operand
	 */
	public void divA(double operand) {
		this.precision /= operand;
		normalize();
	}

	/*
	 * ------------------------------------------------------------------------
	 * DISPLAY
	 * ------------------------------------------------------------------------
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return sciNotation();
	}

	/**
	 * <pre>
	 * Returns string formatted in the form 
	 * ${ [PRECISION] } E[POWER]$
	 * </pre>
	 * 
	 * @return the string
	 */
	public String sciNotation() {
		return "{ " + precision + " } E" + power;
	}

	/**
	 * Returns a string that represents what a double would
	 * print.
	 * 
	 * @return the string
	 */
	public String doubleNotation() {
		return (precision * Math.pow(10, power)) + "";
	}

	/**
	 * Returns a string representation of this BigDouble
	 * without an exponent field.
	 * 
	 * @return the string
	 */
	public String toPlainString() {
		StringBuilder sb;
		if (power != 0)
			sb = new StringBuilder((precision + "").replaceAll("\\.0$", "")
					.replaceAll("\\.", "").replaceAll("-", ""));
		else
			sb = new StringBuilder((precision + "").replaceAll("-", ""));
		int len = sb.length() - 1;
		if (power > 0) {
			if (power >= len)
				for (int i = 0; i < power - len; i++)
					sb.append("0");
			else
				sb.insert(power + 1, ".");
		} else if (power < 0) {
			int abs = Math.abs(power);
			for (int i = 0; i < abs - 1; i++)
				sb.insert(0, "0");
			sb.insert(0, "0.");
		}
		if (precision < 0)
			sb.insert(0, "-");
		return sb.toString();
	}

	/*
	 * ------------------------------------------------------------------------
	 * COMPARABLE
	 * ------------------------------------------------------------------------
	 */
	public int compareTo(BigDouble that) {
		BigDouble diff = this.sub(that);
		if(diff.precision > 0)
			return 1;
		else if (diff.precision < 0)
			return -1;
		return 0;
	}
	
	/**
	 * Compare to.
	 *
	 * @param that the that
	 * @return the int
	 */
	public int compareTo(double that) {
		return this.compareTo(new BigDouble(that));
	}
	
	public boolean isPositive(){
		return this.precision > 0;
	}
	
	public boolean isNegative(){
		return this.precision < 0;
	}
	
	public boolean isZero(){
		return this.precision == 0;
	}

	/*
	 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 * STATIC
	 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
	 */

	/**
	 * Adds the two operands.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble add(BigDouble operand0, BigDouble operand1) {
		return add(operand0, operand1.precision, operand1.power);
	}

	/**
	 * Returns a new object representing the sum of operand0 and operand1.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble add(BigDouble operand0, int operand1) {
		return add(operand0, operand1, (short) 0);
	}

	/**
	 * Returns a new object representing the sum of operand0 and operand1.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble add(BigDouble operand0, double operand1) {
		return add(operand0, operand1, (short) 0);
	}

	/**
	 * Returns a new object representing the sum of operand0 and operand1.
	 * 
	 * @param bigdouble
	 *            the bigdouble
	 * @param precision
	 *            the precision
	 * @param power
	 *            the power
	 * @return the big double
	 */
	private static BigDouble add(BigDouble bigdouble, double precision,
			short power) {
		BigDouble rtn = new BigDouble(precision, power);
		rtn.add(bigdouble);
		return rtn;
	}

	/**
	 * Returns a new object representing the difference between operand0 and
	 * operand 1. Subtracs operand 1 from operand0.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble sub(BigDouble operand0, BigDouble operand1) {
		return sub(operand0, operand1.precision, operand1.power);
	}

	/**
	 * Returns a new object representing the difference between operand0 and
	 * operand 1. Subtracs operand 1 from operand0.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble sub(BigDouble operand0, int operand1) {
		return sub(operand0, operand1, (short) 0);
	}

	/**
	 * Returns a new object representing the difference between operand0 and
	 * operand 1. Subtracs operand 1 from operand0.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble sub(BigDouble operand0, double operand1) {
		return sub(operand0, operand1, (short) 0);
	}

	/**
	 * Returns a new object representing the difference between operand0 and
	 * operand 1. Subtracs operand 1 from operand0.
	 * 
	 * @param bigdouble
	 *            the bigdouble
	 * @param precision
	 *            the precision
	 * @param power
	 *            the power
	 * @return the big double
	 */
	private static BigDouble sub(BigDouble bigdouble, double precision,
			short power) {
		BigDouble rtn = new BigDouble(precision, power);
		rtn.sub(bigdouble);
		return rtn;
	}

	/**
	 * Returns a new object representing the product of operand0 and
	 * operand 1.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble mult(BigDouble operand0, BigDouble operand1) {
		return mult(operand0, operand1.precision, operand1.power);
	}

	/**
	 * Returns a new object representing the product of operand0 and
	 * operand 1.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble mult(BigDouble operand0, int operand1) {
		return mult(operand0, operand1, (short) 0);
	}

	/**
	 * Returns a new object representing the product of operand0 and
	 * operand 1.
	 * 
	 * @param operand0
	 *            the operand0
	 * @param operand1
	 *            the operand1
	 * @return the big double
	 */
	public static BigDouble mult(BigDouble operand0, double operand1) {
		return mult(operand0, operand1, (short) 0);
	}

	/**
	 * Returns a new object representing the product of operand0 and
	 * operand 1.
	 * 
	 * @param bigdouble
	 *            the bigdouble
	 * @param precision
	 *            the precision
	 * @param power
	 *            the power
	 * @return the big double
	 */
	private static BigDouble mult(BigDouble bigdouble, double precision,
			short power) {
		BigDouble rtn = new BigDouble(precision, power);
		rtn.mult(bigdouble);
		return rtn;
	}

	public BigInteger toBigInteger() {
		return new BigInteger((int)this.doubleValue() + "");
	}
	
	public BigDouble deepCopy(){
		return new BigDouble(this);
	}

	public BigDouble min(BigDouble d) {
		return this.compareTo(d) > 0 ? d : this;
	}

	public BigDouble max(BigDouble d) {
		return this.compareTo(d) > 0 ? this : d;
	}

	public double doubleValue() {
		return precision * Math.pow(10, power);
	}

	public BigDouble pow(int i) {
		// TODO
		return null;
	}
	
	public BigDouble sqr(){
		return this.mult(this);
	}

	@Override
	public iDouble abs() {
		return new BigDouble(this.precision < 0 ? -1 * this.precision : precision, this.power);
	}

	@Override
	public iDouble add(iDouble operand) {
		if(operand instanceof BigDouble)
			return this.add((BigDouble)operand);
		// TODO: parse strings
		return null;
	}

	@Override
	public int compareTo(iDouble that) {
		if (that instanceof BigDouble)
			return this.compareTo((BigDouble)that);
		if (that instanceof MyBigDecimal)
			return -1 * that.compareTo(new MyBigDecimal(this.toPlainString()));
		return -1;
	}

	@Override
	public iDouble div(iDouble operand) {
		if (operand instanceof BigDouble)
			return this.div((BigDouble)operand);
		if (operand instanceof MyBigDecimal)
			return new MyBigDecimal(this.toPlainString()).div(operand);
		return new BigDouble(this.doubleValue() / operand.doubleValue());
	}

	@Override
	public iDouble mult(iDouble operand) {
		if (operand instanceof BigDouble)
			return this.mult((BigDouble)operand);
		if (operand instanceof MyBigDecimal)
			return new MyBigDecimal(this.toPlainString()).mult(operand);
		return new BigDouble(this.doubleValue() * operand.doubleValue());
	}

	@Override
	public iDouble sub(iDouble operand) {
		if (operand instanceof BigDouble)
			return this.sub((BigDouble)operand);
		if (operand instanceof MyBigDecimal)
			return new MyBigDecimal(this.toPlainString()).max(operand);
		return new BigDouble(this.doubleValue() - operand.doubleValue());
	}

	@Override
	public iDouble max(iDouble operand) {
		if (operand instanceof BigDouble)
			return this.max((BigDouble)operand);
		if (operand instanceof MyBigDecimal)
			return new MyBigDecimal(this.toPlainString()).max(operand);
		return new BigDouble(this.doubleValue() >= operand.doubleValue() ? this.doubleValue() : operand.doubleValue());
	}

	@Override
	public iDouble min(iDouble operand) {
		if (operand instanceof BigDouble)
			return this.min((BigDouble)operand);
		if (operand instanceof MyBigDecimal)
			return new MyBigDecimal(this.toPlainString()).min(operand);
		return new BigDouble(this.doubleValue() <= operand.doubleValue() ? this.doubleValue() : operand.doubleValue());
	}

	/*
	 * ------------------------------------------------------------------------
	 * TESTING
	 * ------------------------------------------------------------------------
	 */
	// public static void main(String[] args) {
	// BigDouble bd;
	// Double[] tests;
	// tests = new Double[] { 1.0, 1000.0, 1234.0, 10000000000.0,
	// -1000000000.0, 651321654984651.0, 0.00000001, 0.1,
	// 0.321564891321, -0.00000000000826593, 1234.4321 };
	// runTests(tests);
	//
	// // tests = new Double[] { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9,
	// // 0.99, 1.0 };
	// // runTests(tests);
	// }
	//
	// private static void runTests(Double[] tests) {
	// BigDouble bd;
	// for (int i = 0; i < tests.length; i++) {
	// System.out.println("\nTest #" + i + ": " + tests[i]);
	// bd = new BigDouble(tests[i]);
	// System.out.println("\t" + bd.toString() + "\n\t"
	// + bd.toPlainString());
	// }
	// }
}
