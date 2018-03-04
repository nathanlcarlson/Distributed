// Nathan Carlson
package HW3;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Server implements ClientManager {

    private List<String> names;
    private List<String> jobs;
    private Map<String, Float> scores;
    private Map<Integer, String> tasks;
    private Map<Integer, List<Integer>> params;
    private Map<Integer, List<Integer>> answers;
    private int cur_task = 0;

    public Server() {
        jobs = new ArrayList<String>();
        jobs.add("PrimeChecker");
        jobs.add("FractionReducer");
        jobs.add("NumberSorter");
        names = new ArrayList<String>();
        scores = new HashMap<String, Float>();
        tasks = new HashMap<Integer, String>();
        params = new HashMap<Integer, List<Integer>>();
        answers = new HashMap<Integer, List<Integer>>();
    }

    public List<String> register(String userid) throws RemoteException{
        if(names.contains(userid)){
            throw new RemoteException("User Id already exists");
        }
        else{
            names.add(userid);
            scores.put(userid, 0.F);
            System.out.println("Scores: ");
            for (String name: scores.keySet()){
                String key = name.toString();
                String value = scores.get(name).toString();
                System.out.println(key + " " + value);
            }
            System.out.println();
            return jobs;
        }
    }
    public Worker requestWork(String userid, String taskName) throws RemoteException{
        if(!(names.contains(userid))){
            throw new RemoteException("User Id does not exist");
        }

        Worker worker;
        // Store params
        ArrayList<Integer> t_params = new ArrayList();
        if(taskName.equals("PrimeChecker")){
            int rndInt = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            t_params.add(rndInt);
            worker = new PrimeChecker(cur_task, rndInt);
        }
        else if(taskName.equals("FractionReducer")){
            int rndInt1 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt2 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            t_params.add(rndInt1);
            t_params.add(rndInt2);
            worker = new FractionReducer(cur_task, rndInt1, rndInt2);
        }
        else if(taskName.equals("NumberSorter")){
            int rndInt1 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt2 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt3 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            t_params.add(rndInt1);
            t_params.add(rndInt2);
            t_params.add(rndInt3);;
            worker = new NumberSorter(cur_task, rndInt1, rndInt2, rndInt3);
        }
        else{
            throw new RemoteException("Task does not exist");
        }
        // Save task
        params.put(cur_task, t_params);
        tasks.put(cur_task, taskName);
        cur_task++;
        return worker;
    }
    public void submitResults(String userid, Worker answer) throws RemoteException{
        // Check valid userid
        if(!(names.contains(userid))){
            throw new RemoteException("User Id does not exist");
        }
        // Get task Id
        int taskId = answer.getTaskId();
        // Check if valid task Id
        if(!(tasks.containsKey(taskId))){
            throw new RemoteException("Task Id does not exist");
        }
        // Get type of task
        String taskName = tasks.get(taskId);
        // Store answers
        ArrayList<Integer> t_answers = new ArrayList();
        if(taskName.equals("PrimeChecker")){
            PrimeChecker t_answer = (PrimeChecker)answer;
            t_answers.add(t_answer.isPrime() ? 1 : 0);
        }
        else if(taskName.equals("FractionReducer")){
            FractionReducer t_answer = (FractionReducer)answer;
            t_answers.add(t_answer.getNumerator());
            t_answers.add(t_answer.getDenominator());;
        }
        else if(taskName.equals("NumberSorter")){
            NumberSorter t_answer = (NumberSorter)answer;
            t_answers.add(t_answer.getSmallest());
            t_answers.add(t_answer.getMiddle());
            t_answers.add(t_answer.getLargest());;
        }
        else{
            throw new RemoteException("Task does not exist");
        }
        answers.put(taskId, t_answers);
        System.out.println("Task: " + taskName);
        for (Integer tId: tasks.keySet()){
            String task = tasks.get(tId).toString();
            if(task.equals(taskName)){
                System.out.println("Params: " + params.get(tId));
                System.out.println("Answer: " + answers.get(tId) + '\n');
            }
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

            //System.out.println("ClientManager Bound");
        } catch (Exception re) {
            re.printStackTrace();
        }
    }
}
