package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import edu.fau.ngamarra2014.sync_care.Data.User;

public class PatientProfileActivity extends NavigationActivity {

    User user = User.getInstance();

    EditText firstname, lastname, DOB, primarynum, emergancynum, address, city, zipcode;
    ImageButton edit, back;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.patient_profile_activity, null, false);
        drawer.addView(contentView, 0);

        getSupportActionBar().hide();

        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        DOB = (EditText) findViewById(R.id.dob);
        primarynum = (EditText) findViewById(R.id.phonenum);
        emergancynum = (EditText) findViewById(R.id.emernum);
        address = (EditText) findViewById(R.id.address);
        city = (EditText) findViewById(R.id.city);
        zipcode = (EditText) findViewById(R.id.zipcode);
        edit = (ImageButton) findViewById(R.id.editButton);
        save = (Button) findViewById(R.id.save);
        back = (ImageButton) findViewById(R.id.previousButton);

        toggleTextFields(false);

        firstname.setText(user.patient.getFirst());
        lastname.setText(user.patient.getLast());
        DOB.setText(user.patient.getDOB());
        primarynum.setText(user.patient.getPrimaryPhoneNumber());
        emergancynum.setText(user.patient.getEmergencyPhoneNumber());

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleTextFields(true);
                save.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.patient.setName(firstname.getText().toString(), lastname.getText().toString());
                user.patient.setDOB(DOB.getText().toString());
                user.patient.setPrimaryPhoneNumber(primarynum.getText().toString());
                user.patient.setEmergencyPhoneNumber(emergancynum.getText().toString());

                save.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void toggleTextFields(Boolean set){
        firstname.setFocusable(set);
        firstname.setFocusableInTouchMode(set);
        firstname.setClickable(set);
        lastname.setFocusable(set);
        lastname.setFocusableInTouchMode(set);
        lastname.setClickable(set);
        DOB.setFocusable(set);
        DOB.setFocusableInTouchMode(set);
        DOB.setClickable(set);
        primarynum.setFocusable(set);
        primarynum.setFocusableInTouchMode(set);
        primarynum.setClickable(set);
        emergancynum.setFocusable(set);
        emergancynum.setFocusableInTouchMode(set);
        emergancynum.setClickable(set);
        address.setFocusable(set);
        address.setFocusableInTouchMode(set);
        address.setClickable(set);
        city.setFocusable(set);
        city.setFocusableInTouchMode(set);
        city.setClickable(set);
        zipcode.setFocusable(set);
        zipcode.setFocusableInTouchMode(set);
        zipcode.setClickable(set);
    }
}
