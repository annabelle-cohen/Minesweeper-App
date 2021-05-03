package com.example.hw1_minsweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.example.hw1_minsweeper.Logic.Game;
import com.example.hw1_minsweeper.Logic.TileAdapter;
import java.text.ParseException;

public class ThirdActivity extends AppCompatActivity implements InputMessageDialog.inputNameDialogListener{

    mySharedPref sharedPref;
    TextView textViewTimerFinal;
    TextView winOrLose;
    TileAdapter tileAdapter;
    GridView gridView;
    Game game;
    Button buttonExit;
    Button buttonNewGame;
    String name = "none";
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        buttonExit=findViewById(R.id.EXIT);
        buttonNewGame=findViewById(R.id.NewGame);
        game =  (Game)bundle.getSerializable(SecondActivity.ACTIVITY_RESULT_KEY_SECOND);
        gridView=findViewById(R.id.gridViewFinal);
        gridView.setNumColumns(game.getBoard().getWidth());
        winOrLose=findViewById(R.id.youWinOrLoseText);
        sharedPref = new mySharedPref(getApplicationContext());

        if(game.isWon()){
            winOrLose.setText("YOU WON THE GAME!");
            winOrLose.setTextColor(Color.GREEN);
            //needs to create class or interface for all keys!
            if(this.game.getType().equals("Beginner")){
               String key1="Beginner_Score_1";
                String key2="Beginner_Score_2";
                String key3= "Beginner_Score_3";
                try {
                    this.whoGotHighScore(key1,key2,key3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else if(this.game.getType().equals("Intermediate")){
                String key1="Intermediate_Score_1";
                String key2="Intermediate_Score_2";
                String key3= "Intermediate_Score_3";
                try {
                    this.whoGotHighScore(key1,key2,key3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }else if(this.game.getType().equals("Expert")){
                String key1="Expert_Score_1";
                String key2="Expert_Score_2";
                String key3= "Expert_Score_3";
                try {
                    this.whoGotHighScore(key1,key2,key3);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }else{
            winOrLose.setText("GAME OVER!");
            winOrLose.setTextColor(Color.RED);
        }

        textViewTimerFinal=findViewById(R.id.timerEndText);
        tileAdapter=new TileAdapter(this,game.getBoard());
        gridView.setAdapter(tileAdapter);
        textViewTimerFinal.setText(game.getTimer());

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        buttonNewGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newGame();
            }
        });
    }


    private void exit(){
        onStop();
        ThirdActivity.this.finishAffinity();
        System.exit(0);
    }
    private void newGame(){
        onStop();
        Intent intentSecond = new Intent(ThirdActivity.this,MainActivity.class);
        startActivity(intentSecond);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((AnimationDrawable)findViewById(R.id.my_mine).getBackground()).start();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    private void whoGotHighScore(String key1,String key2,String key3) throws ParseException {
        String val=this.getName()+" "+this.game.getTimeTotalForCheck()+" "+this.game.getMoves();
        if(sharedPref.getString(key1,"None").equals("None")){
            this.OpenDialog();
            setKey(key1);

        }else{
            Log.d("in forst else","else");
            String highest_Score1 =sharedPref.getString(key1,"None");
            String highest_Score2=sharedPref.getString(key2,"None");
            String highest_Score3=sharedPref.getString(key3,"None");
            checkIfUpdateIsNeeded1(highest_Score1,highest_Score2,highest_Score3,val,key1,key2,key3);
        }
    }

    private void checkIfUpdateIsNeeded1(String val1,String val2,String val3,String value,String key1,String key2,String key3) throws ParseException {
        boolean isAnotherHighScore=this.game.checkByTime(val1,this.game.getTimeTotalForCheck());
        Log.d("check bool",isAnotherHighScore+"");
        if(!isAnotherHighScore){
            Log.d("in second if","if");
            checkIfUpdateIsNeeded2(val2,val3,value,key2,key3);
        }else{
            Log.d("in second if",key1);
            this.OpenDialog();
            setKey(key1);
            if(!val2.equals("None")) {
                updateScore(key2, val1);
                updateScore(key3, val2);
            }else{
                updateScore(key2, val1);
            }
        }
    }

    private void checkIfUpdateIsNeeded2(String val2,String val3,String value,String key2,String key3) throws ParseException {
        if(sharedPref.getString(key2,"None").equals("None")){
            this.OpenDialog();
            setKey(key2);
        }else {
            boolean isAnotherHighScore2 = this.game.checkByTime(val2, this.game.getTimeTotalForCheck());
            Log.d("check bool", isAnotherHighScore2 + "");
            if (!isAnotherHighScore2) {
                Log.d("in second2 if", "neede2");
                checkIfUpdateIsNeeded3(val3, value, key3);
            } else {
                Log.d("in else2 if", "else needed 2");
                this.OpenDialog();
                setKey(key2);
                updateScore(key3, val2);
            }
        }
    }

    private void checkIfUpdateIsNeeded3(String val3,String value,String key3) throws ParseException {
        if(sharedPref.getString(key3,"None").equals("None")){
            this.OpenDialog();
            setKey(key3);
        }else {
            boolean isAnotherHighScore3 = this.game.checkByTime(val3, this.game.getTimeTotalForCheck());
            Log.d("check bool", isAnotherHighScore3 + "");
            if (isAnotherHighScore3) {
                Log.d("in else3 if", "else needed 3");
                this.OpenDialog();
                setKey(key3);
            }
        }
    }

    private void updateScore(String key,String value) {
        this.sharedPref.putString(key,value);

    }

    private void OpenDialog(){
        InputMessageDialog inputMessageDialog= new InputMessageDialog();
        inputMessageDialog.show(getSupportFragmentManager(),"input name");
    }

    @Override
    public void applyName(String name) {
        this.setName(name);
        saveNewWinner(getKey(),name+" "+this.game.getTimeTotalForCheck()+" "+this.game.getMoves());
        //Log.d("apply",""+this.name);
    }

    private void saveNewWinner(String key,String value){
        sharedPref.putString(key,value);
    }


    private void setName(String name){
        this.name = name;
       Log.d("set",""+this.name);
    }

    public String getName() {
        return name;
    }
}

