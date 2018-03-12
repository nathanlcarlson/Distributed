// Nathan Carlson
package HW4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.RemoteException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Client {
    public static void main(String args[]){
        String host = "127.0.0.1";
        if(args.length != 2){
            System.out.println("Usage: [port] [userId]");
            return;
        }
        String name = args[1];
        int port;
        try{
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            System.out.println("Usage: [port] [userId]");
            System.out.println("Port must be an integer");
            return;
        }
        try {
            Registry registry = LocateRegistry.getRegistry(host, port);
            ClientManager server = (ClientManager)registry.lookup("ClientManager");
            List<String> jobs;
            try{
                jobs = server.register(name);
            }
            catch(RemoteException e){
                jobs = server.connect(name);
            }
            int j = ThreadLocalRandom.current().nextInt(0, jobs.size());
            Worker worker = server.requestWork(name, jobs.get(j));
            worker.doWork();
            server.submitResults(name, worker);
            System.out.println(name+" Score: "+server.getScore(name));
        }
        catch (Exception re) {
            re.printStackTrace();
        }
    }
}
