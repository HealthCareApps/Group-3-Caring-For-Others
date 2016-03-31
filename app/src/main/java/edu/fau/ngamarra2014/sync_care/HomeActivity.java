package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends NavigationActivity{

    Globals globals = Globals.getInstance();

    ImageButton addPatient, patients;
    String userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.home_activity, null, false);
        drawer.addView(contentView, 0);

        addPatient = (ImageButton) findViewById(R.id.addPatient);
        patients = (ImageButton) findViewById(R.id.patients);

        try {
            userid = globals.getuser().getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addPatient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddPatient.class);
                startActivity(i);
            }
        });

        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GrabPatients().execute();
            }
        });
    }

    class GrabPatients extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String grab_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/getPatients.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Getting Patients...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", userid);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(grab_patient_url, "GET");

            try {
                if(json.length() > 0){
                    Intent i = new Intent(getApplicationContext(), PatientListActivity.class);
                    globals.setPatients(json);
                    startActivity(i);
                } else {
                    // failed
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }
}
