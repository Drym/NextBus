package g.nextbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONObject;

import fr.rolandl.blog_gps.R;

/**
 * Created by jonathan on 14/01/16.
 */

public class InformationActivity extends Activity implements Runnable {

    private String numeroBus;
    private String place;
    private String distarretarret;
    private String distbusarret;
    private String vitesse;
    public boolean isPassed;
    public boolean boucle = true;

    private String temps1;

    //TextView t6 = (TextView) findViewById(R.id.textView11);

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.information);

          Bundle bundle = getIntent().getExtras();

          //recuperation des bundles
          int numeroLigne = bundle.getInt("NUMLINE");
          String arretProcheRecup = bundle.getString("ARRET");
          String arretDestiRecup = bundle.getString("ARRETDEST");
          numeroBus = bundle.getString("NUMBUS");
            try {
                Bundle bundle2 = getIntent().getExtras();
                String infoTempsReel = bundle2.getString("TEMPSREEL");
                JSONObject infos = new JSONObject(infoTempsReel);
                infos = (JSONObject) infos.get(numeroBus);
                infos = (JSONObject) infos.get("BUS");
                vitesse = infos.getString("Vitesse");
                distbusarret = infos.getString("DistanceBusArret");
                //Log.d("ConnectionPerma", "distance Bus arret " + distbusarret);
                //Log.d("ConnectionPerma", "vitesse du bus " + vitesse);
            }
            catch(Exception e){
                e.printStackTrace();
            }


          String placeRestante = bundle.getString("PLACE");

          TextView t1 = (TextView) findViewById(R.id.textView2);
          t1.setText(arretProcheRecup);

          TextView t2 = (TextView) findViewById(R.id.textView7);
          t2.setText(arretProcheRecup);

          TextView t3 = (TextView) findViewById(R.id.textView9);
          t3.setText(arretDestiRecup);

          TextView t4 = (TextView) findViewById(R.id.textView5);
          t4.setText("Ligne 1");

          TextView t5 = (TextView) findViewById(R.id.textView4);
          t5.setText(numeroBus);



          Thread h1;
          h1=new Thread(this);
          h1.start();

          }



    /*public String calculTempsArretArrive(){
        String vit=vitesse;
        double vitesseReel = Double.parseDouble(vit);
        double distanceArretAArret = Double.parseDouble(distarretarret);
        double vitesseConvertie = vitesseReel * 16.6667;
        double tempsAvantArrivee = distanceArretAArret / vitesseConvertie;
        Math.round(tempsAvantArrivee);
        if(!isPassed){
            double calculTempsArretPerso = Double.parseDouble(calculTempsArretPerso());
            tempsAvantArrivee = tempsAvantArrivee + calculTempsArretPerso;
        }
        else{
            tempsAvantArrivee = tempsAvantArrivee;
        }
        String returnn = ""+vitesseConvertie;

        return returnn;
    }*/

    @Override
    public void run() {

        TextView t6 = (TextView) findViewById(R.id.textView11);
        TextView t7 = (TextView) findViewById(R.id.textView13);

        while(boucle) {


            Bundle bundle = getIntent().getExtras();

            String infoTempsReel = bundle.getString("TEMPSREEL");



            try {



                JSONObject infos = new JSONObject(infoTempsReel);
                infos = (JSONObject) infos.get(numeroBus);
                infos = (JSONObject) infos.get("BUS");
                place = infos.getString("PlacesRestantes");
                vitesse = infos.getString("Vitesse");
                distbusarret = infos.getString("DistanceBusArret");
                double vites = Double.parseDouble(vitesse);
                double dist = Double.parseDouble(distbusarret);
                double temp = (dist / vites)*3.6;
                temp = Math.round(temp);
                int arround = (int)temp;
                temps1 = ""+arround;


                //Log.d("ppepeppepepe", "peloepleolpeloeo" + temp);
                /*Log.d("ppepeppepepe", "peloepleolpeloeo"+vites);
                Log.d("ppepeppepepe", "peloepleolpeloeo"+dist);*/

                t6.setText(place);
                t7.setText(temps1);
                Thread.sleep(1000);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onBackPressed(){
        boucle = false;
        super.onBackPressed();
    }


    //}

}
