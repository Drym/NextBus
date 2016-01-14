package g.nextbus;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Iterator;

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

            try {
                //Affiche un message du numéro de bus à prendre
                JSONObject ListBus = new JSONObject(objetTransfert.getMessage());
                ListBus = (JSONObject) ListBus.get("BUS");

                RecupererCoord recup = new RecupererCoord();
                LatLng coord;

                for (Iterator<String> it = ListBus.keys(); it.hasNext(); ) {
                    coord = recup.getCoord(ListBus.get(it.next()).toString());

                    objetTransfert.getgMap().addMarker(new MarkerOptions().title("Bus").position(coord));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            //mTcpClient.keepAlive();
            mTcpClient.stopClient();
        }
        catch (Exception e) {
            Log.e("Connection", "Impossible de se connecter au serveur");
        }
    }

}
