package g.nextbus;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucas on 08/01/2016.
 * Permet de récupérer les coordonnées de l'arrete le plus proche
 */
public class ArretProche {

    public String arretLePlusProche(String listArret, double latitude, double longitude){

        String test = "{\"Latitude\":\"4.3434\"}";

        try {
            JSONObject myJson = new JSONObject(test);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }
}
/*
“{\”1\”:{... },\”2\”:{... },\”3\”:{\“Arrêt\”: \“3\”,\”Nom\”:\”Templier\”,\”IP\”:\” 123.345.434.12\”,\”Port\”:\” 1234\”,\”Latitude\”:\”4.3434\”,\”Longitude\”:\” 5.6789\”}}”
 */