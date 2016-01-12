package g.nextbus;

/**
 * Created by jonathan on 11/01/16.
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Geometer {

    private static final String URL = "http://maps.googleapis.com/maps/api/geocode/json";
    double lng;
    double lat;

    public String getJSONByGoogle(String fullAddress) throws IOException {

        URL url = new URL(URL + "?address=" + URLEncoder.encode(fullAddress, "UTF-8")+ "&sensor=false");

        URLConnection conn = url.openConnection();

        ByteArrayOutputStream output = new ByteArrayOutputStream(1024);

        IOUtils.copy(conn.getInputStream(), output);

        String out = output.toString();

        try {
            JSONObject jsonObject = new JSONObject(out);

            lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");

            System.out.println(lat);

            System.out.println(lng);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return out;

    }

}
