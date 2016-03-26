package edu.fau.ngamarra2014.sync_care;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PatientActivity extends AppCompatActivity {

    JSONObject patientInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            patientInfo = new JSONObject(getIntent().getStringExtra("patient"));
            setTitle(patientInfo.getString("first") + " " + patientInfo.getString("last"));

        }catch(JSONException e){

        }

        ImageButton healthcare;
        healthcare = (ImageButton) findViewById(R.id.healthcare);

        healthcare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Healthcare.class);
                startActivity(i);
            }
        });
    }
}
