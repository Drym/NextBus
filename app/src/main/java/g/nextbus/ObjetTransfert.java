package g.nextbus;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Lucas on 11/01/2016.
 */
public class ObjetTransfert {
    String message;
    LatLng latLng;
    private String adresseIP;
    private int port;

    public ObjetTransfert(String message, String adresseIP, int port){
        this.message = message;
        this.adresseIP = adresseIP;
        this.port = port;
    }

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
}
