package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;

import edu.fau.ngamarra2014.sync_care.Data.Prescription;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.OldCode.Globals;

public class RxEditActivity extends Activity {

    Globals globals = Globals.getInstance();
    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);
    private String url;

    EditText name, doctor, dosage, instructions, symptoms;
    int id = 0;
    Button save, add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rx_edit_activity);

        name = (EditText) findViewById(R.id.rx_name);
        doctor = (EditText) findViewById(R.id.rx_doctor);
        dosage = (EditText) findViewById(R.id.rx_dosage);
        instructions = (EditText) findViewById(R.id.rx_instructions);
        symptoms = (EditText) findViewById(R.id.rx_symptoms);
        save = (Button) findViewById(R.id.save);
        add = (Button) findViewById(R.id.add);

        if(user.patient.prescription != null) {
            id = user.patient.prescription.getID();
            name.setText(user.patient.prescription.getName());
            doctor.setText(user.patient.prescription.getDoctorName());
            dosage.setText(user.patient.prescription.getDosage());
            instructions.setText(user.patient.prescription.getInstructions());
            symptoms.setText(user.patient.prescription.getSymptoms());
            save.setVisibility(View.VISIBLE);
        }else add.setVisibility(View.VISIBLE);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/updateDoc.php";
                new UpdateRx().execute();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/addDoc.php";
                new UpdateRx().execute();
            }
        });
    }

    class UpdateRx extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();

        String rxName, rxDoc, rxDosage, rxInstructions, rxSymptoms;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            rxName = name.getText().toString();
            rxDoc = doctor.getText().toString();
            rxDosage = dosage.getText().toString();
            rxInstructions = instructions.getText().toString();
            rxSymptoms = symptoms.getText().toString();

        }

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("database", "Prescriptions");
            query.add("Patient", Integer.toString(user.patient.getID()));
            if(id != 0) query.add("id", Integer.toString(id));
            query.add("name", rxName);
            query.add("doc", rxDoc);
            query.add("dosage", rxDosage);
            query.add("instructions", rxInstructions);
            query.add("symptoms", rxSymptoms);

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    Prescription rx = new Prescription();
                    rx.setID(json.getInt(1));
                    rx.setName(rxName);
                    rx.setDoctorName(rxDoc);
                    rx.setDosage(rxDosage);
                    rx.setInstructions(rxInstructions);
                    rx.setSymptoms(rxSymptoms);
                    rx.setPatient(user.patient.getID());

                    if(json.getString(2).equals("Update")){
                        user.patient.prescription.update(rx);
                        dbHandler.updatePrescription(rx);
                    }else{
                        user.patient.addPrescription(rx);
                        dbHandler.addPrescription(rx);
                    }

                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(String file_url) {

        }

    }
}
