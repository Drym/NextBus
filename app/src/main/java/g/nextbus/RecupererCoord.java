package g.nextbus;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lucas on 14/01/2016.
 */
public class RecupererCoord {

    public RecupererCoord() {}

    public LatLng getCoord (String chaine) {

        LatLng latLng = null;

        try {
            JSONObject reponseJSON = new JSONObject(chaine);
            latLng = new LatLng(reponseJSON.getDouble("Latitude"), reponseJSON.getDouble("Longitude"));

        } catch(JSONException e){
            e.printStackTrace();
        }

        return latLng;
    }

}
