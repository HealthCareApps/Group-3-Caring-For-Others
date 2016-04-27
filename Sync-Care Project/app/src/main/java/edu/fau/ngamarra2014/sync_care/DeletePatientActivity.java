package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Adapters.DeletePatientsAdapter;
import edu.fau.ngamarra2014.sync_care.Data.User;


public class DeletePatientActivity extends Activity {

    User user = User.getInstance();
    ListView list;
    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> details = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_patient);

        for(int i = 0; i < user.getNumberOfPatients(); i++){
            titles.add(user.getPatient(i).getName());
            //details.add("DOB: " + user.getPatient(i).getDOB());
        }


        DeletePatientsAdapter adapter = new DeletePatientsAdapter(titles, this);
        list = (ListView) findViewById(R.id.listviewpatient);
        list.setAdapter(adapter);


    }

}
