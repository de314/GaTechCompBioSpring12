package desposito6;


import desposito6.math.MyBigDecimal;
import desposito6.math.iDouble;

public class DEMath {

	public static double SQRT_PRECISION = Double.MIN_NORMAL;
	public static int SQRT_ITERATIONS = 50;

	public static iDouble sqrt(iDouble arg) {
		iDouble x = arg.div(2);
		iDouble top = arg;
		iDouble bottom = new MyBigDecimal("0");
		int count = 0;
		iDouble diff = x.mult(x).sub(arg);
		while (diff.abs().sub(SQRT_PRECISION).isPositive()
				&& count++ < SQRT_ITERATIONS) {
			if (diff.isPositive())
				top = x;
			else
				bottom = x;
			diff = top.sub(bottom);
			x = diff.div(2).add(bottom);
			diff = x.mult(x).sub(arg);
		}
		return x;
	}

//	public static void main(String[] args) {
//		for (int i = 0; i < 20; i++)
//			testSqrt(i);
//	}
//
//	private static void testSqrt(double num) {
//		iDouble test = new MyBigDecimal(num);
//		double expected = Math.sqrt(num);
////		iDouble computed = sqrt(test);
//		iDouble computed = test.sqrt();
//		System.out.println(test.toPlainString() + "\n\texpected: " + expected
//				+ "\n\tcomputed: " + computed.doubleNotation() + "\n\terror:    " + 
//				computed.sub(expected).abs().toPlainString());
//	}
}
