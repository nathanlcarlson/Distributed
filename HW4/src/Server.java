// Nathan Carlson
package HW4;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;


public class Server implements ClientManager {

    private List<String> jobs;
    private Connection db;
    public Server() throws RemoteException {
        jobs = new ArrayList<String>();
        jobs.add("PrimeChecker");
        jobs.add("FractionReducer");
        jobs.add("NumberSorter");
        String url = "jdbc:mysql://localhost:3306/hw4";
		String userId = "hw4admin";
		String passwd = "hw4pass";
        try{
            db = DriverManager.getConnection(url, userId, passwd);
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
    }

    public List<String> register(String userid) throws RemoteException{

        if(userExists(userid)){
            throw new RemoteException("User Id already exists");
        }
        else{
            try{
                String sql = "INSERT INTO `users` (userId, score) VALUES ('"+userid+"', 0);";
                Statement stmt = db.createStatement();
                int rs = stmt.executeUpdate(sql);
                stmt.close();
            }
            catch(SQLException e){
                e.printStackTrace();
                throw new RemoteException("500 Error Something went wrong");
            }
            printScores();
            return jobs;
        }
    }
    public List<String> connect(String userid) throws RemoteException{
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }
        else{
            printScores();
            return jobs;
        }
    }
    private void printScores() throws RemoteException{
        // Print all scores
        try{
            String sql = "SELECT * FROM `users`;";
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Scores: ");
            while(rs.next()){
                System.out.println(rs.getString("userId")+" "+rs.getInt("score"));
            }
            System.out.println();
            rs.close();
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
    }
    public Worker requestWork(String userid, String taskName) throws RemoteException{
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }

        Worker worker;
        int cur_task;
        // Store params
        if(taskName.equals("PrimeChecker")){
            int rndInt = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            cur_task = insertTask("PrimeChecker", rndInt, null, null);
            worker = new PrimeChecker(cur_task, rndInt);
        }
        else if(taskName.equals("FractionReducer")){
            int rndInt1 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt2 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            cur_task = insertTask("FractionReducer", rndInt1, rndInt2, null);
            worker = new FractionReducer(cur_task, rndInt1, rndInt2);
        }
        else if(taskName.equals("NumberSorter")){
            int rndInt1 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt2 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            int rndInt3 = ThreadLocalRandom.current().nextInt(0, 1 << 10);
            cur_task = insertTask("NumberSorter", rndInt1, rndInt2, rndInt3);
            worker = new NumberSorter(cur_task, rndInt1, rndInt2, rndInt3);
        }
        else{
            throw new RemoteException("Task does not exist");
        }
        try{
            String sql = "INSERT into `usertasks` (userId, taskId) VALUES ('"+userid+"', "+cur_task+"); ";
            Statement stmt = db.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
        return worker;
    }
    public void submitResults(String userid, Worker answer) throws RemoteException{
        // Check valid userid
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }
        // Get task Id
        int taskId = answer.getTaskId();

        // Update row in database
        String taskName;
        try{
            String sql = "SELECT * FROM `tasks` WHERE taskId='"+taskId+"';";
            Statement stmt = db.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
            ResultSet rs = stmt.executeQuery(sql);
            if(!rs.next()){
                throw new RemoteException("Task Id does not exist");
            }
            // Get type of task
            taskName  = rs.getString("taskName");
            // Store answers
            if(taskName.equals("PrimeChecker")){
                PrimeChecker t_answer = (PrimeChecker)answer;
                rs.updateInt("out1", t_answer.isPrime() ? 1 : 0);
            }
            else if(taskName.equals("FractionReducer")){
                FractionReducer t_answer = (FractionReducer)answer;
                rs.updateInt("out1", t_answer.getNumerator());
                rs.updateInt("out2", t_answer.getDenominator());
            }
            else if(taskName.equals("NumberSorter")){
                NumberSorter t_answer = (NumberSorter)answer;
                rs.updateInt("out1", t_answer.getSmallest());
                rs.updateInt("out2", t_answer.getMiddle());
                rs.updateInt("out3", t_answer.getLargest());
            }
            else{
                throw new RemoteException("Task does not exist");
            }
            rs.updateRow();
            rs.close();
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
        // Print all occurences of that task
        try{
            String sql = "SELECT * FROM `tasks` WHERE taskName='"+taskName+"';";
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("Task: "+taskName);
            while(rs.next()){
                System.out.println("Params: " + rs.getInt("in1") +" "+ rs.getInt("in2") +" "+ rs.getInt("in3"));
                System.out.println("Answer: " + rs.getInt("out1") +" "+ rs.getInt("out2") +" "+ rs.getInt("out3"));
            }
            System.out.println();
            rs.close();
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
        // Update score
        try{
            String sql = "UPDATE `users` SET score = score + 1 WHERE userId='"+userid+"';";
            Statement stmt = db.createStatement();
            int rs = stmt.executeUpdate(sql);
            stmt.close();
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
    }
    public float getScore(String userid) throws RemoteException{
        if(!userExists(userid)){
            throw new RemoteException("User Id does not exist");
        }
        try{
            String sql = "SELECT score FROM `users` WHERE userId='"+userid+"';";
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            int score = rs.getInt("score");
            rs.close();
            stmt.close();
            return score;
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
    }

    private int insertTask(String name, Integer a, Integer b, Integer c) throws RemoteException{
        String fields = " (taskName, in1";
        String values = " VALUES ('"+name+"', "+a;
        if(b != null){
            fields += ", in2";
            values += ", "+b;
        }
        if(c != null){
            fields += ", in3";
            values += ", "+c;
        }
        fields += ") ";
        values += "); ";
        String sql = "INSERT INTO `tasks` "+fields+values;
        try{
            Statement stmt = db.createStatement();
            stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = stmt.getGeneratedKeys();
            rs.next();
            int auto_id = rs.getInt(1);
            rs.close();
            stmt.close();
            return auto_id;
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
    }
    private boolean userExists(String userId) throws RemoteException{
        String sql = "SELECT * FROM `users` WHERE userId='"+userId+"';";
        try{
            Statement stmt = db.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            boolean exists = rs.next();
            rs.close();
            stmt.close();
            return exists;
        }
        catch(SQLException e){
            e.printStackTrace();
            throw new RemoteException("500 Error Something went wrong");
        }
    }
    private int executeUpdate(String sql){
        try{
            Statement stmt = db.createStatement();
            int rs = stmt.executeUpdate(sql);
            stmt.close();
            return rs;
        }
        catch(SQLException e){
            e.printStackTrace();
            System.exit(1);
            return 0;
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
            System.out.println("or, to reset/initialize the database, \nUsage: [port] reset");
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
