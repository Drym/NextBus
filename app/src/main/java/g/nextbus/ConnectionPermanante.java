package g.nextbus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

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

            //On se connecte au serveur
            mTcpClient = new TCPClient(objetTransfert.getAdresseIP(), objetTransfert.getPort());
            mTcpClient.run();

            while(objetTransfert.isReset()) {
                try {

                    //Demande la liste des arrets
                    mTcpClient.sendMessage(objetTransfert.getRequete());
                    JSONObject ListBus = new JSONObject(mTcpClient.Reponse());

                    Calcul recup = new Calcul();

                    LatLng coord;
                    int i = 0;

                    JSONObject test = new JSONObject();
                    String numBusAPrendre = objetTransfert.getMessage();

                    for (Iterator iterator = ListBus.keys(); iterator.hasNext(); ) {
                        String numNoeud = (String) iterator.next();
                        test = (JSONObject) ListBus.get(numNoeud);
                        test = (JSONObject) test.get("BUS");

                        coord = recup.getCoord(test.toString());

                        String numBus = test.getString("Bus");
                        addMarker(i, coord, objetTransfert.getListMarker().get(i), numBus);

                        i++;
                    }

                    Thread.sleep(1000);
                } catch (Exception e) {
                    //Log.e("ConnectionPermanate", "Connection trop lente");
                }
            }

            //mTcpClient.stopClient();
        }
        catch (Exception e) {
            Log.e("ConnectionPermanate", "Impossible de se connecter au serveur");
        }
    }

    public void addMarker(final int i, final LatLng coor, final Marker marker, final String numBus) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {
                marker.setTitle("Bus "+numBus);
                marker.setPosition(coor);
            }
        });

    }

}
