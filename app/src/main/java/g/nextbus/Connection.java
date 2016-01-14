package g.nextbus;

import android.util.Log;

/**
 * Created by Lucas on 08/01/2016.
 * Gère la connection avec un serveur TCP pour récupérer des informations
 */
public class Connection implements Runnable {

    /*
    Variables
     */
    private ObjetTransfert objetTransfert;
    private TCPClient mTcpClient;

    /*
    Constructeur
     */
    public Connection (ObjetTransfert objetTransfert) {
        this.objetTransfert = objetTransfert;

    }

    /**
     * Lance la connection TCP
     */
    public void run() {

        try {
            //On se connecte au serveur
            mTcpClient = new TCPClient(objetTransfert.getAdresseIP(), objetTransfert.getPort());
            mTcpClient.run();
            //Demande la liste des arrets
            mTcpClient.sendMessage(objetTransfert.getRequete());
            objetTransfert.setMessage(mTcpClient.Reponse());

            mTcpClient.stopClient();
        }
        catch (Exception e) {
            Log.e("Connection","Impossible de se connecter au serveur");
        }
    }
}