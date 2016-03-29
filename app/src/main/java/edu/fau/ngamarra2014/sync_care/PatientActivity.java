package edu.fau.ngamarra2014.sync_care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class PatientActivity extends AppCompatActivity {

    JSONObject patientInfo;
    TextView diagnosis, dob;
    ImageButton profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        try{
            patientInfo = new JSONObject(getIntent().getStringExtra("patient"));
            setTitle(patientInfo.getString("first") + " " + patientInfo.getString("last"));
            diagnosis = (TextView) findViewById(R.id.pdiganosis);
            dob = (TextView) findViewById(R.id.dob);

            diagnosis.setText("Primary Diagnosis: " + patientInfo.getString("primary_diagnosis"));
            dob.setText("Date of Birth: " + patientInfo.getString("birthdate"));

        }catch(JSONException e){

        }

        ImageButton healthcare;
        healthcare = (ImageButton) findViewById(R.id.healthcare);
        profile = (ImageButton) findViewById(R.id.profile);

        healthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Healthcare.class);
                startActivity(i);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), PatientProfileActivity.class);
                i.putExtra("patient", patientInfo.toString());
                startActivity(i);
            }
        });
    }
}
