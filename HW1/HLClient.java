
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.PrintWriter;
/**
 * Trivial client for the date server.
 */
public class HLClient {

    /**
     * Runs the client as an application.  First it displays a dialog
     * box asking for the IP address or hostname of a host running
     * the date server, then connects to it and displays the date that
     * it serves.
     */
    public static void main(String[] args) throws IOException {
        String serverAddress = "localhost";
        int port = 9090;

        
        // DataOutputStream output = new DataOutputStream(s.getOutputStream());
        String answer = "";
        
        
        
        while(!answer.equals("All done.")){
            Socket s = new Socket(serverAddress, port);
            PrintWriter write_end = new PrintWriter(s.getOutputStream(), true);
            BufferedReader read_end = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));            
            System.out.println("Type data to send to server");
            String guess = br.readLine();
            System.out.println(String.format("Sending: %s", guess));
            write_end.println(guess);
            answer = read_end.readLine();
            System.out.println(answer);
        }
        

        System.exit(0);
    }
}