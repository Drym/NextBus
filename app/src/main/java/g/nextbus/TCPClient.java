package g.nextbus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by Lucas on 08/01/2016.
 */

public class TCPClient {

    private String serverMessage;
    public static final String SERVERIP = "192.168.0.102";
    public static final int SERVERPORT = 4444;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;
    Socket socket;


    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    public String Reponse()  {

        try {
            return in.readLine();
        }
        catch (Exception e) {
            return "Erreur";
        }

    }

    public void stopClient() {

        try {
            out.close();
            in.close();
            socket.close();
        }
        catch (Exception e) {}
    }

    public void run() {


        try {
            //here you must put your computer's IP address.
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            //create a socket to make the connection with the server
            socket = new Socket(serverAddr, SERVERPORT);

            //send the message to the server
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            //receive the message which the server sends back
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }
        catch (Exception e) {}
    }
}