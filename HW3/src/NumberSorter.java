/**
 * NumberSorter.java
 */
package HW3;

/**
 *
 * 
 * @author Nathan Carlson
 */
public class NumberSorter extends Worker {
	private int m_1;
	private int m_2;
	private int m_3;
	
	/**
	 * @param id
	 */
	public NumberSorter(int id, int _1, int _2, int _3) {
		super(id);
		m_1 = _1;
		m_2 = _2;
		m_3 = _3;
	}

	/* (non-Javadoc)
	 * @see hw3.Worker#doWork()
	 */
	@Override
	public void doWork() {
	    int t;
		if (m_1 > m_2){
		    t = m_1;
		    m_1 = m_2;
		    m_2 = t;
		}
        if (m_2 > m_3){
		    t = m_2;
		    m_2 = m_3;
		    m_3 = t;
		}
        if (m_1 > m_2){
		    t = m_1;
		    m_1 = m_2;
		    m_2 = t;
		}

	}

    public int getSmallest(){
        return m_1;
    }
    public int getMiddle(){
        return m_2;
    }
    public int getLargest(){
        return m_3;
    }
	/**
	 * Test harness
	 * @param args There are no arguments.
	 */
	public static void main(String[] args) {
		int taskId = 1;
		NumberSorter pc = new NumberSorter(taskId, 5, -1, -20);
		pc.doWork();
		System.out.println(pc.getSmallest() + " " + pc.getMiddle() + " " + pc.getLargest());
	}
}
