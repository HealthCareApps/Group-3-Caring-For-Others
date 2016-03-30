package edu.fau.ngamarra2014.sync_care;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.z_settings);

        Button linked_requests;
        Button vumark;
        Button alerts;
        Button account;

        linked_requests = (Button) findViewById(R.id.linkID);
        vumark = (Button) findViewById(R.id.vumark);
        alerts = (Button) findViewById(R.id.alerts);
        account = (Button) findViewById(R.id.account);

        TextView settings = (TextView) findViewById(R.id.settingsFor);
        settings.append(getIntent().getStringExtra("settingsFor"));

        linked_requests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), LinkedRequestsActivity.class);
                startActivity(i);
            }
        });

        vumark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), VuMarkActivity.class);
                startActivity(i);
            }
        });

        alerts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AlertsActivity.class);
                startActivity(i);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(i);
            }
        });




    }
}
