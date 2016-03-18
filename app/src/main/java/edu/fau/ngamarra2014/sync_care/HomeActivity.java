package edu.fau.ngamarra2014.sync_care;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity{

    Button settings, rx;
    String username;
    int userid;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        settings = (Button) findViewById(R.id.settings);
        rx = (Button) findViewById(R.id.rxButton);

        username = getIntent().getStringExtra("username");
        userid = getIntent().getIntExtra("id",0);

        setTitle("Welcome, " + username);

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
}
