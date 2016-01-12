package g.nextbus;

/**
 * Created by Lucas on 11/01/2016.
 */
public class ObjetTransfert {
    String message;
    private String adresseIP;
    private int port;

    public ObjetTransfert(String message, String adresseIP, int port){
        this.message = message;
        this.adresseIP = adresseIP;
        this.port = port;
    }
    public void setMessage(String b){
        message=b;
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

    public void setAdresseIP(String adresseIP) {
        this.adresseIP = adresseIP;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
