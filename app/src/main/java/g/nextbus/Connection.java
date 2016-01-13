package g.nextbus;

import android.util.Log;

/**
 * Created by Lucas on 08/01/2016.
 * Gère la connection avec un serveur TCP pour récupérer des informations
 */
public class Connection implements Runnable {
    private ObjetTransfert objetTransfert;
    private TCPClient mTcpClient;
    private String defaut = "{\"1\":{\"Arrêt\": \"1\",\"Nom\":\"Templier\",\"IP\":\"10.212.115.127\",\"Port\":\"1240\",\"Latitude\":\"10\",\"Longitude\":\"10\"}}";


    public Connection (ObjetTransfert objetTransfert) {
        this.objetTransfert = objetTransfert;

    }

    public void run() {

        objetTransfert.setMessage(defaut);

        try {
            //On se connecte au serveur
            mTcpClient = new TCPClient(objetTransfert.getAdresseIP(), objetTransfert.getPort());
            mTcpClient.run();
            //Demande la liste des arrets
            mTcpClient.sendMessage(objetTransfert.getRequete());
            objetTransfert.setMessage(mTcpClient.Reponse());
            //objetTransfert.setMessage(test);
            mTcpClient.stopClient();
        }
        catch (Exception e) {
            Log.e("Connection","Impossible de se connecter au serveur");
        }
    }
}