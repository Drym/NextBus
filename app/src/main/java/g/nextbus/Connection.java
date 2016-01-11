package g.nextbus;

/**
 * Created by Lucas on 08/01/2016.
 * Gère la connection avec un serveur TCP pour récupérer des informations
 */
public class Connection implements Runnable {

    private TCPClient mTcpClient;
    private String reponse;
    private ArretProche arretProche;
    private String adresseIP;
    private int port;

    public Connection (String reponse, String addresseIP, int port) {

        this.reponse = reponse;
        this.adresseIP = addresseIP;
        this.port = port;
    }

    public void run() {
        //On se connecte au serveur
        mTcpClient = new TCPClient(adresseIP, port);
        //Demande la liste des arrets
        mTcpClient.sendMessage("{\\”Requete\\”:\\”LISTARRETS\\”}");
        reponse = mTcpClient.Reponse();
    }
}