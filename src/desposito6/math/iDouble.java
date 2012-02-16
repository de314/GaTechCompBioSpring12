package desposito6.math;

public abstract class iDouble {
	
	public static iDouble newInstance(){
		return null;
	}
	
	public static iDouble newInstance(double num){
		return null;
	}
	
	// ADD
	public abstract iDouble add(iDouble operand);
	
	public abstract iDouble add(double operand);
	
	// SUBTRACT
	public abstract iDouble sub(iDouble operand);
	
	public abstract iDouble sub(double operand);
	
	// MULTIPLY
	public abstract iDouble mult(iDouble operand);
	
	public abstract iDouble mult(double operand);
	
	// DIVIDE
	public abstract iDouble div(iDouble operand);
	
	public abstract iDouble div(double operand);
	
	// OTHER: MATH
	public abstract iDouble sqrt();
	
	public abstract iDouble pow(int power);
	
	public abstract iDouble abs();
	
	public abstract boolean isPositive();
	
	public abstract boolean isNegative();
	
	public abstract boolean isZero();
	
	public abstract iDouble min(iDouble operand);
	
	public abstract iDouble max(iDouble operand);
	
	// DISPLAY
	@Override
	public abstract String toString();
	
	public abstract String sciNotation();
	
	public abstract String doubleNotation();
	
	public abstract String toPlainString();
	
	// OTHER
	public abstract int compareTo(iDouble that);
	
	public abstract iDouble deepCopy();
	
	public abstract double doubleValue();
}
