package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

public class HomeActivity extends Activity{

    Button settings, rx;
    String username;
    int userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        settings = (Button) findViewById(R.id.settings);
        rx = (Button) findViewById(R.id.rxButton);

        TextView welcome = (TextView) findViewById(R.id.welcome);
        username = getIntent().getStringExtra("username");
        userid = getIntent().getIntExtra("id",0);
        welcome.setText("Welcome " + username);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), SettingsActivity.class);
                i.putExtra("settingsFor", username);
                startActivity(i);

            }
        });

        rx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //new Prescriptions().execute();
                Intent i = new Intent(getApplicationContext(), RxActivity.class);
                i.putExtra("id", userid); //Patients id for the prescriptions
                startActivity(i);
            }
        });

    }
}
