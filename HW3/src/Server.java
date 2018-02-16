package HW3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Server implements ClientManager {
    
    private Vector<String> names;
    private Vector<String> jobs;
    Map<String, Float> scores;

    public Server() {
        jobs = new Vector<String>();
        jobs.add("PrimeChecker");
        jobs.add("FractionReducer");
        jobs.add("NumberSorter");
        names = new Vector<String>();
        scores = new HashMap<String, Float>();
    }
    
    public List<String> register(String userid) throws RemoteException{
        if(names.contains(userid)){
            throw new RemoteException("User Id already exists");
        }
        else{
            names.add(userid);
            scores.put(userid, 0.F);
            return jobs;
        }      
    }
    public Worker requestWork(String userid, String taskName) throws RemoteException{
        if(!(names.contains(userid))){
            throw new RemoteException("User Id does not exist");
        }
        
        Worker worker;
        if(taskName.equals("PrimeChecker")){
            int rndInt = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            worker = new PrimeChecker(1, rndInt);
        }
        else if(taskName.equals("FractionReducer")){
            int rndInt1 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt2 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            worker = new FractionReducer(1,rndInt1, rndInt2);
        }
        else if(taskName.equals("NumberSorter")){
            int rndInt1 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt2 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt3 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            worker = new NumberSorter(1, rndInt1, rndInt2, rndInt3);
        }      
        else{
            throw new RemoteException("Task does not exist");
        }
          
        return worker;
    }
    public void submitResults(String userid, Worker answer) throws RemoteException{
        if(!(names.contains(userid))){
            throw new RemoteException("User Id does not exist");
        }
        scores.put(userid, scores.get(userid)+1.F);
    }
    public float getScore(String userid) throws RemoteException{
        if(!(names.contains(userid))){
            throw new RemoteException("User Id does not exist");
        }
        
        return scores.get(userid);
    }
    public static void main(String args[]) {
        try {
            Server obj = new Server();
            ClientManager stub = (ClientManager)UnicastRemoteObject.exportObject(obj, 0);

            Registry registry = LocateRegistry.getRegistry();
            registry.rebind("ClientManager", stub);

            System.out.println("ClientManager Bound");
        } catch (Exception re) {
            re.printStackTrace();
        }
    }
}
