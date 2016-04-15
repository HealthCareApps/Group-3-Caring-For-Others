package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import java.util.ArrayList;

public class RxRecyclerAdapter extends RecyclerView.Adapter<RxRecyclerAdapter.ViewHolder>{

    Globals globals = Globals.getInstance();

    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> doctors = new ArrayList<String>();
    private ArrayList<String> dosages = new ArrayList<String>();
    private ArrayList<String> instructions = new ArrayList<String>();
    private ArrayList<String> symptoms = new ArrayList<String>();
    private int[] images = { R.drawable.rx_bottle_icon};
    RxListActivity RX;

    private String id;
    private int position;

    public RxRecyclerAdapter(RxListActivity rx){

        RX = rx;

        for(int i = 0; i < globals.getPatientPrescriptions().length(); i++){
            try{
                names.add(globals.getPatientPrescriptions().getJSONObject(i).getString("name"));
                doctors.add(globals.getPatientPrescriptions().getJSONObject(i).getString("doctor"));
                dosages.add(globals.getPatientPrescriptions().getJSONObject(i).getString("dosage"));
                instructions.add(globals.getPatientPrescriptions().getJSONObject(i).getString("instructions"));
                symptoms.add(globals.getPatientPrescriptions().getJSONObject(i).getString("symptoms"));
            }catch(JSONException e){

            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_rx_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(names.get(i));
        viewHolder.itemDoctor.setText(doctors.get(i));
        viewHolder.itemDosage.setText(dosages.get(i));
        viewHolder.itemInstructions.setText(instructions.get(i));
        viewHolder.itemSymptoms.setText(symptoms.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return names.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;
        public TextView itemDoctor;
        public TextView itemDosage;
        public TextView itemInstructions;
        public TextView itemSymptoms;
        public ImageButton edit, delete;

        public ViewHolder(View itemView) {
            super(itemView);


            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemName =
                    (TextView) itemView.findViewById(R.id.item_name);
            itemDoctor =
                    (TextView) itemView.findViewById(R.id.item_doctor);
            itemDosage =
                    (TextView) itemView.findViewById(R.id.item_dosage);
            itemInstructions =
                    (TextView) itemView.findViewById(R.id.item_instructions);
            itemSymptoms =
                    (TextView) itemView.findViewById(R.id.item_symptoms);
            edit =
                    (ImageButton) itemView.findViewById(R.id.item_edit);
            delete =
                    (ImageButton) itemView.findViewById(R.id.item_delete);

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    Intent i = new Intent();
                    i.setClass(v.getContext(), RxEditActivity.class);
                    try {
                        globals.setCurrentPrescription(globals.getPatientPrescriptions().getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    v.getContext().startActivity(i);
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position = getAdapterPosition();
                    try {
                        id = globals.getPatientPrescriptions().getJSONObject(position).getString("id");
                        new DeleteRx().execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
    class DeleteRx extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/deleteDoc.php";

        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", id);
            query.add("database", "Prescriptions");

            jsonParser.setParams(query);
            JSONArray json = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                int success = json.getInt(0);

                if (success == 1) {
                    globals.getPatientPrescriptions().remove(position);
                    RX.onFinishCallback();
                } else {
                    // failed
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
