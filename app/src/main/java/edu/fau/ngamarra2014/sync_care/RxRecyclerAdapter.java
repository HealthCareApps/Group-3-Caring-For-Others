package edu.fau.ngamarra2014.sync_care;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Nick on 3/30/2016.
 */
public class RxRecyclerAdapter extends RecyclerView.Adapter<RxRecyclerAdapter.ViewHolder>{
    private ArrayList<String> names = new ArrayList<String>();
    private ArrayList<String> doctors = new ArrayList<String>();
    private ArrayList<String> dosages = new ArrayList<String>();
    private ArrayList<String> instructions = new ArrayList<String>();
    private ArrayList<String> symptoms = new ArrayList<String>();
    private int[] images = { R.drawable.rx_bottle_icon};
    private JSONArray rxArray;

    JSONParser jsonParser = new JSONParser();
    private static String rx_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/connect/rx.php";

    public RxRecyclerAdapter(JSONArray meds){

        rxArray = meds;

        for(int i = 0; i < rxArray.length(); i++){
            try{
                names.add(rxArray.getJSONObject(i).getString("name"));
                doctors.add(rxArray.getJSONObject(i).getString("doctor"));
                dosages.add(rxArray.getJSONObject(i).getString("dosage"));
                instructions.add(rxArray.getJSONObject(i).getString("instructions"));
                symptoms.add(rxArray.getJSONObject(i).getString("symptoms"));
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
        }
    }
}
