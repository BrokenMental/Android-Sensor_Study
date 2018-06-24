package com.inhatc.jinuk.countingjump;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager objSMG;
    Sensor sensor_Accelerometer;
    TextView objTV_Y_Accelerometer;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num = 0;

        sensor_Accelerometer = objSMG.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        objTV_Y_Accelerometer = (TextView) findViewById(R.id.CountingNumber);
        }
    @Override
    public void onResume() {
        super.onResume();

        objSMG.registerListener(this, sensor_Accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

    @Override
    public void onPause() {
        super.onPause();
        objSMG.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){
            if(sensorEvent.values[0] > 5){
                objTV_Y_Accelerometer.setText(num ++);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
