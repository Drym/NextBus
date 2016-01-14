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
          String arretProcheRecup = bundle.getString("ARRET");

          TextView t1 = (TextView) findViewById(R.id.textView2);
          t1.setText(arretProcheRecup);
      }

}
