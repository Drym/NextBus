package g.nextbus;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Lucas on 08/01/2016.
 * Permet de récupérer les coordonnées de l'arrete le plus proche
 */
public class ArretProche {

    private ObjetTransfert objetTransfert;

    public ArretProche(ObjetTransfert objetTransfert){
        this.objetTransfert = objetTransfert;
    }

    public LatLng arretLePlusProche(String listArret, double latitude, double longitude){

        LatLng latLng;
        double latitudeArret = 0;
        double longitudeArret = 0;
        double bestDist = 999999999;
        double bestLat = 0;
        double bestLong = 0;
        double challangeLat;
        double challangeLong;
        double challangeDist;
        String bestNom = "";
        String bestIP = "";
        int bestPort = 0;
        String IPArret;
        int portArret;
        String nomArret;

        try {
            JSONObject reponseJSON = new JSONObject(listArret);

            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numNoeud = (String) iterator.next();
                JSONObject arret = (JSONObject) reponseJSON.get(numNoeud);

                IPArret = (String) arret.get("IP");
                portArret = arret.getInt("Port");
                nomArret = arret.getString("Nom");
                latitudeArret = arret.getDouble("Latitude");
                longitudeArret = arret.getDouble("Longitude");

                challangeLat = pow(latitude - latitudeArret,2);
                challangeLong = pow(longitude - longitudeArret, 2);
                challangeDist = sqrt(challangeLat + challangeLong);

                if (challangeDist < bestDist) {
                    bestDist = challangeDist;
                    bestLat = latitudeArret;
                    bestLong = longitudeArret;
                    bestIP = IPArret;
                    bestPort = portArret;
                    bestNom = nomArret;
                }
            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        latLng = new LatLng(bestLat, bestLong);

        objetTransfert.setAdresseIP(bestIP);
        objetTransfert.setPort(bestPort);
        objetTransfert.setNomArret(bestNom);

        return latLng;
    }
}