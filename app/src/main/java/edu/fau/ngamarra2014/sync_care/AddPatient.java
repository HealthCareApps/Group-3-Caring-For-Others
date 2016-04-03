package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddPatient extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    EditText first, last, dateofbirth, number, emergency, dia;
    RadioGroup radio;
    Button add;
    static int valid = 0;
    String userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_add_activity);

        try {
            userid = globals.getuser().getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        first = (EditText) findViewById(R.id.first);
        last = (EditText) findViewById(R.id.last);
        dateofbirth = (EditText) findViewById(R.id.birthdate);
        number = (EditText) findViewById(R.id.phonenum);
        emergency = (EditText) findViewById(R.id.emernum);
        dia = (EditText) findViewById(R.id.diagnosis);
        radio = (RadioGroup) findViewById(R.id.radioGroup);

        add = (Button) findViewById(R.id.addPatient);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valid = 0;
                FormValidation((ViewGroup) findViewById(R.id.addPatientContent));
                if(valid == 0){
                    new CreatePatient().execute();
                }
            }
        });

    }

    private void FormValidation(ViewGroup group)
    {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if(view.getId() == first.getId() || view.getId() == last.getId()
                        || view.getId() == dateofbirth.getId()
                        || view.getId() == dia.getId()){

                    if (((EditText)view).getText().toString().length() == 0){
                        ((EditText) view).setError("Required!");
                        valid++;
                    }
                }

            }
            if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
                FormValidation((ViewGroup)view);
        }
    }

    class CreatePatient extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String add_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/addPatient.php";

        String fname, lname, birth, phoneNumber, emergencyNumber, gender, diagnosis;
        RadioButton rd = (RadioButton) findViewById(radio.getCheckedRadioButtonId());
        JSONObject patient = new JSONObject();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPatient.this);
            pDialog.setMessage("Adding Patient...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setCanceledOnTouchOutside(false);
            pDialog.show();

            fname = first.getText().toString();
            lname = last.getText().toString();
            birth = dateofbirth.getText().toString();
            phoneNumber = number.getText().toString();
            emergencyNumber = emergency.getText().toString();
            diagnosis = dia.getText().toString();
            gender = rd.getText().toString();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("caretaker", userid);
            query.add("first", fname);
            query.add("last", lname);
            query.add("birth", birth);
            query.add("phone", phoneNumber);
            query.add("emergency", emergencyNumber);
            query.add("gender", gender);
            query.add("diagnosis", diagnosis);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(add_patient_url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    patient.put("first", fname);
                    patient.put("last", lname);
                    patient.put("birthdate", birth);
                    patient.put("phone", phoneNumber);
                    patient.put("emergency", emergencyNumber);
                    patient.put("gender", gender);
                    patient.put("primary_diagnosis", diagnosis);
                    patient.put("name", fname + " " + lname);
                    globals.addPatient(patient);
                    Intent i = new Intent(getApplicationContext(), PatientListActivity.class);
                    startActivity(i);
                    finish();
                } else {
                    // failed
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
        }

    }

}
