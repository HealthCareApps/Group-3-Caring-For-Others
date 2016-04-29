package edu.fau.ngamarra2014.sync_care;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Adapters.PatientRecyclerAdapter;
import edu.fau.ngamarra2014.sync_care.Data.Doctor;
import edu.fau.ngamarra2014.sync_care.Data.Insurance;
import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.Pharmacy;
import edu.fau.ngamarra2014.sync_care.Data.Prescription;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class PatientListActivity extends NavigationActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    DBHandler dbHandler = new DBHandler(this, null, null, 2);

    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.card_activity, null, false);
        drawer.addView(contentView, 0);

        if(user.getAccountType().equals("Specialist")){
            dbHandler.loadSpecialistPatients(user.getID());
            for(int i =0; i < user.getNumberOfPatients(); i++){
                user.getPatient(i).setDoctors(dbHandler.loadDoctors(user.getPatient(i).getID()));
                user.getPatient(i).setPrescriptions(dbHandler.loadPrescriptions(user.getPatient(i).getID()));
                user.getPatient(i).setPharmacies(dbHandler.loadPharmacies(user.getPatient(i).getID()));
                user.getPatient(i).setInsurances(dbHandler.loadInsurances(user.getPatient(i).getID()));
            }
            String lastEntry = dbHandler.lastPatientAdded(user.getID());
            new checkfornewpatients().execute(lastEntry);
        }

        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PatientRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getAccountType().equals("Caretaker")){
                    startActivity(new Intent(getApplicationContext(), AddPatientActivity.class));
                    finish();
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setTitle("Enter patient ID");

                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);

                    alertDialogBuilder.setView(input);

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(getCurrentFocus()!=null) {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            }
                            new sync().execute(input.getText().toString());
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    class sync extends AsyncTask<String, String, String> {

        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/sync.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;

        protected String doInBackground(String... args) {

            // Building Parameters for php
            QueryString query = new QueryString("patient", args[0]);
            query.add("id", Integer.toString(user.getID()));

            jsonParser.setParams(query);
            response = jsonParser.makeHttpRequest(url, "GET");

            try{
                if(response.has("Patients")){
                    for(int i = 0; i < response.getJSONArray("Patients").length(); i++){
                        dbHandler.addPatient(new Patient(response.getJSONArray("Patients").getJSONObject(i)));
                    }
                }
            }catch(JSONException e){
                e.printStackTrace();
            }

            return null;
        }
        protected void onPostExecute(String file_url){
            super.onPostExecute(file_url);
            if(response.has("Successful")){
                Toast toast = Toast.makeText(PatientListActivity.this, "Awaiting Confirmation", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    class checkfornewpatients extends AsyncTask<String, String, String> {

        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/checklinkedrequests.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;

        protected String doInBackground(String... args) {

            QueryString query = new QueryString("id", Integer.toString(user.getID()));
            query.add("last", args[0]);

            jsonParser.setParams(query);
            response = jsonParser.makeHttpRequest(url, "GET");

            try {
                if(response.has("Patients")){
                    JSONArray patients = response.getJSONArray("Patients");
                    for(int i = 0; i < patients.length(); i++){
                        Patient patient = new Patient(patients.getJSONObject(i));
                        user.addPatient(patient);
                        dbHandler.addSpecialistPatient(patients.getJSONObject(i).getInt("special_id"), patient);

                        jsonParser.setParams(new QueryString("id", Integer.toString(patient.getID())));
                        response = jsonParser.makeHttpRequest("http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/patientInfo.php", "GET");

                        JSONArray doctors = response.getJSONArray("doctors");
                        JSONArray insurances = response.getJSONArray("insurances");
                        JSONArray pharmacies = response.getJSONArray("pharmacies");
                        JSONArray prescriptions = response.getJSONArray("prescriptions");

                        for(int x = 0; x < doctors.length(); x++){
                            JSONObject doctor = doctors.getJSONObject(x);
                            Doctor doc = patient.addDoctor(doctor);
                            dbHandler.addDoctor(doc);
                        }
                        for(int y = 0; y < insurances.length(); y++){
                            JSONObject insurance = insurances.getJSONObject(y);
                            Insurance insur = patient.addInsurance(insurance);
                            dbHandler.addInsurance(insur);
                        }
                        for(int z = 0; z < pharmacies.length(); z++){
                            JSONObject pharmacy = pharmacies.getJSONObject(z);
                            Pharmacy pharm = patient.addPharmacy(pharmacy);
                            dbHandler.addPharmacy(pharm);
                        }
                        for(int t = 0; t < prescriptions.length(); t++){
                            JSONObject prescription = prescriptions.getJSONObject(t);
                            Prescription rx = patient.addPrescription(prescription);
                            dbHandler.addPrescription(rx);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
        protected void onPostExecute(String file_url){
            super.onPostExecute(file_url);
            /*if(response.has("Successful")){
                Toast toast = Toast.makeText(PatientListActivity.this, "Awaiting Confirmation", Toast.LENGTH_SHORT);
                toast.show();
            }*/
        }
    }
}
