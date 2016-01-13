package g.nextbus;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.io.File;

import fr.rolandl.blog_gps.R;

/**
 * MainActivity.
 * @author Lucas
 *
 */
public class MainActivity extends Activity implements LocationListener {
    /*******************************************************/
    /** ATTRIBUTS.
     /*******************************************************/
    private LocationManager locationManager;
    private GoogleMap gMap;
    private Marker marker;
    private Marker markerArret;
    private Marker markerArret2;
    private Marker markerArret3;
    private EditText recherche;
    private Button bouton2;
    private Handler mHandler;
    private ObjetTransfert objetTransfert;
    private ObjetTransfert objetTransfert2;
    private ObjetTransfert objetTransfert3;
    private ObjetTransfert objetTransfert4;
    private String listArret;
    private ArretProche arretProche;
    private double latitudeUser = 0;
    private double longitudeUser = 0;
    private boolean setZoomOnlyOnce = false;
    private LatLng coordArret;
    private String IPArret = "10.212.115.218";
    private int portArret = 4444;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Création de la carte et des marker sur la carte
        gMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        marker = gMap.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));
        markerArret = gMap.addMarker(new MarkerOptions().title("Arret le plus proche").position(new LatLng(0, 0)));
        markerArret2 = gMap.addMarker(new MarkerOptions().title("Maison").position(new LatLng(0, 0)));
        markerArret3 = gMap.addMarker(new MarkerOptions().title("Arret d'arrivé").position(new LatLng(0, 0)));
        mHandler = new Handler();

        //bouton pour lancer la connection et trouver l'arret le plus proche
        bouton2 = (Button) findViewById(R.id.bouton2);
        bouton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Connection au serveur...", Toast.LENGTH_SHORT).show();

                //Objet mit en paramètre pour récupérer les infos depuis le serveur
                objetTransfert = new ObjetTransfert(IPArret, portArret);
                //Lancement de la connection
                objetTransfert.setRequete("{\"Requete\":\"LISTARRETS\"}");
                Thread t = new Thread(new Connection(objetTransfert));
                t.start();

                //On attend un peu pour que le thread soit fini
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        //On récupère l'arret le plus proche
                        arretProche = new ArretProche(objetTransfert);

                        coordArret = arretProche.arretLePlusProche(objetTransfert.getMessage(), latitudeUser, longitudeUser);

                        //On le marque sur la carte
                        markerArret.setPosition(coordArret);
                        /*
                        File file = new File("test.png");

                        Bitmap bit = BitmapFactory.decodeFile(String.valueOf(file));

                        arretProche = new ArretProche(objetTransfert);

                        coordArret = arretProche.arretLePlusProche(objetTransfert.getMessage(), latitudeUser, longitudeUser);
                        //On le marque sur la carte

                        MarkerOptions marker = new MarkerOptions().position(coordArret).icon(BitmapDescriptorFactory.fromBitmap(bit));
                        gMap.addMarker(marker);
                        */
                    }
                }, 4000);
            //}
        //});

        //Bouton pour valider le texte
        /*
        bouton2 = (Button) findViewById(R.id.bouton2);
        bouton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        */
                //Permet de trouver les coordonnées de la destination saisie dans la barre de recherche
                try {
                    Toast.makeText(getApplicationContext(), "Connection au Geometer...", Toast.LENGTH_SHORT).show();

                    objetTransfert2 = new ObjetTransfert();

                    recherche = (EditText) findViewById(R.id.editText);
                    objetTransfert2.setMessage(recherche.getText().toString());
                    Thread t2 = new Thread(new Geometer(objetTransfert2));
                    t2.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Permet de trouver le bus le plus proche de maison
                mHandler.postDelayed(new Runnable() {
                    public void run() {

                        //try pour si on a rentré un recherche vide
                        try {
                            arretProche = new ArretProche(objetTransfert2);

                            coordArret = arretProche.arretLePlusProche(objetTransfert.getMessage(), objetTransfert2.getLatLng().latitude, objetTransfert2.getLatLng().longitude);

                            markerArret2.setPosition(objetTransfert2.getLatLng());
                            markerArret3.setPosition(coordArret);

                            Toast.makeText(getApplicationContext(), "Prendre le bus de " + objetTransfert.getNomArret() + " à " + objetTransfert2.getNomArret(), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, 4000);

            //}
        //});

        //Bouton pour valider le texte
        /*
        bouton3 = (Button) findViewById(R.id.bouton3);
        bouton3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
        */

                //On attend un peu pour que le thread soit fini
                mHandler.postDelayed(new Runnable() {
                    public void run() {
//                      Log.d("MainActivity", objetTransfert3.getMessage());

                        //Connection à l'arret pour récupérer les infos du bus
                        Toast.makeText(getApplicationContext(), "Connection à l'arret...", Toast.LENGTH_SHORT).show();

                        //Objet mit en paramètre pour récupérer les infos depuis le serveur
                        objetTransfert3 = new ObjetTransfert(objetTransfert.getAdresseIP(), objetTransfert.getPort());

                        //Lancement de la connection
                        objetTransfert3.setRequete("{\"Requete\":\"BUS\",\"ArretArrive\":\"" + objetTransfert2.getNomArret() + "\"}");
                        Thread t = new Thread(new Connection(objetTransfert3));
                        t.start();


                        mHandler.postDelayed(new Runnable() {
                            public void run() {
                                //Connection au bus pour récupérer les information du bus
                                Toast.makeText(getApplicationContext(), "Connection au bus...", Toast.LENGTH_SHORT).show();

                                //Objet mit en paramètre pour récupérer les infos depuis le serveur
                                objetTransfert4 = new ObjetTransfert(objetTransfert3.getAdresseIP(), objetTransfert3.getPort());

                                //Lancement de la connection
                                objetTransfert4.setRequete("{\"Requete\":\"BUS\"}");
                                Thread t = new Thread(new Connection(objetTransfert4));
                                t.start();

                                //On attend un peu pour que le thread soit fini
                                mHandler.postDelayed(new Runnable() {
                                    public void run() {

                                        try {
                                            JSONObject numBus = new JSONObject(objetTransfert4.getMessage());
                                            numBus = (JSONObject) numBus.get("BUS");
                                            Toast.makeText(getApplicationContext(), "Prendre le bus numéro "+ numBus.getString("Bus") ,Toast.LENGTH_SHORT).show();
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

                //Récupère la latitude et longitude de notre position
                latitudeUser = location.getLatitude();
                longitudeUser = location.getLongitude();
                //latitude.setText("Latitude : " + location.getLatitude());
                //longitude.setText("Longitude : " + location.getLongitude());

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

        }
