package g.nextbus;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import fr.rolandl.blog_gps.R;

/**
 * Created by jonathan on 14/01/16.
 */

public class InformationActivity extends Activity {

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.information);

          //while (true) {
              Bundle bundle = getIntent().getExtras();

              //recuperation des bundles
              String arretProcheRecup = bundle.getString("ARRET");
              String arretDestiRecup = bundle.getString("ARRETDEST");
              String numeroBus = bundle.getString("NUMBUS");
              String placeRestante = bundle.getString("PLACE");
              int numeroLigne = bundle.getInt("NUMLINE");
              //Log.d("ConnectionPerma", "aaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + placeRestante);

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

              TextView t6 = (TextView) findViewById(R.id.textView11);
              t6.setText(placeRestante);

          }
      //}

}
