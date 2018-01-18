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
        int port = 9090;
        ServerSocket listener = new ServerSocket(port);
        HighLowProtocol hlp = new HighLowProtocol();
        try {
            while (true) {
                Socket socket = listener.accept();
                try {

                    PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                    // System.out.println();
                    out.println(hlp.processInput(in.readLine()));
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