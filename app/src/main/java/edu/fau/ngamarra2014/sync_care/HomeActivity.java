package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity{

    Button rx;
    ImageButton addPatient, patients;
    String username;
    int userid;
    JSONArray array;

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String grab_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/getPatients.php";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        rx = (Button) findViewById(R.id.rxButton);
        addPatient = (ImageButton) findViewById(R.id.addPatient);
        patients = (ImageButton) findViewById(R.id.patients);

        username = getIntent().getStringExtra("username");
        userid = getIntent().getIntExtra("id", 0);

        setTitle("Welcome, " + username);

        addPatient.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AddPatient.class);
                i.putExtra("id", userid); //Patients id for the prescriptions
                startActivity(i);
            }
        });

        patients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GrabPatients().execute();
                Intent i = new Intent(getApplicationContext(), PatientListActivity.class);
                startActivity(i);
            }
        });

        rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), RxActivity.class);
                i.putExtra("id", userid); //Patients id for the prescriptions
                startActivity(i);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent set = new Intent(getApplicationContext(), SettingsActivity.class);
                set.putExtra("settingsFor", username);
                startActivity(set);
                return true;
            case R.id.logout:
                Intent signin = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(signin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class GrabPatients extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(HomeActivity.this);
            pDialog.setMessage("Getting Patients...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", Integer.toString(userid));

            jsonParser.setParams(query);
            JSONObject json = jsonParser.makeHttpRequest(grab_patient_url, "GET");

            try {
                if(json.has("result")){
                    array = json.getJSONArray("result");
                }

                /*if(!json.has("error")){
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    i.putExtra("username", username);
                    i.putExtra("id", json.getInt("id"));
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }
}
