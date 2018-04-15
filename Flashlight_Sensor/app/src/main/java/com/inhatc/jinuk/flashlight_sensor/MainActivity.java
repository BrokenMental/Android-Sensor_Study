package com.inhatc.jinuk.flashlight_sensor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

// 레퍼런스 : https://developer.android.com/training/permissions/requesting.html#perm-request
// 도움 : http://kerneler.tistory.com/33
public class MainActivity extends AppCompatActivity implements SensorEventListener{

    SensorManager objSMG;
    Sensor sensor_Illuminance;
    ImageView objIMG;
    Camera objCAM;
    Camera.Parameters objCamPara;

    TextView objTV_Lux;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            if(!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.CAMERA)){
                // 권한 요청
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 100);
            }
        }else{
            // 카메라 실행
            //Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        }

        //objCAM = Camera.open();
        objCAM = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
        objSMG = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor_Illuminance = objSMG.getDefaultSensor(Sensor.TYPE_LIGHT);

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
                objCamPara = objCAM.getParameters();
                objCamPara.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                objCAM.setParameters(objCamPara);
                objCAM.startPreview();
            }else {
                objIMG.setImageResource(R.drawable.lightoff);
                objCamPara = objCAM.getParameters();
                objCamPara.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                objCAM.setParameters(objCamPara);
                objCAM.stopPreview();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
