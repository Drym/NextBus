package g.nextbus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONObject;

import java.util.Iterator;

import fr.rolandl.blog_gps.R;


/**
 * Created by Lucas on 14/01/2016.
 */
public class ConnectionPermanente implements Runnable {

    /*
    Variables
     */
    private ObjetTransfert objetTransfert;
    private TCPClient mTcpClient;
    Bundle bundle = new Bundle();

    /*
    Constructeur
     */
    public ConnectionPermanente(ObjetTransfert objetTransfert) {
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

            //Boucle pour refresh toutes les secondes
            while(objetTransfert.isReset()) {
                try {

                    //Demande la liste des bus
                    mTcpClient.sendMessage(objetTransfert.getRequete());
                    JSONObject ListBus = new JSONObject(mTcpClient.Reponse());

                    Calcul recup = new Calcul();

                    LatLng coord;
                    int i = 0;

                    JSONObject test = new JSONObject();
                    String numBusAPrendre = objetTransfert.getMessage();

                    //calcul de la position de chaque bus
                    for (Iterator iterator = ListBus.keys(); iterator.hasNext(); ) {
                        String numNoeud = (String) iterator.next();

                        test = (JSONObject) ListBus.get(numNoeud);
                        test = (JSONObject) test.get("BUS");

                        //Récupèration des coordonées
                        coord = recup.getCoord(test.toString());

                        String numBus = test.getString("Bus");
                        String placeRestante = test.getString("PlacesRestantes");
                        Log.d("ConnectionPermanente", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + placeRestante);

                        bundle.putString("PLACE", placeRestante);

                        //Affiche le marker sur la carte
                        addMarker(i, coord, objetTransfert.getListMarker().get(i), numBus, numBusAPrendre);

                        i++;
                    }

                    //On attend une seconde avant de refresh
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Log.e("ConnectionPermanente", "Connection trop lente");
                }
            }

            mTcpClient.sendMessage("{\"Requete\":\"DECONNECTION\"}");
            mTcpClient.stopClient();
        }
        catch (Exception e) {
            Log.e("ConnectionPermanente", "Impossible de se connecter au serveur");
        }
    }

    /**
     * Met a jour les coordonées des markers des bus
     * @param i
     * @param coor
     * @param marker
     * @param numBus
     * @param BusAPrendre
     * @author Lucas
     */
    public void addMarker(final int i, final LatLng coor, final Marker marker, final String numBus, final String BusAPrendre) {

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            public void run() {

                //On met le marker du bus a prendre dans une couleur diffèrente
                if (BusAPrendre.equals(numBus)){
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.buspersoico));
                }
                marker.setTitle("Bus "+numBus);
                marker.setPosition(coor);

            }
        });

    }


}
