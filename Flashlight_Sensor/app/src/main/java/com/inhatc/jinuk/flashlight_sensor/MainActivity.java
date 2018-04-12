package com.inhatc.jinuk.flashlight_sensor;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private boolean checkMERAPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    SensorManager objSMG;
    Sensor sensor_Illuminance;
    ImageView objIMG;
    android.hardware.Camera objCAM;
    android.hardware.Camera.Parameters objCamPara;

    TextView objTV_Lux;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        objSMG = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor_Illuminance = objSMG.getDefaultSensor(Sensor.TYPE_PRESSURE);

        objCAM = android.hardware.Camera.open();
        objIMG = (ImageView) findViewById(R.id.imageView);

        objTV_Lux = (TextView) findViewById(R.id.txtLux);
    }

    @Override
    public void onResume() {
        super.onResume();
        objSMG.registerListener(this, sensor_Illuminance, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
        super.onPause();
        objSMG.unregisterListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        objCAM.release();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() == Sensor.TYPE_LIGHT){
            int iLux = (int)event.values[0];
            objTV_Lux.setText(("Lux: " + iLux));
            if(iLux <= 80){
                objIMG.setImageResource(R.drawable.lighton);
                objCamPara.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                objCAM.setParameters(objCamPara);
                objCAM.startPreview();
            }else {
                objIMG.setImageResource(R.drawable.lightoff);
                objCamPara.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                objCAM.setParameters(objCamPara);
                objCAM.stopPreview();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
