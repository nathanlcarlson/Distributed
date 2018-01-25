
package HW1;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;




/**
 */
public class HLServer {

    /**
     * Runs the server.
     */
    public static void main(String[] args) throws IOException {
    
        int port = Integer.parseInt(args[0]);
        int max = Integer.parseInt(args[1]);
        
        ServerSocket listener = new ServerSocket(port);
        HighLowProtocol hlp = new HighLowProtocol(max);
        
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
                        write_end.println(String.format("Welcome. Guess a number [0, %d), 'q' to quit", max));
                    }
                    else{
                        System.out.println("About to read");
                        data_from_client = read_end.readLine();
                        System.out.println("Read");
                        System.out.println(data_from_client);
                        if(data_from_client == null) {
                            hlp.end_game();
                        }
                        else {
                            write_end.println(hlp.processInput(data_from_client));
                        }
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
