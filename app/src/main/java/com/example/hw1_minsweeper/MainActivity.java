package com.example.hw1_minsweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity implements  ScoresTableFragment.ScoresTableFragmentListener {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private mySharedPref sharedPref;
    private String test = "";
    private ScoresTableFragment scoresTableFragment;
    public static final String ACTIVITY_RESULT_KEY = "Activity_result_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scoresTableFragment = (ScoresTableFragment)getSupportFragmentManager().findFragmentById(R.id.scoreTableFragment);



        radioGroup = findViewById(R.id.radioGroup);
        sharedPref =new mySharedPref(getApplicationContext());
        String defaultLevel = getResources().getString(R.string.Beginner);
        String levelChose = sharedPref.getString(getString(R.string.level), defaultLevel);
       // sharedPref.putString("Beginner_Score_1","Paulina 00:01:12 33");
     //   sharedPref.putString("Beginner_Score_2","Momo 00:01:17 31");
      //  sharedPref.putString("Beginner_Score_3","LuLU 00:01:18 32");

        if(levelChose.equals("Beginner")){
            radioGroup.check(findViewById(R.id.beginner).getId());
        }else if(levelChose.equals("Expert")){
            radioGroup.check(findViewById(R.id.expert).getId());
        }else if(levelChose.equals("Intermediate")){
            radioGroup.check(findViewById(R.id.intermediate).getId());
        }
        Button buttonApply = findViewById(R.id.startButton);
        buttonApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                startSecondActivity();
            }
        });
    }

    private void startSecondActivity() {

        //SharedPreferences.Editor editor = sharedPref.edit();
        sharedPref.putString(getString(R.string.level),String.valueOf(radioButton.getText()));
       // editor.commit();

        Intent intent = new Intent(this, SecondActivity.class);
        intent.putExtra(MainActivity.ACTIVITY_RESULT_KEY, String.valueOf(radioButton.getText()));
        startActivity(intent);
    }

    @Override
    public void scoreTableRequested() {
       /* Intent intent = new Intent(this, FullScoreTableActivity.class);
        startActivity(intent);*/
       scoresTableFragment.moveToFullScoreActivity(this);

    }
}
