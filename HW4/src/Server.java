// Nathan Carlson
package HW4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


public class Server implements ClientManager {

    private List<String> names;
    private List<String> jobs;
    private Connection conn;
    private Map<String, Float> scores;
    private Map<Integer, String> tasks;
    private Map<Integer, List<Integer>> params;
    private Map<Integer, List<Integer>> answers;
    private Connection db;
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
        String url = "jdbc:mysql://localhost:3306/hw4";
		String userId = "hw4admin";
		String passwd = "hw4pass";
        try{
            db = DriverManager.getConnection(url, userId, passwd);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    public List<String> register(String userid) throws RemoteException{

        if(userExists(userid)){
            throw new RemoteException("User Id already exists");
        }
        else{
            String sql = "insert into `users` (userId, score) values ('"+userid+"', 0);";
            executeUpdate(sql);
            // System.out.println("Scores: ");
            // for (String name: scores.keySet()){
            //     String key = name.toString();
            //     String value = scores.get(name).toString();
            //     System.out.println(key + " " + value);
            // }
            // System.out.println();
            return jobs;
        }
    }
    public List<String> connect(String userid) throws RemoteException{
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }
        else{
            return jobs;
        }
    }
    public Worker requestWork(String userid, String taskName) throws RemoteException{
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }

        Worker worker;
        int cur_task;
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
        if(!userExists(userid)){
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
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }

        return scores.get(userid);
    }
    private int executeUpdate(String sql){
        try{
            Statement stmt = db.createStatement();
            int rs = stmt.executeUpdate(sql);
            return rs;
        }
        catch(SQLException e){
            e.printStackTrace();
            return 0;
        }
    }
    private int insertTask(String name, Integer a, Integer b, Integer c){
        String sql = "INSERT INTO `tasks` (taskName, in1, in2, in3) VALUES ("+name+","+a+","+b+","+c+");"
    }
    private boolean userExists(String userId){
        String sql = "SELECT * FROM `users` WHERE userId='"+userId+"';";
        System.out.println(sql);
        try{
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            return rs.next();
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public void createTables(){
        String sql = "";
        sql += "CREATE TABLE IF NOT EXISTS `users` ( ";
        sql += "userId varchar(50) NOT NULL primary key, ";
        sql += "score int, ";
        sql += "UNIQUE(userId) ";
        sql += "); ";
        executeUpdate(sql);
        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS `tasks` ( ";
        sql += "taskId int NOT NULL AUTO_INCREMENT PRIMARY KEY, ";
        sql += "taskName varchar(50) NOT NULL, ";
        sql += "in1 int, ";
        sql += "in2 int, ";
        sql += "in3 int, ";
        sql += "out1 int, ";
        sql += "out2 int, ";
        sql += "out3 int, ";
        sql += "UNIQUE(taskId) ";
        sql += "); ";
        executeUpdate(sql);
        sql = "";
        sql += "CREATE TABLE IF NOT EXISTS `usertasks` ( ";
        sql += "userId varchar(50) NOT NULL, ";
        sql += "taskId int NOT NULL, ";
        sql += "UNIQUE(taskId), ";
        sql += "FOREIGN KEY (taskId) REFERENCES tasks(taskId), ";
        sql += "FOREIGN KEY (userId) REFERENCES users(userId) ";
        sql += "); ";
        executeUpdate(sql);

    }
    public void dropTables(){
        String sql = "";
        sql += "DROP TABLE IF EXISTS ";
        sql += "usertasks, users, tasks;";
        executeUpdate(sql);
    }
    public static void main(String args[]) {

        if(!(args.length == 1 || args.length == 2)){
            System.out.println("Usage: [port]");
            System.out.println("or, to reset the database, \nUsage: [port] reset");
            return;
        }
        if(args.length == 2 && !args[1].equals("reset")){
            System.out.println("Please specify 'reset' as the second argument if you would like to reset the database");
            System.out.println("Usage: [port] reset");
            return;
        }
        int port;
        try{
            port = Integer.parseInt(args[0]);
        } catch (NumberFormatException e){
            System.out.println("Usage: [port]");
            System.out.println("Port must be an integer");
            return;
        }
        try {
            Server obj = new Server();
            if(args.length == 2){
                obj.dropTables();
                obj.createTables();
            }
            ClientManager stub = (ClientManager)UnicastRemoteObject.exportObject(obj, port);
            Registry registry = LocateRegistry.createRegistry(port);

            registry.rebind("ClientManager", stub);

            //System.out.println("ClientManager Bound");
        } catch (Exception re) {
            re.printStackTrace();
        }
    }
}
