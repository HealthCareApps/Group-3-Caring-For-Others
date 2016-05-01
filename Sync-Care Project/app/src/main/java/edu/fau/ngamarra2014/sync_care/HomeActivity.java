package edu.fau.ngamarra2014.sync_care;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Authentication.LoginActivity;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;


public class HomeActivity extends NavigationActivity{

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, user.getUsername(), null, 2);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean finish = getIntent().getBooleanExtra("finish", false);
        if (finish) {
            user.logout();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.home_activity, null, false);
        drawer.addView(contentView, 0);

        getSupportActionBar().setTitle("");

        Button info = (Button) findViewById(R.id.info);
        Button about = (Button) findViewById(R.id.aboutus);

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Information.class));
            }
        });
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("About us");

                final TextView input = new TextView(v.getContext());
                input.setText("Welcome to Sync-Care! The worlds first platform that enables synchronization of patients medical information with family, doctors, and caretakers.");

                alertDialogBuilder.setView(input);

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        if(user.getAccountType().equals("Specialist")){
            dbHandler.loadSpecialistPatients(user.getID());
            for(int i =0; i < user.getNumberOfPatients(); i++){
                user.getPatient(i).setDoctors(dbHandler.loadDoctors(user.getPatient(i).getID()));
                user.getPatient(i).setPrescriptions(dbHandler.loadPrescriptions(user.getPatient(i).getID()));
                user.getPatient(i).setPharmacies(dbHandler.loadPharmacies(user.getPatient(i).getID()));
                user.getPatient(i).setInsurances(dbHandler.loadInsurances(user.getPatient(i).getID()));
            }
            startActivity(new Intent(getApplicationContext(), PatientListActivity.class));
        }

        new checkSync().execute();

    }
    class checkSync extends AsyncTask<String, String, String>{

        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/checksync.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;

        protected String doInBackground(String... args) {

            jsonParser.setParams(new QueryString("id", Integer.toString(user.getID())));
            try {
                response = jsonParser.makeHttpRequest(url, "GET");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
        protected void onPostExecute(String url){
            super.onPostExecute(url);

            if(response.has("Successful")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Sync Care");
                alertDialogBuilder.setMessage("You have pending specialist");

                alertDialogBuilder.setPositiveButton("View", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(getApplicationContext(), LinkedRequestsActivity.class));
                    }
                });
                alertDialogBuilder.setNegativeButton("View Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        }
    }
    protected void onRestart(){
        super.onRestart();
        new checkSync().execute();
    }
    protected void onResume() {
        super.onResume();

        boolean finish = getIntent().getBooleanExtra("finish", false);
        Log.i("TAG", "onResume: " + finish);
        if (finish) {
            Log.i("TAG", "onResume: ");
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            finish();
        }
    }
}
