package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Adapters.LinkedRequestAdapter;
import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;


public class LinkedRequestsActivity extends Activity {

    ArrayList<JSONObject> specialist = new ArrayList<>();
    ArrayList<JSONObject> patients = new ArrayList<>();
    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_linked_requests);

        new CheckLinkedRequests().execute();

    }
    class CheckLinkedRequests extends AsyncTask<String, String, String> {
        JSONParser jsonParser = new JSONParser();
        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/linkedrequests.php";

        protected String doInBackground(String... args) {
            jsonParser.setParams(new QueryString("id", Integer.toString(user.getID())));
            JSONObject response = jsonParser.makeHttpRequest(url, "GET");

            try {
                for(int i = 0; i < response.getJSONArray("Patients").length(); i++){
                    patients.add(response.getJSONArray("Patients").getJSONObject(i));
                    specialist.add(response.getJSONArray("Specialists").getJSONObject(i));
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
        }
    }
}