package g.nextbus;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lucas on 11/01/2016.
 * Permet de récupérer des informations dans des Threads
 */
public class ObjetTransfert {
    /*
    Variables
     */
    private String message;
    private String requete;
    private LatLng latLng;
    private String nomArret;
    private String adresseIP;
    private int port;
    private GoogleMap gMap;

    /*
    Constructeurs
     */
    public ObjetTransfert(){}

    public ObjetTransfert(String adresseIP, int port){
        this.adresseIP = adresseIP;
        this.port = port;
    }

    /*
    Getters et Setters
     */
    public String getMessage(){
        return message;
    }

    public String getAdresseIP() {
        return adresseIP;
    }

    public int getPort() {
        return port;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public void setAdresseIP(String adresseIP) {
        this.adresseIP = adresseIP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getNomArret() {
        return nomArret;
    }

    public void setNomArret(String nomArret) {
        this.nomArret = nomArret;
    }

    public String getRequete() {
        return requete;
    }

    public void setRequete(String requete) {
        this.requete = requete;
    }

    public GoogleMap getgMap() {
        return gMap;
    }

    public void setgMap(GoogleMap gMap) {
        this.gMap = gMap;
    }
}
