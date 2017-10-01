package com.moonhythe.songle;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends Activity {

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
    }
}
