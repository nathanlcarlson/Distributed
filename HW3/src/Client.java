package HW3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.List;

public class Client {
    public static void main(String args[]){
        String host = "127.0.0.1";
        String name = "ncarlson";
        try {
            Registry registry = LocateRegistry.getRegistry(host);
            ClientManager server = (ClientManager)registry.lookup("ClientManager");
            List<String> jobs = server.register(name);
            Worker worker = server.requestWork(name, jobs.get(0));
            worker.doWork();
            System.out.println(server.getScore(name));
            server.submitResults(name, worker);
            System.out.println(server.getScore(name));
        }
        catch (Exception re) {
            re.printStackTrace();
        }
    }
}
