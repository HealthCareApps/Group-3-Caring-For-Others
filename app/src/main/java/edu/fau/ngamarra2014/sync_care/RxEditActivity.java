package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;

public class RxEditActivity extends Activity {

    Globals globals = Globals.getInstance();

    EditText name, doctor, dosage, instructions, symptoms;
    String id;
    Button save;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_edit_activity);

        name = (EditText) findViewById(R.id.rx_name);
        doctor = (EditText) findViewById(R.id.rx_doctor);
        dosage = (EditText) findViewById(R.id.rx_dosage);
        instructions = (EditText) findViewById(R.id.rx_instructions);
        symptoms = (EditText) findViewById(R.id.rx_symptoms);
        save = (Button) findViewById(R.id.save);

        try {
            id = globals.getCurrentPrescription().getString("id");
            name.setText(globals.getCurrentPrescription().getString("name"));
            doctor.setText(globals.getCurrentPrescription().getString("doctor"));
            dosage.setText(globals.getCurrentPrescription().getString("dosage"));
            instructions.setText(globals.getCurrentPrescription().getString("instructions"));
            symptoms.setText(globals.getCurrentPrescription().getString("symptoms"));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    globals.getCurrentPrescription().put("name", name.getText().toString());
                    globals.getCurrentPrescription().put("doctor", doctor.getText().toString());
                    globals.getCurrentPrescription().put("dosage", dosage.getText().toString());
                    globals.getCurrentPrescription().put("instructions", instructions.getText().toString());
                    globals.getCurrentPrescription().put("symptoms", symptoms.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                finish();
            }
        });

        class Signin extends AsyncTask<String, String, String> {

            private ProgressDialog pDialog;
            JSONParser jsonParser = new JSONParser();
            private String update_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/updatePrescription.php";

            String rxName, rxDoc, rxDosage, rxInstructions, rxSymptoms;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(RxEditActivity.this);
                pDialog.setMessage("Updating prescription...");
                pDialog.setIndeterminate(false);
                pDialog.setCancelable(false);
                pDialog.setCanceledOnTouchOutside(false);
                pDialog.show();

                rxName = name.getText().toString();
                rxDoc = doctor.getText().toString();
                rxDosage = dosage.getText().toString();
                rxInstructions = instructions.getText().toString();
                rxSymptoms = symptoms.getText().toString();

            }

            protected String doInBackground(String... args) {

                // Building Parameters
                QueryString query = new QueryString("id", id);
                query.add("name", rxName);
                query.add("doc", rxDoc);
                query.add("dosage", rxDosage);
                query.add("instructions", rxInstructions);
                query.add("symptoms", rxSymptoms);

                jsonParser.setParams(query);
                JSONArray json = jsonParser.makeHttpRequest(update_url, "POST");

                try {
                    if(!json.getString(0).equals("Invalid")){

                        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
                        globals.setUser(json.getJSONObject(0));
                        startActivity(i);

                        finish();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return null;
            }

            protected void onPostExecute(String file_url) {
                // dismiss the dialog once done
                pDialog.dismiss();
            }

        }
    }
}
