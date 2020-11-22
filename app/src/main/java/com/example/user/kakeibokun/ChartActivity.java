package com.example.user.kakeibokun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChartActivity extends Activity{
    PieChart mPieChart;
    private DBAdapter dbAdapter;

    TextView shisyutu_sum;
    String year_set;
    String month_set;

    Intent intent = new Intent();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chartset);

        //DBHelperのコンストラクタ呼び出し
        dbAdapter = new DBAdapter(this);
        dbAdapter.openDB();

        intent = getIntent();
        year_set = intent.getStringExtra("year");
        month_set = intent.getStringExtra("month");

        mPieChart = (PieChart) findViewById(R.id.pie_chart);
        setupPieChartView();

        //現在の月の支出を常に表示
        int Totalshisyutu = dbAdapter.TogalShisyutu(month_set, year_set);
        shisyutu_sum = (TextView)findViewById(R.id.totalhyozi);
        shisyutu_sum.setText(String.valueOf(Totalshisyutu) + "円");

        dbAdapter.closeDB();
    }

    private void setupPieChartView() {
        mPieChart.setUsePercentValues(true);

        Legend legend = mPieChart.getLegend();
        legend.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        //--- DBから支出データを取得する ------------------------
        List<String> tags = new ArrayList<String>();
        List<Integer> kateTotal = new ArrayList<Integer>();

        //まずカテゴリーを取得
        tags = dbAdapter.TagList(month_set, year_set);
        for(int i = 0; i < tags.size(); i++) {
            Log.d("tags", tags.get(i));
        }

        //次にカテゴリーごとの合計値を取得
        for(int i=0; i < tags.size(); i++){
            kateTotal.add(dbAdapter.TogalKategory(tags.get(i), month_set, year_set));
        }
        for(int i = 0; i < kateTotal.size(); i++) {
            Log.d("kateTotal", String.valueOf(kateTotal.get(i)));
        }

        //---------------------------------------------------------

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < kateTotal.size(); i++) {
            entries.add(new Entry(kateTotal.get(i), i));
        }

        PieDataSet dataSet = new PieDataSet(entries, "カテゴリー");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setDrawValues(true);

        PieData pieData = new PieData(tags, dataSet);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(Color.WHITE);

        mPieChart.setData(pieData);
    }
}
