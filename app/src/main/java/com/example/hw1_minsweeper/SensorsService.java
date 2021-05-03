package com.example.hw1_minsweeper;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

interface SensorServiceListener{
 enum ALARM_STATE{
     ON,OFF
 }

 void alarmStateChanged(ALARM_STATE alarm_state);
 void startPunish();
 void stopPunish();
}
public class SensorsService extends Service implements SensorEventListener {
    private final IBinder binder = new SensorsServiceBinder();
    private final double THRESHOLD = 2;
    private double first_X;
    private double first_Y;
    private double first_Z;
    private SensorServiceListener.ALARM_STATE currentAlarmState=SensorServiceListener.ALARM_STATE.OFF;
    SensorServiceListener sensor_listener;
    SensorManager sensorManager;
    Sensor mySensor;
    SensorEvent sensor_first;


    public class SensorsServiceBinder extends Binder{
        void registerListener(SensorServiceListener listener){
            sensor_listener=listener;
        }

        void starSensors(){
            sensorManager.registerListener( SensorsService.this,mySensor,SensorManager.SENSOR_DELAY_NORMAL);
        }

        void stopSensors(){

            sensorManager.unregisterListener(SensorsService.this);
        }

        void resetInitialLock(){
            sensor_first=null;
        }

        void loseTheGame(SensorServiceListener.ALARM_STATE alarm_state){
            currentAlarmState=alarm_state;
        }

    }
    public SensorsService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        if(mySensor !=null){
            //need to do something
        }else{
            //need to do something
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager=null;
        mySensor=null;
        sensor_listener=null;

    }

    @Override
    public IBinder onBind(Intent intent) {
       return binder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        //כאילו קיבוע אולי צריך לטפל מתמטית בדגימות אך בכל אופן באופן הזה יעשה
        if(sensor_first == null){
            sensor_first =event;
            this.first_X=sensor_first.values[0];
            Log.d("first x"," "+first_X);
            this.first_Y=sensor_first.values[1];
            Log.d("first y"," "+first_Y);
            this.first_Z=sensor_first.values[2];
            Log.d("first Z"," "+first_Z);

        }else{

            double changeX=event.values[0];
            Log.d("second x"," "+changeX);
            double absDiffX = Math.abs(this.first_X-changeX);
            Log.d("try"," "+absDiffX);
            double changeY=event.values[1];
            double absDiffY = Math.abs(this.first_Y-changeY);
            double changeZ=event.values[2];
            double absDiffZ = Math.abs(this.first_Z-changeZ);

           Log.d("DIFFS", "" + absDiffX + " " + absDiffY + " " + absDiffZ);

          //  SensorServiceListener.ALARM_STATE previousState = currentAlarmState;

            if(absDiffX>THRESHOLD || absDiffY >THRESHOLD || absDiffZ > THRESHOLD){
                currentAlarmState = SensorServiceListener.ALARM_STATE.ON;
                Log.d("in if","hey");
            }else{
                currentAlarmState = SensorServiceListener.ALARM_STATE.OFF;
                Log.d("in else","bye");
            }

            if(currentAlarmState.equals(SensorServiceListener.ALARM_STATE.ON)){
                sensor_listener.alarmStateChanged(currentAlarmState);
                Log.d("in","state");
            }else{
                sensor_listener.stopPunish();
                Log.d("out of","state");
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
