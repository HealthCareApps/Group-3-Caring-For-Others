package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.Exercise;
import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class PatientActivity extends NavigationActivity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, user.getUsername(), null, 2);
    TextView dob, name, id;
    ImageButton profile, doctor, meds, insurance, pharmacy, excercises, caretakers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.patient_activity, null, false);
        drawer.addView(contentView, 0);

        getSupportActionBar().hide();

        new check().execute(Integer.toString(user.patient.getID()));

        name = (TextView) findViewById(R.id.pname);
        dob = (TextView) findViewById(R.id.dob);
        id = (TextView) findViewById(R.id.id);

        name.setText(user.patient.getName());
        id.setText("Patient ID: " + user.getID());
        dob.setText("Date of Birth: " + user.patient.getDOB());

        doctor = (ImageButton) findViewById(R.id.doctor);
        meds = (ImageButton) findViewById(R.id.meds);
        profile = (ImageButton) findViewById(R.id.profile);
        insurance = (ImageButton) findViewById(R.id.insurance);
        pharmacy = (ImageButton) findViewById(R.id.pharmacy);
        excercises = (ImageButton) findViewById(R.id.exercises);
        caretakers = (ImageButton) findViewById(R.id.caretakers);

        if(user.getAccountType().equals("Specialist"))
            caretakers.setVisibility(View.INVISIBLE);


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
        excercises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ExerciseListActivity.class));
            }
        });
    }
    class check extends AsyncTask<String, String, String> {

        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/getExercises.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;

        protected String doInBackground(String... args) {

            // Building Parameters for php
            QueryString query = new QueryString("patient", args[0]);
            query.add("entry", dbHandler.lastExerciseAdded(Integer.parseInt(args[0])));

            jsonParser.setParams(query);

            try{
                response = jsonParser.makeHttpRequest(url, "GET");
                if(response.has("Exercises")){
                    for(int i = 0; i < response.getJSONArray("Exercises").length(); i++){
                        dbHandler.addExercise(user.patient.addExercise(response.getJSONArray("Exercises").getJSONObject(i)));
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url){
            super.onPostExecute(file_url);
            if(response.has("Internet")){
                Toast toast = Toast.makeText(PatientActivity.this, "Unable to upload new information, no Internet connection", Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }
}
