package desposito6.utils;

public class CommandLine {

	public static int containsArg(String[] args, String arg) {
		for (int i = 0; i < args.length; i++)
			if (args[i].equals(arg))
				return i;
		return -1;
	}

	public static void DisplayBar(int total, int current, long time) {
		DisplayBar((int) ((double) (current * 100) / (double) total), time);
	}

	public static void DisplayBar(double total, double current, long time) {
		DisplayBar((int) ((total * 100) / current), time);
	}

	public static void DisplayBar(int percentage, long time) {
		System.out.print("\r\t[");

		for (int i = 0; i < 50; i++) {
			if (i < (percentage / 2)) {
				System.out.print("=");
			} else if (i == (percentage / 2)) {
				System.out.print(">");
			} else {
				System.out.print(" ");
			}
		}
		System.out.print("]   " + percentage + "% (" + time + "s)");
	}

	public static void DisplayBarFinish() {
		System.out
				.println("\r\t[=================================================>]   100%\n");
	}
}
