package g.nextbus;

/**
 * Created by user on 08/01/2016.
 */
public class Connection implements Runnable {

    private TCPClient mTcpClient;
    private String reponse;
    private ArretProche arretProche;

    Connection (String reponse) {
        this.reponse = reponse;
    }

    public void run() {
        //On se connecte au serveur
        mTcpClient = new TCPClient();
        //Demande la liste des arrets
        mTcpClient.sendMessage("liste des arrets");
        reponse = mTcpClient.Reponse();
    }
}
//Started with a "new Thread(threadA).start()" call