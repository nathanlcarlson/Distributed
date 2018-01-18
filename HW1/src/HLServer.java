
package HW1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
// import java.util.Date;
import java.io.BufferedReader;
import java.io.InputStreamReader;




/**
 * A TCP server that runs on port 9090.  When a client connects, it
 * sends the client the current date and time, then closes the
 * connection with that client.  Arguably just about the simplest
 * server you can write.
 */
public class HLServer {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
    
        int port = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);
        
        ServerSocket listener = new ServerSocket(port);
        HighLowProtocol hlp = new HighLowProtocol();
        
        String data_from_client = "";
        try {
            while (true) {
            
                Socket socket = listener.accept();
                System.out.println("Socket Accepted");
                
                try {
                
                    PrintWriter write_end =
                        new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader read_end = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                   
                    if (hlp.is_waiting_to_start()){
                        System.out.println("About to start");
                        hlp.start_game();
                        write_end.println("Welcome. Guess a number [0, 10)");
                    }
                    else{
                        System.out.println("About to read");
                        data_from_client = read_end.readLine();
                        System.out.println("Read");
                        System.out.println(data_from_client);

                        write_end.println(hlp.processInput(data_from_client)); 
                    }
                    
                } finally {
                    socket.close();
                }
            }
        }
        finally {
            listener.close();
        }
    }
}
