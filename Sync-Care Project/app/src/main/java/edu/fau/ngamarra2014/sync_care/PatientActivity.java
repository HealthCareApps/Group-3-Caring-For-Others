package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class PatientActivity extends AppCompatActivity {

    User user = User.getInstance();

    TextView diagnosis, dob, name;
    ImageButton profile, doctor, meds, insurance, pharmacy;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //new GrabPatientsInfo().execute();

        name = (TextView) findViewById(R.id.pname);
        diagnosis = (TextView) findViewById(R.id.pdiganosis);
        dob = (TextView) findViewById(R.id.dob);

        name.setText(user.patient.getName());
        diagnosis.setText("Primary Diagnosis: " + user.patient.getDiagnosis());
        dob.setText("Date of Birth: " + user.patient.getDOB());

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

    /*class GrabPatientsInfo extends AsyncTask<String, String, String> {
        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String prescriptions_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientPrescriptions.php";
        private String doctors_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientDoctors.php";
        private String insurances_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientInsurances.php";
        private String pharmacies_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientPharmacies.php";
        private String info = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/patientInfo.php";

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

            JSONArray arr = jsonParser.makeHttpRequest(info, "GET");

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

    }*/
}
