package edu.fau.ngamarra2014.sync_care;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Data.Prescription;
import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;

public class RxRecyclerAdapter extends RecyclerView.Adapter<RxRecyclerAdapter.ViewHolder>{

    User user = User.getInstance();

    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> doctors = new ArrayList<String>();
    private ArrayList<String> dosages = new ArrayList<String>();
    private ArrayList<String> instructions = new ArrayList<String>();
    private ArrayList<String> symptoms = new ArrayList<String>();
    private int[] images = { R.drawable.rx_bottle_icon};
    RxListActivity RX;
    int id;

    public RxRecyclerAdapter(RxListActivity prescription){

        RX = prescription;

        for(int i = 0; i < user.patient.getNumberOfPrescriptions(); i++){
            Prescription rx = user.patient.getPrescription(i);

            names.add(rx.getName());
            doctors.add(rx.getDoctorName());
            dosages.add(rx.getDosage());
            instructions.add(rx.getInstructions());
            symptoms.add(rx.getSymptoms());

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

            if(user.getAccountType().equals("Medical Specialist")){
                edit.setVisibility(View.INVISIBLE);
                delete.setVisibility(View.INVISIBLE);
            }

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user.patient.setCurrentPrescription(getAdapterPosition());
                    v.getContext().startActivity(new Intent(v.getContext(), RxEditActivity.class));
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    id = user.patient.getPrescription(getAdapterPosition()).getID();
                    new DeleteRx(getAdapterPosition()).execute();
                }
            });
        }
    }
    class DeleteRx extends AsyncTask<String, String, String> {

        private ProgressDialog pDialog;
        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/deleteDoc.php";
        DBHandler dbHandler = new DBHandler(RX, null, null, 2);
        int index;

        public DeleteRx(int index){
            this.index = index;
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", Integer.toString(id));
            query.add("database", "Prescriptions");

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                if (response.has("Successful")) {
                    dbHandler.deleteDoc("prescriptions", id);
                    user.patient.removePrescription(index);
                    RX.onFinishCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
