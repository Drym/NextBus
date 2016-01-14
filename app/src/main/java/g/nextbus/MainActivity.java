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
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Iterator;

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
    private static String IP_SERVEUR = "10.212.115.218";
    private static int PORT_SERVEUR = 4444;

    private LocationManager locationManager;
    private GoogleMap gMap;
    private Handler mHandler;

    private Marker marker;
    private Marker markerArret;
    private Marker markerArret2;
    private Marker markerArret3;

    private ObjetTransfert objetTransfert;
    private ObjetTransfert objetTransfert2;
    private ObjetTransfert objetTransfert3;
    private ObjetTransfert objetTransfert4;
    private ObjetTransfert objetTransfert5;

    private boolean setZoomOnlyOnce = false;

    /*******************************************************/
    /**
     * FONCTIONS.
     * /
     *******************************************************/

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button loginButton = (Button) findViewById(R.id.buttonInfo);
        loginButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainActivity.this, InformationActivity.class);
               startActivity(intent);
           }
        });


        //Création de la carte et des marker sur la carte
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        marker = gMap.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));
        markerArret = gMap.addMarker(new MarkerOptions().title("Arret le plus proche").position(new LatLng(0, 0)));
        markerArret2 = gMap.addMarker(new MarkerOptions().title("Maison").position(new LatLng(0, 0)));

        //markerArret2 = gMap.addMarker(new MarkerOptions().title("Maison").icon(BitmapDescriptorFactory.fromResource(R.drawable.maison)).position(new LatLng(0, 0)));

        markerArret3 = gMap.addMarker(new MarkerOptions().title("Arret d'arrivé").position(new LatLng(0, 0)));
        mHandler = new Handler();

        //Bouton pour lancer tout le processus de connection
        Button bouton2 = (Button) findViewById(R.id.bouton2);
        bouton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Connection au serveur pour récupérer la liste des arrets
                try {
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
                            ArretProche arretProche = new ArretProche(objetTransfert);
                            LatLng coordArret = arretProche.arretLePlusProche(objetTransfert.getMessage(), 0, 0);

                            //On le marque sur la carte
                            markerArret.setTitle("Arret le plus proche: " + objetTransfert.getNomArret());
                            markerArret.setPosition(coordArret);


                            //markerArret = gMap.addMarker(new MarkerOptions().position(coordArret).title("Arret le plus proche: " + objetTransfert.getNomArret().icon(BitmapDescriptorFactory.fromResource(R.drawable.test)));

                            /*
                            File file = new File("test.png");
                            Bitmap bit = BitmapFactory.decodeFile(String.valueOf(file));
                            arretProche = new ArretProche(objetTransfert);
                            coordArret = arretProche.arretLePlusProche(objetTransfert.getMessage(), latitudeUser, longitudeUser);
                            //On le marque sur la carte
                            MarkerOptions marker = new MarkerOptions().position(coordArret).icon(BitmapDescriptorFactory.fromBitmap(bit));
                            gMap.addMarker(marker);
                            */
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }, 4000);

                //Permet de trouver les coordonnées de la destination saisie dans la barre de recherche
                try {
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

                //Permet de trouver le bus le plus proche de l'adresse saisie
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        //try pour si on a rentré un recherche vide
                        try {
                            //Récupération de l'arret le plus proche de la destination
                            ArretProche arretProche = new ArretProche(objetTransfert2);
                            LatLng coordArret = arretProche.arretLePlusProche(objetTransfert.getMessage(), objetTransfert2.getLatLng().latitude, objetTransfert2.getLatLng().longitude);

                            //Ajout du marker de la destination et de l'arret le plus proche de celle-ci
                            markerArret2.setPosition(objetTransfert2.getLatLng());
                            markerArret3.setTitle("Arret d'arrivé: " + objetTransfert2.getNomArret());
                            markerArret3.setPosition(coordArret);

                            //Affiche les noms de arrets à prendre
                            Toast.makeText(getApplicationContext(), "Prendre le bus de " + objetTransfert.getNomArret() + " à " + objetTransfert2.getNomArret(), Toast.LENGTH_SHORT).show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 4000);


                //On attend un peu pour que le thread soit fini
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        //Connection à l'arret pour récupérer les infos du bus
                        try {
                            Toast.makeText(getApplicationContext(), "Connection à l'arret...", Toast.LENGTH_SHORT).show();

                            //Objet mit en paramètre pour récupérer les infos depuis le serveur
                            objetTransfert3 = new ObjetTransfert(objetTransfert.getAdresseIP(), objetTransfert.getPort());
                            objetTransfert3.setRequete("{\"Requete\":\"BUS\",\"ArretArrive\":\"" + objetTransfert2.getNomArret() + "\"}");

                            //objetTransfert3.setKeepAlive("{\"Requete\":\"LISTBUS\"}");
                            //ICI

                            //Lancement de la connection en mettant en paramètre objetTransfort qui contient la requete
                            Thread t = new Thread(new Connection(objetTransfert3));
                            t.start();

                        } catch (Exception e) {
                            Log.e("MainActivity", "Echec de connection à l'arret");
                        }

                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                //Connection au bus pour récupérer les information du bus
                                try {
                                    Toast.makeText(getApplicationContext(), "Connection au bus...", Toast.LENGTH_SHORT).show();

                                    //Objet mit en paramètre pour récupérer les infos depuis le serveur
                                    objetTransfert4 = new ObjetTransfert(objetTransfert3.getAdresseIP(), objetTransfert3.getPort());

                                    //Lancement de la connection en mettant en paramètre objetTransfort qui contient la requete
                                    objetTransfert4.setRequete("{\"Requete\":\"BUS\"}");
                                    Thread t = new Thread(new Connection(objetTransfert4));
                                    t.start();

                                } catch (Exception e) {
                                    Log.e("MainActivity", "Echec de connection au bus");
                                }

                                //On attend un peu pour que le thread soit fini
                                mHandler.postDelayed(new Runnable() {
                                    public void run() {

                                        try {
                                            //Affiche un message du numéro de bus à prendre
                                            JSONObject numBus = new JSONObject(objetTransfert4.getMessage());
                                            numBus = (JSONObject) numBus.get("BUS");
                                            Toast.makeText(getApplicationContext(), "Prendre le bus numéro " + numBus.getString("Bus"), Toast.LENGTH_SHORT).show();

                                            positionEnTempsReel();

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 4000);
                            }
                        }, 4000);
                    }
                }, 4000);
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
     */
    public void abonnementGPS() {
        //On s'abonne
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);
    }

    /**
     * Méthode permettant de se désabonner de la localisation par GPS.
     */
    public void desabonnementGPS() {
        //Si le GPS est disponible, on s'y abonne
        locationManager.removeUpdates(this);
    }


    @Override
    public void onLocationChanged(final Location location) {

        //Mise à jour des coordonnées
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (setZoomOnlyOnce == false) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            setZoomOnlyOnce = true;
        }
        marker.setPosition(latLng);

        /*
        Toast.makeText(getApplicationContext(), (int)location.getAccuracy(), Toast.LENGTH_SHORT).show();

        Circle circle = gMap.addCircle(new CircleOptions()
                .center(latLng)
                .radius(location.getAccuracy())
                .strokeColor(Color.RED)
                .fillColor(Color.argb((int)0.4, 135, 225, 255)));
        */
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
    public void onStatusChanged(final String provider, final int status, final Bundle extras) {
    }


    public void positionEnTempsReel() {

        objetTransfert5 =  new ObjetTransfert(objetTransfert.getAdresseIP(), objetTransfert.getPort());

        objetTransfert5.setRequete("{\"Requete\":\"LISTBUS\"}");
        objetTransfert5.setgMap(gMap);
        Thread t = new Thread(new Connection(objetTransfert5));
        t.start();

    }
}
