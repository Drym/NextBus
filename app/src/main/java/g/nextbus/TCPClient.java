package g.nextbus;

import android.util.Log;

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
    private String SERVERIP;
    private int SERVERPORT;
    private boolean mRun = false;

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    public TCPClient (String SERVERIP, int SERVERPORT) {
        this.SERVERIP = SERVERIP;
        this.SERVERPORT = SERVERPORT;
    }

    /*
    Envoyer un message
     */
    public void sendMessage(String message){
        out.println(message);
        out.flush();
    }

    /*
    Récupérer la réponse
     */
    public String Reponse()  {

        try {
            return in.readLine();
        }
        catch (Exception e) {
            return "Erreur";
        }

    }

    /*
    Se déconnecter
     */
    public void stopClient() {

        try {
            out.close();
            in.close();
            socket.close();
        }
        catch (Exception e) {}
    }

    /*
    Se connecter
     */
    public void run() {

        try {
            //Adresse IP
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);

            //Création de la socket
            socket = new Socket(serverAddr, SERVERPORT);

            //Création de outPutStream
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

            //Création de inPutStream
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        }
        catch (Exception e) {
            Log.e("TCPClient", "Impossible de se connecter au serveur");
        }
        }
        }