package g.nextbus;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Lucas on 08/01/2016.
 * Permet de récupérer les coordonnées de l'arrete le plus proche et le des coordonnées à partir d'un Json
 */
public class Calcul {

    /*
    Variables
     */
    private ObjetTransfert objetTransfert;

    /*
    Constructeurs
     */
    public Calcul(){}


    public Calcul(ObjetTransfert objetTransfert){
        this.objetTransfert = objetTransfert;
    }

    /**
     * Fonction arretLePlusProche, permet de trouver et de récupérer les information de l'arret le plus proche
     * @param listArret
     * @param latitude
     * @param longitude
     * @return latLng (un latitude et une longitude)
     */
    public LatLng arretLePlusProche(String listArret, double latitude, double longitude){

        LatLng latLng;
        double latitudeArret = 0;
        double longitudeArret = 0;

        double bestDist = 999999999;
        double bestLat = 0;
        double bestLong = 0;
        String bestNom = "";
        String bestIP = "";
        int bestPort = 0;

        double challangeLat;
        double challangeLong;
        double challangeDist;

        String IPArret;
        int portArret;
        String nomArret;

        try {
            JSONObject reponseJSON = new JSONObject(listArret);

            //Recherche dans tous les arrets le plus proche de notre position
            for (Iterator iterator = reponseJSON.keys(); iterator.hasNext();) {
                String numNoeud = (String) iterator.next();
                JSONObject arret = (JSONObject) reponseJSON.get(numNoeud);

                //Récupère les informations d'un arret pour les tester
                IPArret = (String) arret.get("IP");
                portArret = arret.getInt("Port");
                nomArret = arret.getString("Nom");
                latitudeArret = arret.getDouble("Latitude");
                longitudeArret = arret.getDouble("Longitude");

                //Calcul de la distance
                challangeLat = pow(latitude - latitudeArret,2);
                challangeLong = pow(longitude - longitudeArret, 2);
                challangeDist = sqrt(challangeLat + challangeLong);

                //Stock le meilleur arret
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

        //Place dans l'objet transfert les information à garder dans le futur
        objetTransfert.setAdresseIP(bestIP);
        objetTransfert.setPort(bestPort);
        objetTransfert.setNomArret(bestNom);

        return latLng;
    }


    public LatLng getCoord (String chaine) {

        LatLng latLng = null;

        try {
            JSONObject reponseJSON = new JSONObject(chaine);
            latLng = new LatLng(reponseJSON.getDouble("Latitude"), reponseJSON.getDouble("Longitude"));

        } catch(JSONException e){
            e.printStackTrace();
        }

        return latLng;
    }
}