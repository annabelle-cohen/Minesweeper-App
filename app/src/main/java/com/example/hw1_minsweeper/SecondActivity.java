package com.example.hw1_minsweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import com.example.hw1_minsweeper.Logic.Game;
import com.example.hw1_minsweeper.Logic.TileAdapter;

public class SecondActivity extends AppCompatActivity implements SensorServiceListener {

    SensorsService.SensorsServiceBinder binder;
    boolean isBound=false;
    TextView textViewTimer;
    TextView textViewMinesLeft;
    Game game;
    TileAdapter tileAdapter;
    GridView gridView;
    public static final String ACTIVITY_RESULT_KEY_SECOND="Game";
    Button buttonStop;
    Button buttonRestart;
    int tempMinesLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        String buttonPressed=null;

        Intent intent = getIntent();
        if (intent != null) {
            buttonPressed = intent.getStringExtra(MainActivity.ACTIVITY_RESULT_KEY);
        }
        game = new Game(buttonPressed);
        textViewTimer=findViewById(R.id.timerText);
        textViewMinesLeft=findViewById(R.id.minesLeft);
        gridView=findViewById(R.id.gridView);
        gridView.setNumColumns(game.getBoard().getWidth());
        buttonStop=findViewById(R.id.stopButton);
        buttonRestart=findViewById(R.id.newGameButton);
        tileAdapter=new TileAdapter(this,game.getBoard());
        gridView.setAdapter(tileAdapter);
        textViewTimer.setText(game.StartClock(game.getClock()));
        this.tempMinesLeft=game.getMinesLeft();
        textViewMinesLeft.setText("Mines Left:"+tempMinesLeft);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while (!game.isWon()&&!game.isLos()) {
                    game.Run(game.getClock());
                    try {
                        Thread.sleep( 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textViewTimer.setText(game.StartClock(game.getClock()));
                            tileAdapter.notifyDataSetChanged();

                        }
                    });

                }

                binder.stopSensors();
                unbindService(mConnection);
                startThirdActivity(game);
            }
        });

        t.start();
        gridView.setFocusable(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                game.playTile(position);
                tileAdapter.notifyDataSetChanged();
            }

        });
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                game.playTileForLong(position);
                if (tempMinesLeft!=game.getMinesLeft()){
                    tempMinesLeft=game.getMinesLeft();
                    textViewMinesLeft.setText("Mines Left:"+tempMinesLeft);
                }
                tileAdapter.notifyDataSetChanged();
                return true;
            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop();
            }
        });
        buttonRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restart();
            }
        });


    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        binder = (SensorsService.SensorsServiceBinder) service;
        binder.registerListener(SecondActivity.this);
        isBound=true;
        binder.starSensors();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

            isBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, SensorsService.class);
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);
    }

    private void restart(){
        onStop();
        Intent intentSecond = new Intent(SecondActivity.this,MainActivity.class);
        startActivity(intentSecond);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isBound){
            binder.resetInitialLock();
            binder.starSensors();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(isBound){
            binder.stopSensors();
        }
    }

    private void stop(){
        onStop();
        SecondActivity.this.finishAffinity();
        System.exit(0);
    }
    private void startThirdActivity(Game game){
        Bundle bundle = new Bundle();
        bundle.putSerializable(SecondActivity.ACTIVITY_RESULT_KEY_SECOND, game);
        Intent intentSecond = new Intent(SecondActivity.this,ThirdActivity.class);
        intentSecond.putExtras(bundle);
        startActivity(intentSecond);
    }


    @Override
    protected void onStop() {

        super.onStop();
        if(isBound){
            unbindService(mConnection);
            isBound=false;
        }
    }


    @Override
    public void alarmStateChanged(ALARM_STATE alarm_state) {
        if(alarm_state.equals(ALARM_STATE.ON)){
            this.game.setStartCover(true);
            this.game.setStartPlantMines(true);
            startPunish();
        }
    }

    @Override
    public void startPunish() {
        this.game.startPunish();
        textViewMinesLeft.setText("Mines Left:"+this.game.getMinesLeft());
        tileAdapter.notifyDataSetChanged();
    }

    @Override
    public void stopPunish() {
        this.game.setStartCover(false);
        this.game.setStartPlantMines(false);
        this.game.stopPunish();
        textViewMinesLeft.setText("Mines Left:"+this.game.getMinesLeft());
        tileAdapter.notifyDataSetChanged();
    }
}
