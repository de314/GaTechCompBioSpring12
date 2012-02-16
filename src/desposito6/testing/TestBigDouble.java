package desposito6.testing;

import java.math.BigDecimal;
import java.math.MathContext;

import desposito6.math.BigDouble;

public class TestBigDouble {
	
	public static void testCompareTo(){
		BigDouble arg1 = new BigDouble(5.12345, (short)-1206);
		System.out.println(arg1.isPositive());
	}

	private static void displaySpeedup(long bigDecimal, long bigDouble) {
		DisplayUtils.DisplaySpeedup("BigDecimal duration: ", bigDecimal,
				"BigDouble duration: ", bigDouble, "ms");
	}

	public static void testConstructionTime(int num) {
		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			new BigDouble(1234.4321);
		long durationBigDouble = System.currentTimeMillis() - start;

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			new java.math.BigDecimal(1234.4321);
		long durationBigDecimal = System.currentTimeMillis() - start;

		displaySpeedup(durationBigDecimal, durationBigDouble);
	}

	public static void testAddTime(int num) {
		BigDouble arg1, arg2;
		arg1 = new BigDouble(1234.4321);
		arg2 = new BigDouble(1234.4321);

		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			BigDouble.add(arg1, arg2);
		long durationBigDouble = System.currentTimeMillis() - start;

		java.math.BigDecimal bd1, bd2;
		bd1 = new java.math.BigDecimal(1234.4321);
		bd2 = new java.math.BigDecimal(1234.4321);

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			bd1.add(bd2, MathContext.DECIMAL64);
		long durationBigDecimal = System.currentTimeMillis() - start;

		displaySpeedup(durationBigDecimal, durationBigDouble);
	}

	public static void testAddTime2(int num) {
		BigDouble arg1, arg2;
		arg1 = new BigDouble(1234.4321);
		arg2 = new BigDouble(1234.4321);

		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			arg1.add(arg2);
		long durationBigDouble = System.currentTimeMillis() - start;

		java.math.BigDecimal bd1, bd2;
		bd1 = new java.math.BigDecimal(1234.4321);
		bd2 = new java.math.BigDecimal(1234.4321);

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			bd1 = bd1.add(bd2, MathContext.DECIMAL64);
		long durationBigDecimal = System.currentTimeMillis() - start;

		displaySpeedup(durationBigDecimal, durationBigDouble);
	}

	public static void testSubTime(int num) {
		BigDouble arg1, arg2;
		arg1 = new BigDouble(1234.4321);
		arg2 = new BigDouble(1234.4321);

		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			BigDouble.sub(arg1, arg2);
		long durationBigDouble = System.currentTimeMillis() - start;

		java.math.BigDecimal bd1, bd2;
		bd1 = new java.math.BigDecimal(1234.4321);
		bd2 = new java.math.BigDecimal(1234.4321);

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			bd1.subtract(bd2, MathContext.DECIMAL64);
		long durationBigDecimal = System.currentTimeMillis() - start;

		displaySpeedup(durationBigDecimal, durationBigDouble);
	}

	public static void testMultTime(int num) {
		BigDouble arg1, arg2;
		arg1 = new BigDouble(1234.4321);
		arg2 = new BigDouble(1234.4321);

		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			BigDouble.mult(arg1, arg2);
		long durationBigDouble = System.currentTimeMillis() - start;

		java.math.BigDecimal bd1, bd2;
		bd1 = new java.math.BigDecimal(1234.4321);
		bd2 = new java.math.BigDecimal(1234.4321);

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			bd1.multiply(bd2, MathContext.DECIMAL64);
		long durationBigDecimal = System.currentTimeMillis() - start;

		displaySpeedup(durationBigDecimal, durationBigDouble);
	}

	public static void testMultTime2(int num) {
		BigDouble arg1, arg2;
		arg1 = new BigDouble(1234.4321);
		arg2 = new BigDouble(1234.4321);

		long start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			arg1.multA(arg2);
		long durationBigDouble = System.currentTimeMillis() - start;

		java.math.BigDecimal bd1, bd2;
		bd1 = new java.math.BigDecimal(1234.4321);
		bd2 = new java.math.BigDecimal(1234.4321);

		start = System.currentTimeMillis();
		for (int i = 0; i < num; i++)
			bd1 = bd1.multiply(bd2, MathContext.DECIMAL64);
		long durationBigDecimal = System.currentTimeMillis() - start;

		displaySpeedup(durationBigDecimal, durationBigDouble);
	}

	private static void displayAccuracy(BigDecimal bigDecimal,
			BigDouble bigDouble) {
		DisplayUtils.DisplayAccuracyResults("BigDecimal value: ", bigDecimal
				.toString(), "BigDouble value: ", bigDouble
				.toPlainString());
	}

	public static void testAccuracyConstructor(double num, String snum) {
		BigDouble bDou = new BigDouble(num);
		BigDecimal bDec = new BigDecimal(snum);
		displayAccuracy(bDec, bDou);
	}

	public static void testAccuracyAdd(double num, String snum, int iters) {
		BigDouble bDou = new BigDouble(num);
		BigDecimal bDec = new BigDecimal(snum);
		for (int i = 0; i < iters; i++) {
			bDou.addA(num);
			bDec = bDec.add(new BigDecimal(num), MathContext.DECIMAL128);
		}
		displayAccuracy(bDec, bDou);
	}

	public static void testAccuracyAdd2(double num, String snum, int iters) {
		BigDouble bDou = new BigDouble(num);
		BigDecimal bDec = new BigDecimal(snum);
		for (int i = 0; i < iters; i++) {
			bDou = bDou.add(num);
			bDec = bDec.add(new BigDecimal(num), MathContext.DECIMAL128);
		}
		displayAccuracy(bDec, bDou);
	}

	public static void testAccuracyMult(double num, String snum, int iters) {
		BigDouble bDou = new BigDouble(num);
		BigDecimal bDec = new BigDecimal(snum);
		for (int i = 0; i < iters; i++) {
			bDou.multA(num);
			bDec = bDec.multiply(new BigDecimal(num), MathContext.DECIMAL128);
		}
		displayAccuracy(bDec, bDou);
	}

	public static void testAccuracyMult2(double num, String snum, int iters) {
		BigDouble bDou = new BigDouble(num);
		BigDouble bDou2 = new BigDouble(num);
		BigDecimal bDec = new BigDecimal(snum);
		BigDecimal bDec2 = new BigDecimal(snum);
		for (int i = 0; i < iters; i++) {
			bDou = bDou.mult(bDou2);
			bDec = bDec.multiply(bDec2, MathContext.DECIMAL128);
		}
		displayAccuracy(bDec, bDou);
	}
}
