package cs.inhatc.sensor.smart_sensor_project;

import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class valueVO {
    private long lastTime;
    private float speed;
    private float lastX;
    private float lastY;
    private float lastZ;

    private BarChart barChart;
    private int chartLine;

    //그래프 그리기 위한 ArrayList
    private ArrayList<BarEntry> entries = new ArrayList<>();
    private ArrayList<String> labels = new ArrayList<String>();

    //뒤로가기 버튼 Delay
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    //가속도 센서 Delay
    private final int SHAKE_THRESHOLD = 800;

    private SQLiteDatabase sql;

    private int moveCount;
    private SensorManager objSMG;
    private Sensor sensor_Accelerometer;
    private WalkData walkData;
    private TextView walkCount;

    public long getLastTime() {
        return lastTime;
    }

    public void setLastTime(long lastTime) {
        this.lastTime = lastTime;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getLastX() {
        return lastX;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getLastZ() {
        return lastZ;
    }

    public void setLastZ(float lastZ) {
        this.lastZ = lastZ;
    }

    public BarChart getBarChart() {
        return barChart;
    }

    public void setBarChart(BarChart barChart) {
        this.barChart = barChart;
    }

    public int getChartLine() {
        return chartLine;
    }

    public void setChartLine(int chartLine) {
        this.chartLine = chartLine;
    }

    public ArrayList<BarEntry> getEntries() {
        return entries;
    }

    public void setEntries(ArrayList<BarEntry> entries) {
        this.entries = entries;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public void setLabels(ArrayList<String> labels) {
        this.labels = labels;
    }

    public long getFINISH_INTERVAL_TIME() {
        return FINISH_INTERVAL_TIME;
    }

    public long getBackPressedTime() {
        return backPressedTime;
    }

    public void setBackPressedTime(long backPressedTime) {
        this.backPressedTime = backPressedTime;
    }

    public int getShakeThreshold() {
        return SHAKE_THRESHOLD;
    }

    public SQLiteDatabase getSql() {
        return sql;
    }

    public void setSql(SQLiteDatabase sql) {
        this.sql = sql;
    }

    public int getMoveCount() {
        return moveCount;
    }

    public void setMoveCount(int moveCount) {
        this.moveCount = moveCount;
    }

    public SensorManager getObjSMG() {
        return objSMG;
    }

    public void setObjSMG(SensorManager objSMG) {
        this.objSMG = objSMG;
    }

    public Sensor getSensor_Accelerometer() {
        return sensor_Accelerometer;
    }

    public void setSensor_Accelerometer(Sensor sensor_Accelerometer) {
        this.sensor_Accelerometer = sensor_Accelerometer;
    }

    public WalkData getWalkData() {
        return walkData;
    }

    public void setWalkData(WalkData walkData) {
        this.walkData = walkData;
    }

    public TextView getWalkCount() {
        return walkCount;
    }

    public void setWalkCount(TextView walkCount) {
        this.walkCount = walkCount;
    }
}
