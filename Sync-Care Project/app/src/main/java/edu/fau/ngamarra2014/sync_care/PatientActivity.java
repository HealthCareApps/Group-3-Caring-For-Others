package edu.fau.ngamarra2014.sync_care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import edu.fau.ngamarra2014.sync_care.Data.User;

public class PatientActivity extends AppCompatActivity {

    User user = User.getInstance();

    TextView diagnosis, dob, name;
    ImageButton profile, doctor, meds, insurance, pharmacy, excercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //new GrabPatientsInfo().execute();

        name = (TextView) findViewById(R.id.pname);
        dob = (TextView) findViewById(R.id.dob);

        name.setText(user.patient.getName());
        dob.setText("Date of Birth: " + user.patient.getDOB());

        doctor = (ImageButton) findViewById(R.id.doctor);
        meds = (ImageButton) findViewById(R.id.meds);
        profile = (ImageButton) findViewById(R.id.profile);
        insurance = (ImageButton) findViewById(R.id.insurance);
        pharmacy = (ImageButton) findViewById(R.id.pharmacy);
        excercises = (ImageButton) findViewById(R.id.excercises);


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
                startActivity(new Intent(getApplicationContext(), ExcerciseActivity.class));
            }
        });
    }
}
