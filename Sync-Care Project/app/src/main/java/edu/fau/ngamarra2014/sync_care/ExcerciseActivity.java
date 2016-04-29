package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Locale;

import edu.fau.ngamarra2014.sync_care.Data.Excercise;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class ExcerciseActivity extends Activity {

    User user = User.getInstance();
    DBHandler dbHandler = new DBHandler(this, null, null, 2);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_activity);

        final EditText name = (EditText) findViewById(R.id.excercisename);
        final EditText start_time = (EditText) findViewById(R.id.starttime);
        final EditText duration = (EditText) findViewById(R.id.timelength);
        final EditText calories = (EditText) findViewById(R.id.caloriesburned);
        final EditText comments = (EditText) findViewById(R.id.comments);

        final Calendar calendar = Calendar.getInstance();

        Button save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addDoc().execute(name.getText().toString(), start_time.getText().toString(), duration.getText().toString(),
                        calories.getText().toString(), comments.getText().toString(),
                        calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.ENGLISH));
            }
        });
    }

    class addDoc extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/addDoc.php";

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("database", "excercise");
            query.add("id", Integer.toString(user.patient.getID()));
            query.add("patient", Integer.toString(user.patient.getID()));
            query.add("name", args[0]);
            query.add("start", args[1]);
            query.add("duration", args[2]);
            query.add("calories", args[3]);
            query.add("comments", args[4]);
            query.add("date", args[5]);

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(url, "POST");

            try {
                if (response.has("Successful")) {
                    Excercise excercise = new Excercise();
                    excercise.setID(response.getInt("id"));
                    excercise.setName(args[0]);
                    excercise.setStart(args[1]);
                    excercise.setDuration(args[2]);
                    excercise.setCalories(args[3]);
                    excercise.setComments(args[4]);
                    excercise.setDate(args[5]);
                    excercise.setPatient(user.patient.getID());
                    excercise.setSpecialist(user.getID());
                    dbHandler.addExcercise(excercise);

                    finish();
                }
                else{
                    Log.i("Error", response.toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
