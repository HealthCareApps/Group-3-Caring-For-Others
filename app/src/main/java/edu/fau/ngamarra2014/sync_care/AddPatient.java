package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddPatient extends AppCompatActivity {

    Globals globals = Globals.getInstance();

    EditText first, last, dateofbirth, number, emergency;
    RadioGroup radio;
    Button add;
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
        radio = (RadioGroup) findViewById(R.id.radioGroup);

        add = (Button) findViewById(R.id.addPatient);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreatePatient().execute();
            }
        });

    }

    class CreatePatient extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String add_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/addPatient.php";

        String fname, lname, birth, phoneNumber, emergencyNumber, gender;
        RadioButton rd = (RadioButton) findViewById(radio.getCheckedRadioButtonId());

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

            gender = rd.getText().toString();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("caretaker", userid);
            query.add("first", fname);
            query.add("last", lname);
            query.add("birth", birth);
            query.add("phone", phoneNumber);
            query.add("emergancy", emergencyNumber);
            query.add("gender", gender);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(add_patient_url,
                    "POST");

            // check for success tag
            try {
                int success = json.getInt(0);

                if (success == 1) {
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
