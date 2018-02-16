package HW3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Client {
    public static void main(String args[]){
        String host = "127.0.0.1";
        String name = "ncarlson";
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClientManager server = (ClientManager)registry.lookup("ClientManager");
            List<String> jobs = server.register(name);
            
            for(int i=0; i<15; ++i){
                int j = ThreadLocalRandom.current().nextInt(0, jobs.size());
                Worker worker = server.requestWork(name, jobs.get(j));
                worker.doWork();
                server.submitResults(name, worker);
                System.out.println("My Score: "+server.getScore(name));
            }
            
        }
        catch (Exception re) {
            re.printStackTrace();
        }
    }
}
