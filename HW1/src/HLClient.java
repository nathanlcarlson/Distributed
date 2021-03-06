
package HW1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.DataOutputStream;
import java.io.PrintWriter;

/**
 */
public class HLClient {

    /**
     */
    public static void main(String[] args) throws IOException {
        String serverAddress = args[0];
        int port = Integer.parseInt(args[1]);

        
        // DataOutputStream output = new DataOutputStream(s.getOutputStream());
        String answer = "";
        
        // Receive prompt upon connection
        Socket s = new Socket(serverAddress, port);
        BufferedReader read_end = new BufferedReader(new InputStreamReader(s.getInputStream()));
        System.out.println(read_end.readLine());
        
        
        while(!answer.equals("All done.")){
            // Create socket
            s = new Socket(serverAddress, port);
            // Create write end of socket
            PrintWriter write_end = new PrintWriter(s.getOutputStream(), true);
            // Create read end of socket
            read_end = new BufferedReader(new InputStreamReader(s.getInputStream()));
            // Take input from stdin
            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));            
            //System.out.println("Type data to send to server");
            String guess = keyboard.readLine();
            //System.out.println(String.format("Sending: %s", guess));
            // Send data
            write_end.println(guess);
            // Receive data
            answer = read_end.readLine();
            // Print answer to stdout
            System.out.println(answer);
            s.close();
        }
        
        
        System.exit(0);
    }
}
