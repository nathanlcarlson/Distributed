// Nathan Carlson
/**
 * Worker.java
 */
package HW4;

import java.io.Serializable;

/**
 * @author david apostal
 *
 */
public abstract class Worker implements Serializable {
	private int taskId;

	/**
	 * @param the task id for the work task
	 */
	public Worker(int id) {
		taskId = id;
	}

	public int getTaskId() {
		return taskId;
	}

	public abstract void doWork();
}
