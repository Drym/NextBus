package g.nextbus;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.ArrayList;
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
                //JSONObject ListBus;

                //JSONObject ListBusdur = new JSONObject("{\"1\":{\"BUS\":{\"Bus\":\"1\",\"Vitesse\":25,\"IP\":\"10.212.115.127\",\"Port\":1234,\"Latitude\":43.617619,\"Noeud\":\"25\",\"Longitude\":7.070758}},\"2\":{\"BUS\":{\"Bus\":\"2\",\"Vitesse\":50,\"IP\":\"10.212.115.127\",\"Port\":1235,\"Latitude\":43.616486,\"Noeud\":\"44\",\"Longitude\":7.068313}},\"3\":{\"BUS\":{\"Bus\":\"3\",\"Vitesse\":75,\"IP\":\"10.212.115.127\",\"Port\":1236,\"Latitude\":43.616257,\"Noeud\":\"58\",\"Longitude\":7.066246}}}");

                //ListBus = ListBusdur;

                RecupererCoord recup = new RecupererCoord();
                LatLng coord;

                ArrayList<LatLng> listBus = new ArrayList<>();

                int i = 0;
                JSONObject test = new JSONObject();

                for (Iterator iterator = ListBus.keys(); iterator.hasNext();) {
                    String numNoeud = (String) iterator.next();
                    test = (JSONObject) ListBus.get(numNoeud);
                    test = (JSONObject) test.get("BUS");

                    coord = recup.getCoord(test.toString());
                    listBus.add(i, coord);

                    i++;
                }

                objetTransfert.setListBus(listBus);

            } catch (Exception e) {
                e.printStackTrace();
            }

            //mTcpClient.keepAlive();
            mTcpClient.stopClient();
        }
        catch (Exception e) {
            Log.e("ConnectionPerma", "Impossible de se connecter au serveur");
        }
    }

}
