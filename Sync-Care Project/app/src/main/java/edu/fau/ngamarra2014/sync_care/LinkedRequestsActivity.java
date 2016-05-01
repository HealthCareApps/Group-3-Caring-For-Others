package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Adapters.LinkedRequestAdapter;
import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;


public class LinkedRequestsActivity extends NavigationActivity {

    ArrayList<JSONObject> specialist = new ArrayList<>();
    ArrayList<JSONObject> patients = new ArrayList<>();
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_linked_requests, null, false);
        drawer.addView(contentView, 0);

        getSupportActionBar().setTitle("Pending Request");

        new CheckLinkedRequests().execute();

    }
    class CheckLinkedRequests extends AsyncTask<String, String, String> {
        JSONParser jsonParser = new JSONParser();
        JSONObject response;
        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/linkedrequests.php";

        protected String doInBackground(String... args) {
            jsonParser.setParams(new QueryString("id", Integer.toString(user.getID())));

            try {
                response = jsonParser.makeHttpRequest(url, "GET");
                if(!response.has("Internet")){
                    for(int i = 0; i < response.getJSONArray("Patients").length(); i++){
                        patients.add(response.getJSONArray("Patients").getJSONObject(i));
                        specialist.add(response.getJSONArray("Specialists").getJSONObject(i));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
            LinkedRequestAdapter adapter = new LinkedRequestAdapter(patients, specialist, getApplicationContext());
            ListView view = (ListView) findViewById(R.id.listviewpatient);
            view.setAdapter(adapter);

            if(response.has("Internet")){
                Toast toast = Toast.makeText(LinkedRequestsActivity.this, "Connect to internet to check pending requests", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}