package edu.fau.ngamarra2014.sync_care;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import edu.fau.ngamarra2014.sync_care.Adapters.DoctorRecyclerAdapter;
import edu.fau.ngamarra2014.sync_care.Add.Edit.DoctorEditActivity;
import edu.fau.ngamarra2014.sync_care.Data.User;

public class DoctorListActivity extends NavigationActivity {

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

        getSupportActionBar().setTitle("Doctors");

        recyclerView =
                (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new DoctorRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        if(user.getAccountType().equals("Caretaker")){
            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getApplicationContext(), DoctorEditActivity.class));
                    finish();
                }
            });
        }
    }
    public void onFinishCallback()
    {
        finish();
        startActivity(getIntent());
    }
    protected void onRestart(){
        super.onRestart();
        user.patient.doctor = null;
        finish();
        startActivity(getIntent());
    }
}
