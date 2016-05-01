package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

public class AccountActivity extends NavigationActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.account_activity, null, false);
        drawer.addView(contentView, 0);

        getSupportActionBar().setTitle("");

        RelativeLayout patient = (RelativeLayout) findViewById(R.id.deletepatient);
        RelativeLayout medical = (RelativeLayout) findViewById(R.id.deletemedical);
        RelativeLayout profile = (RelativeLayout) findViewById(R.id.manageprofile);
        RelativeLayout link = (RelativeLayout) findViewById(R.id.linkrequests);

        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeletePatientActivity.class));
            }
        });

        medical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DeleteMedicalActivity.class));
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageProfileActivity.class));
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LinkedRequestsActivity.class));
            }
        });

    }
}