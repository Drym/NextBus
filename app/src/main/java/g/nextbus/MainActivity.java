package g.nextbus;

import android.app.Activity;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private TextView latitude;
    private TextView longitude;
    private Button bouton1;
    private String listArret;
    private ArretProche arretProche;
    private Connection conec;
    private double latitudeUser = 0;
    private double longitudeUser = 0;
    private boolean setZoomOnlyOnce = false;
    public LatLng coordArret;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Création de la carte et des marker sur la carte
        gMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        marker = gMap.addMarker(new MarkerOptions().title("Vous êtes ici").position(new LatLng(0, 0)));
        markerArret = gMap.addMarker(new MarkerOptions().title("Arret le plus proche").position(new LatLng(1, 1)));


        //Création des textes de latitude et longitude
        latitude = (TextView)findViewById(R.id.latitude);
        longitude = (TextView)findViewById(R.id.longitude);

        bouton1 = (Button)findViewById(R.id.bouton1);
        bouton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //conec = new Connection(listArret);
                //Thread t=new Thread(conec);
                //t.start();

                coordArret = arretProche.arretLePlusProche(listArret, latitudeUser, longitudeUser);
                markerArret.setPosition(coordArret);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        //Obtention de la référence du service
        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);

        //Si le GPS est disponible, on s'y abonne
        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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

        /*
        //On affiche dans un Toat la nouvelle Localisation
        final StringBuilder msg = new StringBuilder("lat : ");
        msg.append(location.getLatitude());
        msg.append( "; lng : ");
        msg.append(location.getLongitude());

        Toast.makeText(this, msg.toString(), Toast.LENGTH_SHORT).show();
        */

        //Mise à jour des coordonnées
        final LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        if (setZoomOnlyOnce == false) {
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            setZoomOnlyOnce = true;
        }
        marker.setPosition(latLng);

        //Récupère la latitude et longitude de notre position
        latitudeUser =  location.getLatitude();
        longitudeUser = location.getLongitude();
        latitude.setText("Latitude : " + location.getLatitude());
        longitude.setText("Longitude : " + location.getLongitude());

    }


    @Override
    public void onProviderDisabled(final String provider) {
        //Si le GPS est désactivé on se désabonne
        if("gps".equals(provider)) {
            desabonnementGPS();
        }
    }


    @Override
    public void onProviderEnabled(final String provider) {
        //Si le GPS est activé on s'abonne
        if("gps".equals(provider)) {
            abonnementGPS();
        }
    }


    @Override
    public void onStatusChanged(final String provider, final int status, final Bundle extras) { }
}
