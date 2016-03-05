package edu.fau.ngamarra2014.sync_care;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Nick on 3/4/2016.
 */
public class CheckCreds {

    private String username;
    private String password;
    private static String login_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/login.php";

    private JSONObject json;
    private JSONArray response = null;

    public CheckCreds(String username, String password){
        this.username = username;
        this.password = password;
    }

    public JSONArray signin(){
        JSONParser jsonParser = new JSONParser();

        QueryString query = new QueryString("username", username);
        query.add("password", password);

        jsonParser.setParams(query);

        json = jsonParser.makeHttpRequest(login_url, "POST");

        try{
            if(!json.has("error")){
                response = json.getJSONArray("user");
                Log.i("Sign in", "signin: Success - " + response);
                return response;
            }else{
                Log.i("Sign in", "signin: Failed");
            }

        }catch(JSONException e){
            e.printStackTrace();
        }
        return response;
    }
}
