package desposito6.testing;

public class Main {

	public static void testBigDoubleTime() {
		System.out.println("----------------");
		System.out.println("[ CONSTRUCTORS ]");
		System.out.println("----------------");
		testBigDoubleTimeConstructor();

		System.out.println("------------");
		System.out.println("[ ADDITION ]");
		System.out.println("------------");
		testBigDoubleTimeAdd();

		System.out.println("---------------");
		System.out.println("[ SUBTRACTION ]");
		System.out.println("---------------");
		testBigDoubleTimeSub();

		System.out.println("------------------");
		System.out.println("[ MULTIPLICATION ]");
		System.out.println("------------------");
		testBigDoubleTimeMult();
	}
	
	public static void testBigDoubleTimeConstructor() {
		System.out.println("Test Constructor Time 1,000");
		TestBigDouble.testConstructionTime(1000);

		System.out.println("\nTest Constructor Time 100,000");
		TestBigDouble.testConstructionTime(100000);

		System.out.println("\nTest Constructor Time 10,000,000");
		TestBigDouble.testConstructionTime(10000000);
	}

	public static void testBigDoubleTimeAdd() {
		System.out.println("\nTest Add Time 100,000");
		TestBigDouble.testAddTime(100000);

		System.out.println("\nTest Add Time 10,000,000");
		TestBigDouble.testAddTime(10000000);

		System.out.println("\nTest Add Actuation Time 100,000");
		TestBigDouble.testAddTime2(100000);

		System.out.println("\nTest Add Actuation Time 10,000,000");
		TestBigDouble.testAddTime2(10000000);
	}

	public static void testBigDoubleTimeSub() {
		System.out.println("\nTest Sub Time 100,000");
		TestBigDouble.testSubTime(100000);

		System.out.println("\nTest Sub Time 10,000,000");
		TestBigDouble.testSubTime(10000000);
	}

	public static void testBigDoubleTimeMult() {
		System.out.println("\nTest Mult Time 100,000");
		TestBigDouble.testMultTime(100000);

		System.out.println("\nTest Mult Time 10,000,000");
		TestBigDouble.testMultTime(10000000);
		
		System.out.println("\nTest Mult Actuation Time 100,000");
		TestBigDouble.testMultTime2(100000);

		System.out.println("\nTest Mult Actuation Time 10,000,000");
		TestBigDouble.testMultTime2(10000000);
	}
	
	

	public static void testBigDoubleAccuracy() {
		System.out.println("Test Constructor Accuracy 1.0E-10");
		TestBigDouble.testAccuracyConstructor(0.00000000001, "0.00000000001");

		System.out.println("\nTest Constructor Accuracy 1.0E10");
		TestBigDouble.testAccuracyConstructor(10000000000.0, "10000000000.0");

		System.out.println("\nTest Constructor Time 0.333");
		TestBigDouble.testAccuracyConstructor(0.333, "0.333");

		System.out.println("\nTest Add Accuracy 0.57x10");
		TestBigDouble.testAccuracyAdd(0.57, "0.57", 10);

		System.out.println("\nTest Add Accuracy 0.57x100");
		TestBigDouble.testAccuracyAdd(0.57, "0.57", 100);

		System.out.println("\nTest Mult Accuracy 0.5x5");
		TestBigDouble.testAccuracyMult(0.5, "0.5", 5);

		System.out.println("\nTest Mult Accuracy 0.5x10");
		TestBigDouble.testAccuracyMult(0.5, "0.5", 10);

		System.out.println("\nTest Mult Accuracy 0.5x25");
		TestBigDouble.testAccuracyMult(0.5, "0.5", 25);

		System.out.println("\nTest Mult Accuracy 0.5x100");
		TestBigDouble.testAccuracyMult(0.5, "0.5", 100);

		System.out.println("\nTest Mult Accuracy 0.5x1000");
		TestBigDouble.testAccuracyMult(0.5, "0.5", 1000);

		System.out.println("\nTest Mult2 Accuracy 0.5x25");
		TestBigDouble.testAccuracyMult2(0.5, "0.5", 25);

		System.out.println("\nTest Mult2 Accuracy 0.5x100");
		TestBigDouble.testAccuracyMult2(0.5, "0.5", 100);

		System.out.println("\nTest Mult2 Accuracy 0.5x1000");
		TestBigDouble.testAccuracyMult2(0.5, "0.5", 1000);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		testBigDoubleTime();
//
//		System.out.println("------------");
//		System.out.println("[ ACCURACY ]");
//		System.out.println("------------");
//		testBigDoubleAccuracy();
		
		TestBigDouble.testCompareTo();
	}

}
