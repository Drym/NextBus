package g.nextbus;

/**
 * Created by jonathan on 11/01/16.
 */

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Geometer {

    private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
    double lng;
    double lat;
    LatLng latLng;
    private InputStream is;


    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line + "\n");
        }
        return builder.toString();
    }

    public String getJSONByGoogle(String fullAddress) throws IOException {


        /*URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");

        URLConnection conn = url.openConnection();


        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        return conn.getInputStream();
        */
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



        URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        // Starts the query
        conn.connect();
        int response = conn.getResponseCode();
        is = conn.getInputStream();

        // Convert the InputStream into a string
        String contentAsString = readIt(is);


        try {
            JSONObject jsonObject = new JSONObject(contentAsString);

            double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            System.out.println(lat);
            System.out.println(lng);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return contentAsString;


    }

}
