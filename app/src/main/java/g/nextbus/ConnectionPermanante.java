package g.nextbus;

import android.util.Log;

/**
 * Created by Lucas on 14/01/2016.
 */
public class ConnectionPermanante  implements Runnable {

    /*
    Variables
     */
    private ObjetTransfert objetTransfert;
    private TCPClient mTcpClient;
    private boolean connecte = false;

    /*
    Constructeur
     */
    public ConnectionPermanante (ObjetTransfert objetTransfert) {
        this.objetTransfert = objetTransfert;

    }

    /**
     * Lance la connection TCP
     */
    public void run() {

        try {
            connecte = true;
            //On se connecte au serveur
            mTcpClient = new TCPClient(objetTransfert.getAdresseIP(), objetTransfert.getPort());
            mTcpClient.run();
            //Demande la liste des arrets
            mTcpClient.sendMessage(objetTransfert.getRequete());

           // while(connecte) {
                objetTransfert.setMessage(mTcpClient.Reponse());
             //   Thread.sleep(1000);
            //}

            mTcpClient.keepAlive();
        }
        catch (Exception e) {
            Log.e("Connection", "Impossible de se connecter au serveur");
        }
    }

}
