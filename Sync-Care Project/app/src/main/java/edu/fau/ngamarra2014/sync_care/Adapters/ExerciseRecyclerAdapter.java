package edu.fau.ngamarra2014.sync_care.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import edu.fau.ngamarra2014.sync_care.Data.User;
import edu.fau.ngamarra2014.sync_care.R;

public class ExerciseRecyclerAdapter extends RecyclerView.Adapter<ExerciseRecyclerAdapter.ViewHolder> {

    User user = User.getInstance();

    private ArrayList<String> name = new ArrayList<String>();
    private ArrayList<String> start = new ArrayList<String>();
    private ArrayList<String> duration = new ArrayList<String>();
    private ArrayList<String> calories = new ArrayList<String>();
    private ArrayList<String> comments = new ArrayList<String>();
    private ArrayList<String> date = new ArrayList<String>();

    private int[] images = { R.drawable.exercise};

    public ExerciseRecyclerAdapter(){

        for(int i = 0; i < user.patient.getNumberOfExercises(); i++){
            name.add(user.patient.getExercise(i).getName());
            start.add(user.patient.getExercise(i).getStart());
            duration.add(user.patient.getExercise(i).getDuration());
            calories.add(user.patient.getExercise(i).getCalories());
            comments.add(user.patient.getExercise(i).getComments());
            date.add(user.patient.getExercise(i).getDate());
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
}
