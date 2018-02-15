/**
 * ClientManager.java
 */
package HW3;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * @author david apostal
 *
 */
public interface ClientManager extends Remote {
	/**
	 * Registers a client with the volunteer computing server.
	 * 
	 * @param userid the client name
	 * @return a List of available work names. The names are unique.
	 * @throws RemoteException if userid is not unique
	 */
	public List<String> register(String userid) throws RemoteException;
	
	/**
	 * Requests work from the server.
	 * 
	 * @param userId the client requesting work
	 * @param taskName the project name
	 * @return a Worker object that can perform
	 * @throws RemoteException if something goes wrong during Worker.doWork()
	 */
	public Worker requestWork(String userId, String taskName) throws RemoteException;

	/**
	 * Submits work results from a volunteer to the server.
	 * 
	 * @param userId the client submitting results
	 * @param answer the completed Worker
	 * @throws RemoteException
	 */
	public void submitResults(String userId, Worker answer) throws RemoteException;
	
	/**
	 * Gets the score for a specified client.
	 * @param userid the client name
	 * @return the client's current score
	 * @throws RemoteException if userid is unknown
	 */
	public float getScore(String userid) throws RemoteException;
}
