package com.inhatc.jinuk.proximitysensor_source;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager objSMG;
    ImageView objIMG;
    Vibrator objVIB;
    Sensor sensor_Proximity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objSMG = (SensorManager)getSystemService(SENSOR_SERVICE);
        objIMG = (ImageView)findViewById(R.id.imageView);
        objVIB = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sensor_Proximity = objSMG.getDefaultSensor(Sensor.TYPE_PROXIMITY);
    }
    @Override
    protected void onResume() {
        super.onResume();

        objSMG.registerListener(this, sensor_Proximity, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_PROXIMITY){
            if(event.values[0] <= 5){
                Animation objANI = AnimationUtils.loadAnimation(this, R.anim.jumping);
                objIMG.setImageResource(R.drawable.emoticon2);
                objIMG.startAnimation(objANI);
                objVIB.vibrate(1000);
            }else {
                objIMG.setImageResource(R.drawable.emoticon1);
            }
        }
    }
}
