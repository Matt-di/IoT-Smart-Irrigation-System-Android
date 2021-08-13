package com.mattih.controlirrigtion.activities;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.mattih.controlirrigtion.R;
import com.mattih.controlirrigtion.data.MyStorage;
import com.mattih.controlirrigtion.models.SensorData;
import com.mattih.controlirrigtion.utilities.BaseUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DisplayGraphically extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_graphically);

        lineChart = findViewById(R.id.lineDisplay);

        configureLineChart();

        new Thread(this::setupLine).start();


    }

    private void setupLine() {
        ArrayList<SensorData> datas = (ArrayList<SensorData>) MyStorage.getPreference(getApplicationContext()).loadSensorResponse();
        ArrayList<Entry> sm = new ArrayList<>();
        ArrayList<Entry> temo = new ArrayList<>();
        ArrayList<Entry> humid = new ArrayList<>();


        int i = 0;
        for (int j = datas.size() - 1; j > 200; j--) {
            SensorData data = datas.get(j);
//            long date = 0;
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//                DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//                LocalDateTime localDateTime = LocalDateTime.parse(data.getUpdatedDate(),simpleDateFormat);

            long date = BaseUtils.toMillisecond(data.getUpdatedDate());

            if (i == 10) break;
            sm.add(new Entry(date, data.getSoilMoisture().floatValue()));
            temo.add(new Entry(date, data.getTemperature().floatValue()));
            humid.add(new Entry(date, data.getHumidity().floatValue()));
            i++;


        }
        LineDataSet soillineDataSet = new LineDataSet(sm, "Soil Moisture");
        LineDataSet temolineDataSet = new LineDataSet(temo, "Temperature ");
        LineDataSet humidlineDataSet = new LineDataSet(humid, "Humidity");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        soillineDataSet.setDrawCircles(false);
        soillineDataSet.setDrawCircles(false);
        soillineDataSet.setLineWidth(3);
        soillineDataSet.setColor(Color.RED);
        soillineDataSet.setCircleColor(Color.RED);

        temolineDataSet.setDrawCircles(false);
        temolineDataSet.setDrawCircles(false);
        temolineDataSet.setLineWidth(3);
        temolineDataSet.setColor(Color.GREEN);
        temolineDataSet.setCircleColor(Color.GREEN);

        humidlineDataSet.setDrawCircles(false);
        humidlineDataSet.setDrawCircles(false);
        humidlineDataSet.setLineWidth(3);
        humidlineDataSet.setColor(Color.BLUE);
        humidlineDataSet.setCircleColor(Color.BLUE);

        dataSets.add(soillineDataSet);
        dataSets.add(temolineDataSet);
        dataSets.add(humidlineDataSet);

        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

    private ArrayList<Entry> datas() {
        ArrayList<Entry> dataValues = new ArrayList<>();
        dataValues.add(new Entry(0, 2));
        dataValues.add(new Entry(3, 12));
        dataValues.add(new Entry(7, 22));
        dataValues.add(new Entry(2, 20));
        dataValues.add(new Entry(0, 21));

        return dataValues;
    }


    private void configureLineChart() {
        Description desc = new Description();
        desc.setText("Sensor Data");
        desc.setTextSize(28);
        lineChart.setDescription(desc);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setLabelCount(5);
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
            private final SimpleDateFormat mFormat2 = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value;
                return String.format("%s \n %s", mFormat.format(new Date(millis)), mFormat2.format(new Date(millis)));
            }
        });
    }
}
