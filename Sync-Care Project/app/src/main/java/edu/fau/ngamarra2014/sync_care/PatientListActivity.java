package edu.fau.ngamarra2014.sync_care;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import edu.fau.ngamarra2014.sync_care.Adapters.PatientRecyclerAdapter;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class PatientListActivity extends NavigationActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;

    User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.card_activity, null, false);
        drawer.addView(contentView, 0);

        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PatientRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getAccountType().equals("Caretaker")){
                    startActivity(new Intent(getApplicationContext(), AddPatientActivity.class));
                    finish();
                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                    alertDialogBuilder.setTitle("Enter patient ID");

                    final EditText input = new EditText(view.getContext());
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);

                    alertDialogBuilder.setView(input);

                    alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(getCurrentFocus()!=null) {
                                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            }
                            new sync(input.getText().toString()).execute();
                        }
                    });
                    alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }
    class sync extends AsyncTask<String, String, String> {

        private String url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/sync.php";
        JSONParser jsonParser = new JSONParser();
        JSONObject response;
        String id;

        public sync(String id){
            this.id = id;
        }
        protected String doInBackground(String... args) {

            // Building Parameters for php
            QueryString query = new QueryString("patient", id);
            query.add("id", Integer.toString(user.getID()));

            jsonParser.setParams(query);
            response = jsonParser.makeHttpRequest(url, "GET");

            return null;
        }
        protected void onPostExecute(String file_url){
            super.onPostExecute(file_url);
            if(response.has("Successful")){
                Toast toast = Toast.makeText(PatientListActivity.this, "Awaiting Confirmation", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
