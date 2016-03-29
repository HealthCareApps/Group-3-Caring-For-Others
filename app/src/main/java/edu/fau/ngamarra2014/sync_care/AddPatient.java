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

import org.json.JSONException;
import org.json.JSONObject;

public class AddPatient extends AppCompatActivity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();

    EditText first;
    EditText last;
    EditText dateofbirth;
    EditText number;
    EditText emergancy;
    RadioGroup radio;
    Button add;
    int id;

    private static String add_patient_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/addPatient.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_patient);

        id = getIntent().getIntExtra("id",0);

        first = (EditText) findViewById(R.id.first);
        last = (EditText) findViewById(R.id.last);
        dateofbirth = (EditText) findViewById(R.id.birthdate);
        number = (EditText) findViewById(R.id.phonenum);
        emergancy = (EditText) findViewById(R.id.emernum);
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

        String fname;
        String lname;
        String birth;
        String phoneNumber;
        String emergancyNumber;
        String gender;
        RadioButton rd = (RadioButton) findViewById(radio.getCheckedRadioButtonId());

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddPatient.this);
            pDialog.setMessage("Adding Patient...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            fname = first.getText().toString();
            lname = last.getText().toString();
            birth = dateofbirth.getText().toString();
            phoneNumber = number.getText().toString();
            emergancyNumber = emergancy.getText().toString();

            gender = rd.getText().toString();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("caretaker", Integer.toString(id));
            query.add("first", fname);
            query.add("last", lname);
            query.add("birth", birth);
            query.add("phone", phoneNumber);
            query.add("emergancy", emergancyNumber);
            query.add("gender", gender);

            // getting JSON Object
            // Note that create product url accepts POST method
            jsonParser.setParams(query);
            JSONObject json = jsonParser.makeHttpRequest(add_patient_url,
                    "POST");

            // check for success tag
            try {
                int success = json.getInt("success");

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

}
