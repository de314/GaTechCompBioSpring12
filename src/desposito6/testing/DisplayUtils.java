package desposito6.testing;

import java.math.BigDecimal;
import java.math.MathContext;

public class DisplayUtils {

	public static void DisplaySpeedup(String origDesc, long durationOrig,
			String newDesc, long durationNew, String unitsOfTime) {
		System.out.println("\t" + origDesc + ": " + durationOrig + unitsOfTime);
		System.out.println("\t" + newDesc + ": " + durationNew + unitsOfTime);
		System.out.println("\tSpeedup: " + ((double)durationOrig / (double)durationNew));
	}

	public static void DisplayAccuracyResults(String origDesc, String origValue,
			String newDesc, String newValue) {
		System.out.println("\t" + origDesc + ": " + origValue);
		System.out.println("\t" + newDesc + ": " + newValue);
		BigDecimal origVal = new BigDecimal(origValue);
		BigDecimal newVal = new BigDecimal(newValue);
		BigDecimal difference = origVal.subtract(newVal).abs();
		BigDecimal percent = difference.divide(origVal, MathContext.DECIMAL128);
		System.out.println("\tDifference: " + difference.toPlainString());
		System.out.println("\tPercentage Differece: " + percent.toPlainString());
	}

}
