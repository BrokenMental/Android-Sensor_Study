package cs.inhatc.sensor.smart_sensor_project;

import android.database.Cursor;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.hardware.Sensor.TYPE_ACCELEROMETER;
import static cs.inhatc.sensor.smart_sensor_project.KakaoMapViewer.vo;

public class WalkingViewer extends AppCompatActivity implements SensorEventListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_walking);

        SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd");
        String date = fm1.format(new Date());

        vo.setBarChart((BarChart)findViewById(R.id.chart));

        vo.setObjSMG((SensorManager)getSystemService(SENSOR_SERVICE));
        vo.setSensor_Accelerometer(vo.getObjSMG().getDefaultSensor(TYPE_ACCELEROMETER));
        walkSelect(date);

        BarDataSet barDataSet = new BarDataSet(vo.getEntries(), null);
        barDataSet.setDrawValues(false);
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet.setDrawValues(!barDataSet.isDrawValuesEnabled());
        barDataSet.setValueTextSize(15);

        BarData barData = new BarData(barDataSet);
        vo.getBarChart().setData(barData);
        vo.getBarChart().setFitBars(true);
        vo.getBarChart().getLegend().setEnabled(false);

        XAxis xAxis = vo.getBarChart().getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextSize(14);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(vo.getLabels()));

        YAxis yLAxis = vo.getBarChart().getAxisLeft();
        yLAxis.setTextColor(Color.BLACK);
        yLAxis.setTextSize(14);
        yLAxis.setAxisMinimum(0f);
        yLAxis.setDrawGridLines(true);

        YAxis yRAxis = vo.getBarChart().getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        Description description = new Description();
        description.setText("");

        vo.getBarChart().setDoubleTapToZoomEnabled(false);
        vo.getBarChart().setDrawGridBackground(false);
        vo.getBarChart().setDescription(description);
        vo.getBarChart().setNoDataText("걸음걸이 데이터가 없습니다.");
        vo.getBarChart().setNoDataTextColor(Color.BLACK);
        vo.getBarChart().animateY(2000, Easing.EasingOption.EaseInCubic);
        vo.getBarChart().invalidate();
    }

    public void walkSelect(String date) {
        vo.setWalkCount((TextView)findViewById(R.id.walkCount));
        vo.setWalkData(new WalkData(this));

        vo.setSql(vo.getWalkData().getWritableDatabase());

        Cursor cursor;
        cursor = vo.getSql().rawQuery("SELECT * FROM WalkHistory ORDER BY WalkHistory._id DESC Limit 7;", null);

        while (cursor.moveToNext())
        {
            vo.getEntries().add(new BarEntry(vo.getChartLine(), cursor.getInt(1)));
            vo.getLabels().add(cursor.getString(2));
            vo.setChartLine(vo.getChartLine()+1);

            if(date.matches(cursor.getString(2)))
            {
                vo.getWalkCount().setText("" + cursor.getString(1));
                vo.setMoveCount(Integer.parseInt(cursor.getString(1)));
            }
        }

        cursor.close();
        vo.getSql().close();
    }

    public void walkCounting(SensorEvent event){
        if (event.sensor.getType() == TYPE_ACCELEROMETER)
        {
            long currentTime = System.currentTimeMillis();
            long gabOfTime = (currentTime - vo.getLastTime());
            if (gabOfTime > 100) {
                vo.setLastTime(currentTime);

                vo.setSpeed(Math.abs(event.values[0] + event.values[1] + event.values[2] - vo.getLastX() - vo.getLastY() - vo.getLastZ()) / gabOfTime * 10000);

                if (vo.getSpeed() > vo.getShakeThreshold()) {
                    vo.setMoveCount(vo.getMoveCount()+1);
                    vo.getWalkCount().setText(""+vo.getMoveCount());
                }

                vo.setLastX(event.values[0]);
                vo.setLastY(event.values[1]);
                vo.setLastZ(event.values[2]);
            }
        }
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - vo.getBackPressedTime();

        if (0 <= intervalTime && vo.getFINISH_INTERVAL_TIME() >= intervalTime) {
            super.onBackPressed();

            SimpleDateFormat fm1 = new SimpleDateFormat("yyyy-MM-dd",Locale.KOREA);
            String date = fm1.format(new Date());
            vo.setSql(vo.getWalkData().getWritableDatabase());
            vo.getSql().execSQL("INSERT INTO WalkHistory SELECT NULL,0,'"+date+"' WHERE NOT EXISTS(SELECT WalkHistory.Testdate FROM WalkHistory WHERE WalkHistory.Testdate='"+date+"');");
            vo.getSql().execSQL("UPDATE WalkHistory SET walk_cnt="+Integer.parseInt(""+vo.getWalkCount().getText())+" WHERE WalkHistory.Testdate='"+date+"';");
            vo.getSql().close();
        } else {
            vo.setBackPressedTime(tempTime);
            Toast.makeText(this, "뒤로 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        vo.getObjSMG().registerListener(this, vo.getSensor_Accelerometer(), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        vo.getObjSMG().unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        walkCounting(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
