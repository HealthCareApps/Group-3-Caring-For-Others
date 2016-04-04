package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientActivity extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    TextView diagnosis, dob, name;
    ImageButton profile, doctor, meds, insurance, pharmacy;
    String patientid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        new GrabPatientsInfo().execute();

        name = (TextView) findViewById(R.id.pname);
        diagnosis = (TextView) findViewById(R.id.pdiganosis);
        dob = (TextView) findViewById(R.id.dob);

        try{
            patientid = globals.getCurrentPatient().getString("id");
            name.setText(globals.getCurrentPatient().getString("name"));
            diagnosis.setText("Primary Diagnosis: " + globals.getCurrentPatient().getString("primary_diagnosis"));
            dob.setText("Date of Birth: " + globals.getCurrentPatient().getString("birthdate"));

        }catch(JSONException e){
            e.printStackTrace();
        }

        doctor = (ImageButton) findViewById(R.id.doctor);
        meds = (ImageButton) findViewById(R.id.meds);
        profile = (ImageButton) findViewById(R.id.profile);
        insurance = (ImageButton) findViewById(R.id.insurance);
        pharmacy = (ImageButton) findViewById(R.id.pharmacy);

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DoctorListActivity.class));
            }
        });

        meds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startActivity(new Intent(getApplicationContext(), RxListActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PatientProfileActivity.class));
            }
        });

        insurance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), InsuranceListActivity.class));
            }
        });

        pharmacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PharmacyListActivity.class));
            }
        });
    }

    class GrabPatientsInfo extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String prescriptions_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientPrescriptions.php";
        private String doctors_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientDoctors.php";
        private String insurances_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientInsurances.php";
        private String pharmacies_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientPharmacies.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(PatientActivity.this);
            pDialog.setMessage("Loading Patient information...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", patientid);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(prescriptions_url, "GET");
            globals.setPatientPrescriptions(json);
            json = jsonParser.makeHttpRequest(doctors_url, "GET");
            globals.setPatientDoctors(json);
            json = jsonParser.makeHttpRequest(insurances_url, "GET");
            globals.setPatientInsurances(json);
            json = jsonParser.makeHttpRequest(pharmacies_url, "GET");
            globals.setPatientPharmacies(json);

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }
}
