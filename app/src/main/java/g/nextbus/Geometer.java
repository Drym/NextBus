package g.nextbus;

/**
 * Created by jonathan on 11/01/16.
 */

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Geometer {

    private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
    double lng;
    double lat;
    LatLng latLng;

    public InputStream getJSONByGoogle(String fullAddress) throws IOException {


        URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");

        URLConnection conn = url.openConnection();


        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        return conn.getInputStream();

        /*
        IOUtils.copy(conn.getInputStream(), output);

        String out = output.toString();
        */


        /*
        try {
            JSONObject jsonObject = new JSONObject(out);

            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            //System.out.println(lat);

            //System.out.println(lng);
            latLng = new LatLng(lat, lng);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        */
       // return "aaaa";

    }

}
