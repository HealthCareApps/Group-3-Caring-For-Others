package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Adapters.DeletePatientsAdapter;
import edu.fau.ngamarra2014.sync_care.Data.User;


public class DeletePatientActivity extends NavigationActivity {

    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_delete_patient, null, false);
        drawer.addView(contentView, 0);

        ArrayList<String> titles = new ArrayList<String>();

        for(int i = 0; i < user.getNumberOfPatients(); i++){
            titles.add(user.getPatient(i).getName());
        }

        DeletePatientsAdapter adapter = new DeletePatientsAdapter(titles, this);
        ListView list = (ListView) findViewById(R.id.listviewpatient);
        list.setAdapter(adapter);


    }

}
