package g.nextbus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.ArrayList;

import fr.rolandl.blog_gps.R;

/**
 * MainActivity.
 *
 * @author Lucas
 */

public class MainActivity extends Activity implements LocationListener {
    /*******************************************************/
    /**
     * VARIABLES.
     * /
     *******************************************************/
    private static String IP_SERVEUR = "172.20.10.4";//"10.212.115.127";
    private static int PORT_SERVEUR = 4444;

    private LocationManager locationManager;
    private GoogleMap gMap;
    private Handler mHandler;

    private Circle circle;
    private Marker marker;
    private Marker markerArret;
    private Marker markerArret2;
    private Marker markerArret3;

    private ObjetTransfert objetTransfert;
    private ObjetTransfert objetTransfert2;
    private ObjetTransfert objetTransfert3;
    private ObjetTransfert objetTransfert5;
    private ObjetTransfert objetTransfert6;

    private boolean setZoomOnlyOnce = false;
    private boolean canLaunch=false;
    Bundle bundle = new Bundle();

    private String locationNextArret;
    private String locationDestArret;
    private String envoiInfo;

    private String numeroBusInfo;
    private int numeroLigne;
    private int nbBus;



    /*******************************************************/
    /**
     * Fonctions de base
     * /
     *******************************************************/

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Création de la carte et des markers sur la carte
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        marker = gMap.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));

        //Ajout d'un cercle de precision
        circle = gMap.addCircle(new CircleOptions()
                .center(new LatLng(0, 0))
                .radius(0)
                .fillColor(Color.argb(100, 135, 225, 255)));
        circle.setStrokeWidth(0);

        //Ajout de marker avec une icone personnalisée
        markerArret = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Arret de départ")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arretprocheico)));

        markerArret2 = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.maisonico)));

        markerArret3 = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Arret d'arrivé")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.arretdestico)));

        //Bouton information
        final Button infoButton = (Button) findViewById(R.id.buttonInfo);
        infoButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (canLaunch) {
                    locationNextArret = objetTransfert.getNomArret();
                    locationDestArret = objetTransfert2.getNomArret();

                    envoiInfo = objetTransfert5.getMessage();
                    //Log.d("ConnectionPerma", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + envoiInfo);

                    Intent intent = new Intent(MainActivity.this, InformationActivity.class);


                    bundle.putString("ARRET", locationNextArret);
                    bundle.putString("TEMPSREEL", envoiInfo);
                    bundle.putString("ARRETDEST", locationDestArret);
                    bundle.putInt("NUMLINE", numeroLigne);
                    bundle.putString("NUMBUS", numeroBusInfo);

                    intent.putExtras(bundle);
                    MainActivity.this.startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Entrez une adresse et validez avant ...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mHandler = new Handler();

        /**
         * Bouton pour lancer tout les processus de connection
         * @author Lucas
         */
        Button bouton2 = (Button) findViewById(R.id.bouton2);
        bouton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Boolean pour savoir si une recherche a été lancé et si la bouton information peut être utilisé
                canLaunch = true;

                //Connection au serveur
                listArret();
                //Connection au geometer
                geometer();
                //Connection à l'arret de bus
                infoArret();

            }
        });


        /**
         * Bouton reset pour refresh la google map
         * @author Lucas
         */
        Button bouton = (Button) findViewById(R.id.bouton);
        bouton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    //permet d'arreter la boucle infinie d'actualisation des bus en temps réel dans ConnectionPermanente
                    objetTransfert5.setReset(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //Nétoyage de la google map
                gMap.clear();

                //Ajout des markers et cercle de base
                marker = gMap.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));

                circle = gMap.addCircle(new CircleOptions()
                        .center(new LatLng(0, 0))
                        .radius(0)
                        .fillColor(Color.argb(100, 135, 225, 255)));
                circle.setStrokeWidth(0);

                markerArret = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Arret de départ")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arretprocheico)));

                markerArret2 = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Destination")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.maisonico)));

                markerArret3 = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Arret d'arrivé")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.arretdestico)));

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        //Obtention de la référence du service
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Si le GPS est disponible, on s'y abonne
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            abonnementGPS();
        }
    }


    @Override
    public void onPause() {
            super.onPause();

        //On appelle la méthode pour se désabonner
        desabonnementGPS();
    }

    /**
     * Méthode permettant de s'abonner à la localisation par GPS.
     * @author Lucas
     */
    public void abonnementGPS() {
        //On s'abonne
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    /**
     * Méthode permettant de se désabonner de la localisation par GPS.
     * @author Lucas
     */
    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(final Location location) {

        //Mise à jour des coordonnées
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        //On ne zoom que la première fois
        if (setZoomOnlyOnce == false) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            setZoomOnlyOnce = true;
        }

        //Mise du marker de notre position et du cercle de précision
        marker.setPosition(latLng);
        try {
            circle.setRadius(location.getAccuracy());
            circle.setCenter(latLng);
        } catch (Exception e) {
            Log.e("MainActivity", "Impossible de récupérer l'accuracy");
        }

    }


    @Override
    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if ("gps".equals(provider)) {
            desabonnementGPS();
        }
    }


    @Override
    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if ("gps".equals(provider)) {
            abonnementGPS();
        }
    }


    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {}


    /*******************************************************/
    /**
     * Fonctions
     * /
     *******************************************************/

    /**
     * Récupère la liste des arrets en se connectant au serveur dont l'adresse est connue en dure
     * Et lance la connection à ce même serveur pour récupèrer le nombre de bus : nbBus()
     * @author Lucas
     */
    public void listArret() {
        //Connection au serveur pour récupérer la liste des arrets
        try {
            //Affiche qu'une connection est en cours
            Toast.makeText(getApplicationContext(), "Connection au serveur...", Toast.LENGTH_SHORT).show();

            //Objet mit en paramètre pour récupérer les informations depuis le serveur
            objetTransfert = new ObjetTransfert(IP_SERVEUR, PORT_SERVEUR);

            //Lancement de la connection en mettant en paramètre objetTransfort qui contient la requete
            objetTransfert.setRequete("{\"Requete\":\"LISTARRETS\"}");
            Thread t = new Thread(new Connection(objetTransfert));
            t.start();

        } catch (Exception e) {
            Log.e("MainActivity", "Echec de connection au serveur");
        }

        //On attend un peu pour que le thread soit fini
        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    //On récupère l'arret le plus proche
                    Calcul calcul = new Calcul(objetTransfert);
                    LatLng coordArret = calcul.arretLePlusProche(objetTransfert.getMessage(), marker.getPosition().latitude, marker.getPosition().longitude);

                    //On le marque sur la carte
                    markerArret.setTitle("Arret de départ: " + objetTransfert.getNomArret());
                    markerArret.setPosition(coordArret);

                    //Récupère le nombre de bus
                    nbBus();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, 4000);
    }

    /**
     * Récupère le nombre de bus  en se connectant au serveur dont l'adresse est connue en dure
     * Permet de créer le nombre de marker qu'il faut en fonction du nombre de bus dans : positionEnTempsReel()
     * @author Lucas
     */
    public void nbBus() {
        //Connection au serveur pour récupérer le nombre de bus
        try {
            //Objet mit en paramètre pour récupérer les informations depuis le serveur
            objetTransfert6 = new ObjetTransfert(IP_SERVEUR, PORT_SERVEUR);

            //Lancement de la connection en mettant en paramètre objetTransfort qui contient la requete
            objetTransfert6.setRequete("{\"Requete\":\"NBBUS\"}");
            Thread t = new Thread(new Connection(objetTransfert6));
            t.start();

        } catch (Exception e) {
            Log.e("MainActivity", "Echec de connection au serveur");
        }

        mHandler.postDelayed(new Runnable() {
            public void run() {
                try {
                    //Récupération de la réponse et mise dans nbBus
                    JSONObject nbBusJson = new JSONObject(objetTransfert6.getMessage());
                    nbBus = nbBusJson.getInt("NOMBREBUS");

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 4000);
    }

    /**
     * Permet d'établir la connection avec un service google qui nous renvoit les coordonnées en fonction d'une recherche effectuée
     * @author Lucas
     */
    public void geometer() {
        //Permet de trouver les coordonnées de la destination saisie dans la barre de recherche
        try {
            //Affiche qu'une connection est en cours
            Toast.makeText(getApplicationContext(), "Connection au Geometer...", Toast.LENGTH_SHORT).show();

            //Création de l'objet de transfert et ajout du texte récupéré dans la barre de recherche
            objetTransfert2 = new ObjetTransfert();
            EditText recherche = (EditText) findViewById(R.id.editText);
            objetTransfert2.setMessage(recherche.getText().toString());

            //Lancement de la connection
            Thread t2 = new Thread(new Geometer(objetTransfert2));
            t2.start();

        } catch (Exception e) {
            Log.e("MainActivity", "Echec de connection au Geometer");
        }

        //Permet de trouver l'arret de bus le plus proche de l'adresse saisie
        mHandler.postDelayed(new Runnable() {
            public void run() {

                //try pour si on a rentré un recherche vide
                try {
                    //Récupération de l'arret le plus proche de la destination
                    Calcul calcul = new Calcul(objetTransfert2);
                    LatLng coordArret = calcul.arretLePlusProche(objetTransfert.getMessage(), objetTransfert2.getLatLng().latitude, objetTransfert2.getLatLng().longitude);

                    //Ajout du marker de la destination et de l'arret le plus proche de celle-ci
                    markerArret2.setPosition(objetTransfert2.getLatLng());
                    markerArret3.setTitle("Arret d'arrivé: " + objetTransfert2.getNomArret());
                    markerArret3.setPosition(coordArret);

                    //Affiche les noms de arrets à prendre
                    //Toast.makeText(getApplicationContext(), "Prendre le bus de " + objetTransfert.getNomArret() + " à " + objetTransfert2.getNomArret(), Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, 4000);

    }

    /**
     * Connection à l'arret le plus proche pour récupère des informations
     * L'adresse IP et le port sont donnée par le serveur lors de l'éxecution de listArret();
     * Lance infosBus();
     * @author Lucas
     */
    public void infoArret() {

        //On attend un peu pour que le thread precedent soit fini (en effet, il nous faut l'adresse IP et le port)
        mHandler.postDelayed(new Runnable() {
            public void run() {
                //Connection à l'arret de bus  pour récupérer les information du bus
                try {
                    //Affiche qu'une connection est en cours
                    Toast.makeText(getApplicationContext(), "Connection à l'arret...", Toast.LENGTH_SHORT).show();

                    //Objet mit en paramètre pour récupérer les infos depuis le serveur
                    objetTransfert3 = new ObjetTransfert(objetTransfert.getAdresseIP(), objetTransfert.getPort());
                    objetTransfert3.setRequete("{\"Requete\":\"BUS\",\"ArretArrive\":\"" + objetTransfert2.getNomArret() + "\"}");

                    //Lancement de la connection en mettant en paramètre objetTransfort qui contient la requete
                    Thread t = new Thread(new Connection(objetTransfert3));
                    t.start();

                } catch (Exception e) {
                    Log.e("MainActivity", "Echec de connection à l'arret");
                }

                //Permet d'afficher qu'un bus à été trouver, et de récupère son numéro
                infoBus();
            }
        }, 4000);

    }

    /**
     * Permet d'afficher qu'un bus à bien été trouvé à partir des connections précedente et de récupèrer son numéro
     * Lance positionEnTempsReel()
     * @author Lucas
     */
    public void infoBus() {
        //On attend un peu pour que le thread soit fini
        mHandler.postDelayed(new Runnable() {
            public void run() {

                try {
                    //Affiche un message du numéro de bus à prendre
                    JSONObject numBus = new JSONObject(objetTransfert3.getMessage());
                    numBus = (JSONObject) numBus.get("BUS");

                    numeroBusInfo = numBus.getString("Bus");
                    Toast.makeText(getApplicationContext(), "Bus trouvé", Toast.LENGTH_SHORT).show();

                    //Permet d'afficher tout les bus en temps réel sur la carte
                    positionEnTempsReel();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 4000);
    }

    /**
     * Permet d'afficher la position des bus en temps réel sur la carte
     * @author Lucas
     */
    public void positionEnTempsReel() {

        //Ajout des informations de connection à l'arret de bus
        objetTransfert5 =  new ObjetTransfert(objetTransfert.getAdresseIP(), objetTransfert.getPort());

        //Création d'une liste de markers
        ArrayList<Marker> listMarker = new ArrayList<>();

        //Création des markers de bus
        for( int i = 0; i < nbBus; i++) {
            Marker markerBus = gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Bus")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.busgeneralico)));

            listMarker.add(i, markerBus);
        }

        //Ajout à l'objet des différentes informations utile pour le prochain thread
        objetTransfert5.setListMarker(listMarker);
        objetTransfert5.setNbBus(nbBus);
        objetTransfert5.setMessage(numeroBusInfo);
        objetTransfert5.setReset(true);
        objetTransfert5.setRequete("{\"Requete\":\"LISTBUS\",\"ArretArrive\":\""+objetTransfert2.getNomArret()+"\"}");

        //Lancement de la connection
        Thread t = new Thread(new ConnectionPermanente(objetTransfert5));
        t.start();

    }
}
