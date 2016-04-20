package edu.fau.ngamarra2014.sync_care.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.HomeActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class LoginActivity extends AppCompatActivity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);

    EditText inputUsername;
    EditText inputPassword;
    TextView create;
    Button signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        findViewById(R.id.background).getBackground().setAlpha(222);

        //Text Fields
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);

        //Buttons
        create = (TextView) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.login);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean response = dbHandler.findUser(inputUsername.getText().toString(),inputPassword.getText().toString());
                if(response){
                    dbHandler.loadPatients(user.getID());
                    for(int i =0; i < user.getNumberOfPatients(); i++){
                        user.getPatient(i).setDoctors(dbHandler.loadDoctors(user.getPatient(i).getID()));
                        user.getPatient(i).setPrescriptions(dbHandler.loadPrescriptions(user.getPatient(i).getID()));
                        user.getPatient(i).setPharmacies(dbHandler.loadPharmacies(user.getPatient(i).getID()));
                        user.getPatient(i).setInsurances(dbHandler.loadInsurances(user.getPatient(i).getID()));
                    }
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);
                }else{
                    new Signin(v).execute();
                }
            }
        });
    }

    class Signin extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String login_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Authentication/login.php";

        private View view;
        String username;
        String password;

        public Signin(View v){
            this.view = v;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            //Getting user credentials
            username = inputUsername.getText().toString();
            password = inputPassword.getText().toString();
        }

        protected String doInBackground(String... args) {

            // Building Parameters for php
            QueryString query = new QueryString("username", username);
            query.add("password", password);
            MCrypt mcrypt = new MCrypt();


            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(login_url, "POST");

            try {
                if(response.has("User")){
                    user.setID(response.getJSONObject("User").getInt("id"));
                    user.setFirst(response.getJSONObject("User").getString("first"));
                    user.setLast(response.getJSONObject("User").getString("last"));
                    user.setEmail(response.getJSONObject("User").getString("email"));
                    user.setUsername(response.getJSONObject("User").getString("username"));
                    user.setPassword(response.getJSONObject("User").getString("password"));
                    dbHandler.addUser(user);
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                } else {
                    Snackbar.make(view, "Invalid credentials", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) { pDialog.dismiss();}
    }
}
