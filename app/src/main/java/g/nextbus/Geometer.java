package g.nextbus;

/**
 * Created by jonathan on 11/01/16.
 */

import com.google.android.gms.maps.model.LatLng;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Geometer implements Runnable  {

    private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
    private double lng;
    private double lat;
    private LatLng latLng;
    private ObjetTransfert objetTransfert;
    private String fullAddress;
    private URLConnection conn;
    private String out;


    public Geometer (ObjetTransfert objetTransfert) {
        this.objetTransfert = objetTransfert;

    }


    public URL getJSONByGoogle() throws IOException {

        fullAddress = objetTransfert.getMessage();
        URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8") + "&sensor=false");

        return url;

    }

     public void run() {

         URL url = null;
         try {
             url = getJSONByGoogle();
             conn = url.openConnection();

             ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

             IOUtils.copy(conn.getInputStream(), output);

             out = output.toString();

         } catch (IOException e) {
             e.printStackTrace();
         }


        try {
            JSONObject jsonObject = new JSONObject(out);

            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            latLng = new LatLng(lat, lng);

            objetTransfert.setLatLng(latLng);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
