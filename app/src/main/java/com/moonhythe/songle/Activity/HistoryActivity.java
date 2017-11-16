package com.moonhythe.songle.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Badge;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = HistoryActivity.class.getSimpleName();
    private List<Badge> badges;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        badges = parseBadges();

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeAdapter();
    }

    public List<Badge> parseBadges(){
        List<Badge> badges_list = new ArrayList<Badge>();
        return badges_list;
    }

    private void initializeAdapter(){
        Log.i(TAG, "Sending " + badges.size() + " badges");
        RVAdapter adapter = new RVAdapter(badges);
        rv.setAdapter(adapter);
    }


}
