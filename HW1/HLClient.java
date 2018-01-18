
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

        Socket s = new Socket(serverAddress, port);
        DataOutputStream output = new DataOutputStream(s.getOutputStream());

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String guess = br.readLine();
        PrintWriter out = new PrintWriter(s.getOutputStream(), true);
        out.println(guess);
        BufferedReader input =
            new BufferedReader(new InputStreamReader(s.getInputStream()));
        String answer = input.readLine();
        System.out.println(answer);

        System.exit(0);
    }
}