// Nathan Carlson
/**
 * FractionReducer.java
 */
package HW4;

/**
 *
 *
 * @author Nathan Carlson
 */
public class FractionReducer extends Worker {
	private int num;
	private int den;

	/**
	 * @param id
	 */
	public FractionReducer(int id, int _num, int _den) {
		super(id);
		num = _num;
		den = _den;
	}

	/* (non-Javadoc)
	 * @see hw3.Worker#doWork()
	 */
	@Override
	public void doWork() {
		int gcf = gcf(num, den);
		num /= gcf;
		den /= gcf;
	}

	private int gcf(int a, int b) {
        return b == 0 ? a : gcf(b, a % b);
    }

    public int getNumerator(){
        return num;
    }
    public int getDenominator(){
        return den;
    }
	/**
	 * Test harness
	 * @param args There are no arguments.
	 */
	public static void main(String[] args) {
		int num = Integer.parseInt(args[0]);
		int den = Integer.parseInt(args[1]);
		int taskId = 1;
		FractionReducer pc = new FractionReducer(taskId, num, den);
		pc.doWork();
		System.out.println(num + "/" + den + "->" + pc.getNumerator() + "/" + pc.getDenominator());
	}
}
