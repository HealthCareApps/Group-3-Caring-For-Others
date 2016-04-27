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
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;


public class LinkedRequestsActivity extends Activity {

    ArrayList<Patient> LinkedRequests = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();

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
            QueryString query = new QueryString("id", "");

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(url, "GET");

            try {
                for(int i = 0; i < response.getJSONArray("Patients").length(); i++){
                    LinkedRequests.add(new Patient(response.getJSONArray("Patients").getJSONObject(i)));
                    list.add(response.getJSONArray("Patients").getJSONObject(i).getString("first"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url) {
            LinkedRequestAdapter adapter = new LinkedRequestAdapter(list,list,getApplicationContext());
            ListView view = (ListView) findViewById(R.id.listviewpatient);
            view.setAdapter(adapter);
        }
    }
}