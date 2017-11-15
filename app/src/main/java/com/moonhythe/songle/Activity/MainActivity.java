package com.moonhythe.songle.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.app.AlertDialog;

import com.moonhythe.songle.R;
import com.moonhythe.songle.Structure.Preference;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();

    ImageButton start_btn, instructions_btn, history_btn, settings_btn, continue_btn;

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
        settings_btn = (ImageButton) findViewById(R.id.settings_btn);
        continue_btn = (ImageButton) findViewById(R.id.continue_btn);

        start_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                if(Preference.getSharedPreferenceBoolean(MainActivity.this, "can_continue", false)){
                    AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                    alertDialog.setTitle("Warning!");
                    alertDialog.setMessage("By clicking 'Continue' you will lose the progress of your previous game.");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Continue",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
                                    myIntent.putExtra("continue_game", false);
                                    startActivity(myIntent);
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                } else{
                    Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
                    myIntent.putExtra("continue_game", false);
                    startActivity(myIntent);
                }
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
        settings_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(myIntent);
            }
        });
        continue_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                Intent myIntent = new Intent(MainActivity.this, GameActivity.class);
                myIntent.putExtra("continue_game", true);
                startActivity(myIntent);
            }
        });

        // Check if there's a game to continue
        if(!Preference.getSharedPreferenceBoolean(this, "can_continue", false)){
            continue_btn.setVisibility(View.GONE);
        } else{
            continue_btn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check if there's a game to continue
        if(!Preference.getSharedPreferenceBoolean(this, "can_continue", false)){
            continue_btn.setVisibility(View.GONE);
        } else{
            continue_btn.setVisibility(View.VISIBLE);
        }
    }
}
