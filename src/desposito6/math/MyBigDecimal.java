package desposito6.math;

import java.math.BigDecimal;
import java.math.MathContext;

import desposito6.DEMath;

public class MyBigDecimal extends iDouble {

	private BigDecimal num;
	private MathContext precision;

	public static iDouble newInstance() {
		return new MyBigDecimal();
	}

	public static iDouble newInstance(double num) {
		return new MyBigDecimal(num);
	}

	public MyBigDecimal() {
		this(0.0);
	}

	public MyBigDecimal(String num) {
		this(num, MathContext.DECIMAL128);
	}

	public MyBigDecimal(String num, MathContext precision) {
		this(new BigDecimal(num), precision);
	}

	public MyBigDecimal(double num) {
		this(num, MathContext.DECIMAL128);
	}

	public MyBigDecimal(double num, MathContext precision) {
		this(new BigDecimal(num), precision);
	}

	public MyBigDecimal(BigDecimal num) {
		this(num, MathContext.DECIMAL128);
	}

	public MyBigDecimal(BigDecimal num, MathContext precision) {
		this.num = num;
		this.precision = precision;
	}

	@Override
	public iDouble abs() {
		iDouble temp = new MyBigDecimal(num.abs().toPlainString(), precision);
		return temp;
	}

	@Override
	public iDouble add(iDouble operand) {
		iDouble temp = new MyBigDecimal(num.add(new BigDecimal(operand
				.toPlainString()), precision), precision);
		return temp;
	}

	@Override
	public iDouble add(double operand) {
		iDouble temp = new MyBigDecimal(num.add(new BigDecimal(operand),
				precision), precision);
		return temp;
	}

	@Override
	public int compareTo(iDouble that) {
		return num.compareTo(new BigDecimal(that.toPlainString()));
	}

	@Override
	public iDouble div(iDouble operand) {
		iDouble temp = new MyBigDecimal(num.divide(new BigDecimal(operand
				.toPlainString()), precision), precision);
		return temp;
	}

	@Override
	public iDouble div(double operand) {
		iDouble temp = new MyBigDecimal(num.divide(new BigDecimal(operand),
				precision), precision);
		return temp;
	}

	@Override
	public String doubleNotation() {
		return new Double(num.doubleValue()).toString();
	}

	@Override
	public boolean isNegative() {
		return num.compareTo(BigDecimal.ZERO) == 0;
	}

	@Override
	public boolean isPositive() {
		return num.compareTo(BigDecimal.ZERO) > 0;
	}

	@Override
	public boolean isZero() {
		return num.compareTo(BigDecimal.ZERO) < 0;
	}

	@Override
	public iDouble mult(iDouble operand) {
		iDouble temp = new MyBigDecimal(num.multiply(new BigDecimal(operand
				.toPlainString()), precision), precision);
		return temp;
	}

	@Override
	public iDouble mult(double operand) {
		iDouble temp = new MyBigDecimal(num.multiply(new BigDecimal(operand),
				precision), precision);
		return temp;
	}

	@Override
	public iDouble pow(int power) {
		if (power == 0)
			return new MyBigDecimal(1);
		iDouble temp = new MyBigDecimal(num.pow(power, precision), precision);
		return temp;
	}

	@Override
	public String sciNotation() {
		return num.toEngineeringString();
	}

	@Override
	public iDouble sqrt() {
		return DEMath.sqrt(this);
	}

	@Override
	public iDouble sub(iDouble operand) {
		iDouble temp = new MyBigDecimal(num.subtract(new BigDecimal(operand
				.toPlainString()), precision), precision);
		return temp;
	}

	@Override
	public iDouble sub(double operand) {
		iDouble temp = new MyBigDecimal(num.subtract(new BigDecimal(operand),
				precision), precision);
		return temp;
	}

	@Override
	public String toPlainString() {
		return num.toPlainString();
	}

	@Override
	public String toString() {
		return sciNotation();
	}

	@Override
	public iDouble deepCopy() {
		return new MyBigDecimal(new BigDecimal(num.toPlainString()));
	}

	@Override
	public iDouble max(iDouble operand) {
		return this.compareTo(operand) >= 0 ? this : operand;
	}

	@Override
	public iDouble min(iDouble operand) {
		return this.compareTo(operand) <= 0 ? this : operand;
	}

	@Override
	public double doubleValue() {
		return num.doubleValue();
	}
}
