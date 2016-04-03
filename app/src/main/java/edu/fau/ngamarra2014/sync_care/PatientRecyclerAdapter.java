package edu.fau.ngamarra2014.sync_care;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.ArrayList;

public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.ViewHolder>{

    Globals globals = Globals.getInstance();

    private ArrayList<String> titles = new ArrayList<String>();
    private ArrayList<String> details = new ArrayList<String>();
    private int[] images = { R.drawable.mario_icon};

    public PatientRecyclerAdapter(){

        for(int i = 0; i < globals.getPatients().length(); i++){
            try{
                titles.add(globals.getPatients().getJSONObject(i).getString("name"));
                details.add("DOB: " + globals.getPatients().getJSONObject(i).getString("birthdate"));
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_patient_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemTitle.setText(titles.get(i));
        viewHolder.itemDetail.setText(details.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return titles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemTitle;
        public TextView itemDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemTitle =
                    (TextView) itemView.findViewById(R.id.item_title);
            itemDetail =
                    (TextView) itemView.findViewById(R.id.item_detail);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent();
                    i.setClass(v.getContext(), PatientActivity.class);

                    try {
                        globals.setCurrentPatient(globals.getPatients().getJSONObject(position));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    v.getContext().startActivity(i);
                }
            });
        }
    }
}

