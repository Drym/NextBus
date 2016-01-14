package g.nextbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import fr.rolandl.blog_gps.R;

/**
 * Created by user on 14/01/16.
 */

public class InformationActivity extends Activity {

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.information);


          Bundle bundle = getIntent().getExtras();

          //recuperation des bundles
          String arretProcheRecup = bundle.getString("ARRET");
          String arretDestiRecup = bundle.getString("ARRETDEST");
          int numeroLigne = bundle.getInt("NUMLINE");

          TextView t1 = (TextView) findViewById(R.id.textView2);
          t1.setText(arretProcheRecup);

          TextView t2 = (TextView) findViewById(R.id.textView7);
          t2.setText(arretProcheRecup);

          TextView t3 = (TextView) findViewById(R.id.textView9);
          t3.setText(arretDestiRecup);

          TextView t4 = (TextView) findViewById(R.id.textView4);
          t4.setText(arretDestiRecup);


      }

}
