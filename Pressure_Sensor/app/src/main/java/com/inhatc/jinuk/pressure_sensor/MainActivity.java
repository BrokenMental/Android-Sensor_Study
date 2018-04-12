package com.inhatc.jinuk.pressure_sensor;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager objSMG;
    Sensor sensor_AmbientPressure;
    Sensor sensor_AmbientTemperature;
    Sensor sensor_Temperature;
    Sensor sensor_Illuminance;
    Sensor sensor_Humidity;

    TextView objTV_AmbientPressure;
    TextView objTV_AmbientTemperature;
    TextView objTV_Temperature;
    TextView objTV_Illuminance;
    TextView objTV_Humidity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objSMG = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor_AmbientPressure = objSMG.getDefaultSensor(Sensor.TYPE_PRESSURE);
        sensor_AmbientTemperature = objSMG.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        sensor_Temperature = objSMG.getDefaultSensor(Sensor.TYPE_TEMPERATURE);
        sensor_Illuminance = objSMG.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensor_Humidity = objSMG.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);

        objTV_AmbientPressure = (TextView) findViewById(R.id.txtAmbientPressure);
        objTV_AmbientTemperature = (TextView) findViewById(R.id.txtAmbientTemperature);
        objTV_Temperature = (TextView) findViewById(R.id.txtTemperature);
        objTV_Illuminance = (TextView) findViewById(R.id.txtIlluminance);
        objTV_Humidity = (TextView) findViewById(R.id.txtHumidity);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        objSMG.registerListener(this, sensor_AmbientPressure, SensorManager.SENSOR_DELAY_NORMAL);
        objSMG.registerListener(this, sensor_AmbientTemperature, SensorManager.SENSOR_DELAY_NORMAL);
        objSMG.registerListener(this,sensor_Temperature, SensorManager.SENSOR_DELAY_NORMAL);
        objSMG.registerListener(this,sensor_Illuminance, SensorManager.SENSOR_DELAY_NORMAL);
        objSMG.registerListener(this,sensor_Humidity, SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    protected void onPause() {
        super.onPause();
        objSMG.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case  Sensor.TYPE_PRESSURE:
                objTV_AmbientPressure.setText(("Ambient Pressure: "+ event.values[0]));
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                objTV_AmbientTemperature.setText(("Ambient Temperature: " + event.values[0]));
                break;
            case Sensor.TYPE_TEMPERATURE:
                objTV_Temperature.setText(("Temperature: " + event.values[0]));
                break;
            case Sensor.TYPE_LIGHT:
                objTV_Illuminance.setText(("Illuminance: " + event.values[0]));
                break;
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                objTV_Humidity.setText(("Humidity:" + event.values[0]));
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
