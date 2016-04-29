package edu.fau.ngamarra2014.sync_care.Adapters;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.Database.DBHandler;
import edu.fau.ngamarra2014.sync_care.Database.JSONParser;
import edu.fau.ngamarra2014.sync_care.Database.QueryString;
import edu.fau.ngamarra2014.sync_care.ExcerciseListActivity;
import edu.fau.ngamarra2014.sync_care.R;

public class ExcerciseRecyclerAdapter extends RecyclerView.Adapter<ExcerciseRecyclerAdapter.ViewHolder> {

    User user = User.getInstance();

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> start = new ArrayList<String>();
    private ArrayList<String> duration = new ArrayList<String>();
    private ArrayList<String> calories = new ArrayList<String>();
    private ArrayList<String> comments = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();

    private int[] images = { R.drawable.exercise};
    ExcerciseListActivity excercise;

    private int id;

    public ExcerciseRecyclerAdapter(ExcerciseListActivity excercise){
        this.excercise = excercise;

        for(int i = 0; i < user.patient.getNumberOfDoctors(); i++){
            name.add(user.patient.getDoctor(i).getName());
            start.add(user.patient.getDoctor(i).getType());
            duration.add(user.patient.getDoctor(i).getContactInfo()[0]);
            calories.add(user.patient.getDoctor(i).getContactInfo()[2]);
            comments.add(user.patient.getDoctor(i).getAddress()[0]);
            date.add(user.patient.getDoctor(i).getAddress()[1]);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.card_excercise_layout, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.itemName.setText(name.get(i));
        viewHolder.itemStartTime.setText(start.get(i));
        viewHolder.itemDuration.setText(duration.get(i));
        viewHolder.itemCaloriesBurned.setText(calories.get(i));
        viewHolder.itemComments.setText(comments.get(i));
        viewHolder.itemDate.setText(date.get(i));
        viewHolder.itemImage.setImageResource(images[0]);
    }
    @Override
    public int getItemCount() {
        return name.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView itemImage;
        public TextView itemName;
        public TextView itemStartTime;
        public TextView itemDuration;
        public TextView itemCaloriesBurned;
        public TextView itemComments;
        public TextView itemDate;

        public ViewHolder(View itemView) {
            super(itemView);

            itemImage =
                    (ImageView) itemView.findViewById(R.id.item_image);
            itemName =
                    (TextView) itemView.findViewById(R.id.item_name);
            itemStartTime =
                    (TextView) itemView.findViewById(R.id.start);
            itemDuration =
                    (TextView) itemView.findViewById(R.id.duration);
            itemCaloriesBurned =
                    (TextView) itemView.findViewById(R.id.calories);
            itemComments =
                    (TextView) itemView.findViewById(R.id.comments);
            itemDate =
                    (TextView) itemView.findViewById(R.id.weekday);
        }
    }
    /*class DeleteDoc extends AsyncTask<String, String, String> {

        JSONParser jsonParser = new JSONParser();
        private String delete_url = "http://lamp.cse.fau.edu/~ngamarra2014/Sync-Care2/PHP/Functions/deleteDoc.php";
        DBHandler dbHandler = new DBHandler(Doc, null, null, 2);
        int index;

        public DeleteDoc(int index){
            this.index = index;
        }
        protected String doInBackground(String... args) {

            // Building Parameters
            QueryString query = new QueryString("id", Integer.toString(id));
            query.add("database", "Doctors");

            jsonParser.setParams(query);
            JSONObject response = jsonParser.makeHttpRequest(delete_url, "POST");

            try {
                if (response.has("Successful")) {
                    dbHandler.deleteDoc("doctors", id);
                    user.patient.removeDoctor(index);
                    Doc.onFinishCallback();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }*/


}
