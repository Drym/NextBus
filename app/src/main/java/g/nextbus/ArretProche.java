package g.nextbus;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lucas on 08/01/2016.
 * Permet de récupérer les coordonnées de l'arrete le plus proche
 */
public class ArretProche {

    public static LatLng arretLePlusProche(String listArret, double latitude, double longitude){

        LatLng latLng;
        String test = "{\"Latitude\":\"4.3434\"}";

        /*
        try {
            JSONObject myJson = new JSONObject(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
        latLng = new LatLng(43.6, 7.07);

        return latLng;
    }
}
/*
“{\”1\”:{... },\”2\”:{... },\”3\”:{\“Arrêt\”: \“3\”,\”Nom\”:\”Templier\”,\”IP\”:\” 123.345.434.12\”,\”Port\”:\” 1234\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”}}”
 */