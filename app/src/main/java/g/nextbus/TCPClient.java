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
 * Permet d'établir une connection TCP avec une adresse et un port
 */

public class TCPClient {

    /*
    Variable
     */
    private String IP;
    private int port;

    private PrintWriter out;
    private BufferedReader in;
    private Socket socket;

    /*
    Constructeur
     */
    public TCPClient (String IP, int port) {
        this.IP = IP;
        this.port = port;
    }

    /**
     * Envois d'un message
     * @param message
     */
    public void sendMessage(String message){
        Log.d("TCPClient", message);
        out.println(message);
        out.flush();
    }

    /**
     * Récupération de la réponse
     * @return ce qui à été lu
     */
    public String Reponse()  {

        try {
            return in.readLine();
        }
        catch (Exception e) {
            return "Erreur";
        }

    }

    /**
     * Déconnection du client
     */
    public void stopClient() {

        try {
            out.close();
            in.close();
            socket.close();
        }
        catch (Exception e) {
            Log.e("TCPClient", "Erreur de déconnection");
        }
    }

    /**
     * Lancement de la connection
     */
    public void run() {

        try {
            //Adresse IP
            InetAddress serverAddr = InetAddress.getByName(IP);

            //Création de la socket
            socket = new Socket(serverAddr, port);

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