package edu.fau.ngamarra2014.sync_care.Authentication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.Doctor;
import edu.fau.ngamarra2014.sync_care.Data.Exercise;
import edu.fau.ngamarra2014.sync_care.Data.Insurance;
import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.Pharmacy;
import edu.fau.ngamarra2014.sync_care.Data.Prescription;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.HomeActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class LoginActivity extends AppCompatActivity {

    User user = User.getInstance();
    DBHandler login = new DBHandler(this, "USERS", null, 2);

    SharedPreferences credentials;

    EditText inputUsername, inputPassword;
    TextView create;
    Button signin;
    CheckBox rememberMe;

    String username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        credentials = getSharedPreferences("PREF_FILE", 0);

        //Text Fields
        inputUsername = (EditText) findViewById(R.id.username);
        inputPassword = (EditText) findViewById(R.id.password);
        rememberMe = (CheckBox) findViewById(R.id.remember);

        //Buttons
        create = (TextView) findViewById(R.id.signup);
        signin = (Button) findViewById(R.id.login);

        Boolean remember = credentials.getBoolean("RememberMe", false);
        if(remember){
            username = credentials.getString("Username", null);
            password = credentials.getString("Password", null);
            login();
        }

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Registration.class));
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = inputUsername.getText().toString();
                password = inputPassword.getText().toString();
                if(rememberMe.isChecked()){
                    SharedPreferences.Editor editor = credentials.edit();
                    editor.putBoolean("RememberMe", true);
                    editor.putString("Username", username);
                    editor.putString("Password", password);
                    editor.apply();
                }
                login();
            }
        });
    }
    public void login(){
        Log.i("Login", "login: " + username + " " + password);
        int response = login.AuthenticateUser(username, password);
        Log.i("Code", ""+response);
        DBHandler dbHandler = new DBHandler(LoginActivity.this, user.getUsername(), null, 2);

        if(response == 1){
            dbHandler.loadPatients(user.getID());

            for(int i =0; i < user.getNumberOfPatients(); i++){
                user.getPatient(i).setDoctors(dbHandler.loadDoctors(user.getPatient(i).getID()));
                user.getPatient(i).setPrescriptions(dbHandler.loadPrescriptions(user.getPatient(i).getID()));
                user.getPatient(i).setPharmacies(dbHandler.loadPharmacies(user.getPatient(i).getID()));
                user.getPatient(i).setInsurances(dbHandler.loadInsurances(user.getPatient(i).getID()));
                user.getPatient(i).setExercises(dbHandler.loadExercises(user.getPatient(i).getID()));
            }

            startActivity(new Intent(getApplicationContext(), HomeActivity.class));

        }else if(response == 2){
            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            Toast toast = Toast.makeText(LoginActivity.this, "Invalid credentials", Toast.LENGTH_SHORT);
            toast.show();
        }else if(response == 0){
            new Signin().execute();
        }
    }

    class Signin extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        JSONObject response;
        private String login_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Authentication/login.php";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Logging in..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters for php
            QueryString query = new QueryString("username", username);
            query.add("password", password);

            jsonParser.setParams(query);

            try {
                response = jsonParser.makeHttpRequest(login_url, "POST");
                if(response.has("User")){
                    user.setUser(response.getJSONObject("User"));
                    DBHandler dbHandler = new DBHandler(LoginActivity.this, user.getUsername(), null, 2);
                    login.addUser(user);

                    QueryString query2 = new QueryString("id", Integer.toString(user.getID()));
                    if(user.getAccountType().equals("Caretaker"))
                        query2.add("type", "Caretaker");
                    else
                        query2.add("type", "Specialist");

                    jsonParser.setParams(query2);

                    response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/getPatients.php", "GET");
                    JSONArray patients = response.getJSONArray("Patients");

                    for(int i = 0; i < patients.length(); i++){
                        Patient patient = user.addPatient(patients.getJSONObject(i));
                        if(user.getAccountType().equals("Caretaker"))
                            dbHandler.addPatient(patient);
                        else
                            dbHandler.addSpecialistPatient(patients.getJSONObject(i).getInt("special_id"), patient);

                        jsonParser.setParams(new QueryString("id", Integer.toString(patient.getID())));

                        response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/doctors.php", "GET");
                        JSONArray doctors = response.getJSONArray("doctors");
                        for(int x = 0; x < doctors.length(); x++){
                            JSONObject doctor = doctors.getJSONObject(x);
                            dbHandler.addDoctor(patient.addDoctor(doctor));
                        }
                        response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/insurances.php", "GET");
                        JSONArray insurances = response.getJSONArray("insurances");
                        for(int y = 0; y < insurances.length(); y++){
                            JSONObject insurance = insurances.getJSONObject(y);
                            dbHandler.addInsurance(patient.addInsurance(insurance));
                        }
                        response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/pharmacies.php", "GET");
                        JSONArray pharmacies = response.getJSONArray("pharmacies");
                        for(int z = 0; z < pharmacies.length(); z++){
                            JSONObject pharmacy = pharmacies.getJSONObject(z);
                            dbHandler.addPharmacy(patient.addPharmacy(pharmacy));
                        }
                        response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/prescriptions.php", "GET");
                        JSONArray prescriptions = response.getJSONArray("prescriptions");
                        for(int t = 0; t < prescriptions.length(); t++){
                            JSONObject prescription = prescriptions.getJSONObject(t);
                            dbHandler.addPrescription(patient.addPrescription(prescription));
                        }
                        response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/exercises.php", "GET");
                        JSONArray exercises = response.getJSONArray("exercises");
                        for(int t = 0; t < exercises.length(); t++){
                            JSONObject exercise = exercises.getJSONObject(t);
                            dbHandler.addExercise(patient.addExercise(exercise));
                        }
                    }
                    startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            if(response.has("Error")){
                Toast toast = Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT);
                toast.show();
            }else if(response.has("Internet")){
                Toast toast = Toast.makeText(LoginActivity.this, "No Internet Connection", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
