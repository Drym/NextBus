package g.nextbus;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import static java.lang.Math.*;

/**
 * Created by Lucas on 08/01/2016.
 * Permet de récupérer les coordonnées de l'arrete le plus proche
 */
public class ArretProche {

    public static LatLng arretLePlusProche(String listArret, double latitude, double longitude, String IPArret, int portArret){

        LatLng latLng;
        double latitudeArret = 0;
        double longitudeArret = 0;
        double bestDist = 999999999;
        double bestLat = 0;
        double bestLong = 0;
        double challangeLat;
        double challangeLong;
        double challangeDist;
        String bestIP = "";
        int bestPort = 0;
        String test = "{\"1\":{\"Arrêt\": \"1\",\"Nom\":\"Templier\",\"IP\":\"123.345.434.12\",\"Port\":\" 1234\",\"Latitude\":\"43.6\",\"Longitude\":\"7.07\"},\"2\":{\"Arrêt\": \"1\",\"Nom\":\"Templier\",\"IP\":\"123.345.434.12\",\"Port\":\" 1234\",\"Latitude\":\"43.6\",\"Longitude\":\"7.07\"}}";

        try {
            JSONObject reponseJSON = new JSONObject(test); //Mettre listArret

            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numNoeud = (String) iterator.next();
                JSONObject arret = (JSONObject) reponseJSON.get(numNoeud);

                IPArret = (String) arret.get("IP");
                portArret = arret.getInt("Port");
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
                }

            }
        }
        catch(JSONException e){
            e.printStackTrace();
        }

        latLng = new LatLng(bestLat, bestLong);
        IPArret = bestIP;
        portArret = bestPort;

        return latLng;
    }
}