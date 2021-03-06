package edu.fau.ngamarra2014.sync_care.Add.Edit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Data.Patient;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.PatientListActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class AddPatientActivity extends AppCompatActivity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, user.getUsername(), null, 2);

    EditText first, last, dateofbirth, number, emergency;
    RadioGroup radio;
    Button add;
    static int valid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_add_activity);

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
                valid = 0;
                FormValidation((ViewGroup) findViewById(R.id.addPatientContent));
                if(valid == 0){
                    new CreatePatient().execute();
                }
            }
        });

    }

    private void FormValidation(ViewGroup group) {
        for (int i = 0, count = group.getChildCount(); i < count; ++i) {
            View view = group.getChildAt(i);
            if (view instanceof EditText) {
                if(view.getId() == first.getId() || view.getId() == last.getId()
                        || view.getId() == dateofbirth.getId()){
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
        JSONObject response;
        private String add_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/addDoc.php";

        String fname, lname, birth, phoneNumber, emergencyNumber, gender;
        RadioButton rd = (RadioButton) findViewById(radio.getCheckedRadioButtonId());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPatientActivity.this);
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
            QueryString query = new QueryString("database", "patients");
            query.add("Patient", Integer.toString(user.getID()));
            query.add("first", fname);
            query.add("last", lname);
            query.add("birth", birth);
            query.add("phone", phoneNumber);
            query.add("emergency", emergencyNumber);
            query.add("gender", gender);

            jsonParser.setParams(query);

            try {
                response = jsonParser.makeHttpRequest(add_patient_url, "POST");
                if (response.has("Successful")) {
                    Patient patient = new Patient(response.getInt("id"), fname, lname, gender, birth, user.getID());
                    patient.setPrimaryPhoneNumber(phoneNumber);
                    patient.setEmergencyPhoneNumber(emergencyNumber);
                    user.addPatient(patient);
                    dbHandler.addPatient(patient);

                    startActivity(new Intent(getApplicationContext(), PatientListActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {
            pDialog.dismiss();
            if(response.has("Internet")){
                Toast toast = Toast.makeText(AddPatientActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
                toast.show();
            }
        }

    }

}
