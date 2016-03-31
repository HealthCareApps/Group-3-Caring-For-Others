package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;

public class RxEditActivity extends Activity {

    Globals globals = Globals.getInstance();

    EditText name, doctor, dosage, instructions, symptoms;
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

    }
}
