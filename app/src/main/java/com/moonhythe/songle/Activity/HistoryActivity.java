package com.moonhythe.songle.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Badge;
import com.moonhythe.songle.Structure.Preference;
import com.moonhythe.songle.Structure.RVAdapter;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private static final String TAG = HistoryActivity.class.getSimpleName();
    private List<Badge> badges;
    private RecyclerView rv;
    private TextView amount_gold, amount_silver, amount_bronze;
    private int gold_counter = 0, silver_counter = 0, bronze_counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        badges = Preference.getBadges(this);

        for(Badge badge : badges){
            switch(badge.getBadge()){
                case "Gold":
                    gold_counter++;
                    break;
                case "Silver":
                    silver_counter++;
                    break;
                case "Bronze":
                    bronze_counter++;
                    break;
            }
        }

        amount_gold = (TextView)findViewById(R.id.amount_gold);
        amount_silver = (TextView)findViewById(R.id.amount_silver);
        amount_bronze = (TextView)findViewById(R.id.amount_bronze);

        amount_gold.setText(gold_counter + " badges");
        amount_silver.setText(silver_counter + " badges");
        amount_bronze.setText(bronze_counter + " badges");

        rv = (RecyclerView)findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeAdapter();
    }

    private void initializeAdapter(){
        Log.i(TAG, "Sending " + badges.size() + " badges");
        RVAdapter adapter = new RVAdapter(badges);
        rv.setAdapter(adapter);
    }


}
