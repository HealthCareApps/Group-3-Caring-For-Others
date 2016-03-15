package edu.fau.ngamarra2014.sync_care;

import android.app.Activity;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import org.json.JSONObject;

import java.util.ArrayList;

public class RxListFragment extends ListFragment implements OnItemClickListener{

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String rx_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/rx.php";

    RxListListener activityCallback;
    String name;
    ArrayList<String> itemsList = new ArrayList<String>();

    public interface RxListListener {
        public void onRxClick(String name);
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            activityCallback = (RxListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RxListListener");
        }
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        try {
            activityCallback = (RxListListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement RxListListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this rx_list
        View view = inflater.inflate(R.layout.rx_list, container, false);

        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Prescriptions().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        name = (String)parent.getItemAtPosition(position);
        buttonClicked(view);
    }

    public void buttonClicked (View view) {
        activityCallback.onRxClick(name);
    }

    class Prescriptions extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Prescriptions..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", "1007");

            jsonParser.setParams(query);
            JSONObject json = jsonParser.makeHttpRequest(rx_url, "GET");

            // check for success tag
            try {
                if(!json.has("error")){
                    itemsList.add(json.getString("name"));
                } else {
                    // failed to create product
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

            ArrayAdapter<String> myArray = new ArrayAdapter<String> (getActivity(), android.R.layout.simple_list_item_1, itemsList);
            setListAdapter(myArray);
            //getListView().setOnItemClickListener(this);
            // dismiss the dialog once done
            pDialog.dismiss();

        }

    }
}
