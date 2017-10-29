package com.moonhythe.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    ImageButton start_btn, instructions_btn, history_btn;

    public void gotoGame(View v){
        Intent gotoGame = new Intent(this, GameActivity.class);
        startActivity(gotoGame);
    }

    public void gotoInstructions(View v){
        Intent gotoInstructions = new Intent(this, InstructionsActivity.class);
        startActivity(gotoInstructions);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        start_btn = (ImageButton) findViewById(R.id.start_btn);
        instructions_btn = (ImageButton) findViewById(R.id.instructions_btn);
        history_btn = (ImageButton) findViewById(R.id.history_btn);

        start_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(myIntent);
            }
        });
        instructions_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, InstructionsActivity.class);
                startActivity(myIntent);
            }
        });
        history_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(myIntent);
            }
        });

    }
}
