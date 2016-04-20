package edu.fau.ngamarra2014.sync_care.Database;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class JSONParser {

    static InputStream is = null;
    static JSONObject response = null;
    static String json = "";

    private String params = "";

    // constructor
    public JSONParser() {

    }

    public void setParams(QueryString query){
        params = query.toString();
    }
    // function get json from url
    // by making HTTP POST or GET mehtod
    public JSONObject makeHttpRequest(String url, String method) {
        // Making HTTP request
        try {

            // check for request method
            if(method == "POST"){
                byte[] postData = params.getBytes( StandardCharsets.UTF_8 );
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
                urlConnection.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
                wr.write(postData);

                is = urlConnection.getInputStream();
                wr.close();
                urlConnection.disconnect();

            }else if(method == "GET"){
                HttpURLConnection urlConnection = (HttpURLConnection) new URL(url + "?" + params).openConnection();
                is = urlConnection.getInputStream();
                urlConnection.disconnect();
            }


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            int ch;
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder result = new StringBuilder();
            String line;
            StringBuffer sb = new StringBuffer();

            while ((line = reader.readLine()) != null){
                result.append(line);
            }
            /*while ((ch = is.read()) != -1) {
                sb.append((char) ch);
            }*/

            json = result.toString();
//            json = sb.toString();
            is.close();

        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        try {
            response = new JSONObject(json);
        } catch (JSONException e) {
            Log.i("Error", e.toString());
            Log.i("String", json);
        }
        return response;

    }
}
