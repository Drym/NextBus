package g.nextbus;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import fr.rolandl.blog_gps.R;

/**
 * Created by user on 14/01/16.
 */
public class InformationActivity extends Activity {

  /*  @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        final Button chercherItineraire = (Button) findViewById(R.id.button);
        chercherItineraire.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InformationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }*/
      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.information);
      }

}