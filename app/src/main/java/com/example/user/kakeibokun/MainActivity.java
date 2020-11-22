package com.example.user.kakeibokun;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.MainThread;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.v4.app.FragmentActivity;

import java.util.Calendar;

public class MainActivity extends FragmentActivity {


    //日付を取得
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    String year_str = String.valueOf(year);
    String month_str = String.valueOf(month+1);
    String day_str = String.valueOf(day);

    TextView nowmonth;
    TextView shisyutu_sum;
    TextView syunyu_sum;
    TextView zangaku;
    String month_set = month_str;
    String year_set = year_str;

    //データベースオブジェクトの作成
    DBAdapter dbAdapter = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ((Button) findViewById(R.id.button))
                .setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //登録ダイアログクラスをインスタンス化
                        TorokuDialog fragment1 = new TorokuDialog();
                        //TorokuDialogクラスのshow()メソッドでダイアログフラグメントを表示
                        fragment1.show(
                                //FragmentManagerオブジェクトを取得
                                getSupportFragmentManager(),
                                //フラグメントの識別用タグ（文字列）を設定
                                "toroku_dialog"
                        );
                    }
                });

        //次の月と前の月ボタン
        findViewById(R.id.next).setOnClickListener(buttonMoveListner);
        findViewById(R.id.back).setOnClickListener(buttonMoveListner);

        //支出テキストが押された時
        findViewById(R.id.sisyutu).setOnClickListener(shisyutuTextListner);
        findViewById(R.id.sisyutugaku).setOnClickListener(shisyutuTextListner);

        //収入テキストが押された時
        findViewById(R.id.syunyu).setOnClickListener(syunyuTextListner);
        findViewById(R.id.syunyugaku).setOnClickListener(syunyuTextListner);

        //グラフボタンが押された時、現在の月を送る
        ((Button) findViewById(R.id.button2)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ChartActivity.class);
                intent.putExtra("year", year_set);
                intent.putExtra("month", month_set);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        dbAdapter.openDB();

        //メインページの上に現在の月を表示
        nowmonth = (TextView) findViewById(R.id.month);
        nowmonth.setText(month_set + "月");

        //現在の月の支出を常に表示
        int Totalshisyutu = dbAdapter.TogalShisyutu(month_set, year_set);
        shisyutu_sum = (TextView)findViewById(R.id.sisyutugaku);
        shisyutu_sum.setText(String.valueOf(Totalshisyutu) + "円");

        //現在の月の収入を常に表示
        int Totalsyunyu = dbAdapter.TotalSyunyu(month_set, year_set);
        syunyu_sum = (TextView)findViewById(R.id.syunyugaku);
        syunyu_sum.setText(String.valueOf(Totalsyunyu) + "円");

        //現在の月の収入－支出を表示（残額）
        int Totalzangaku = Totalsyunyu - Totalshisyutu;
        zangaku = (TextView)findViewById(R.id.textView7);
        zangaku.setText(String.valueOf(Totalzangaku) + "円");

        dbAdapter.closeDB();
    }

    //前の月と次の月ボタンが押された時の処理
    View.OnClickListener buttonMoveListner = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            int nowmon;
            int nowyear;
            switch(view.getId()){
                case R.id.back :
                    //前の月の移動する
                    nowmon = Integer.parseInt(month_set) - 1;
                    if(nowmon == 0){
                        nowmon = 12;
                        nowyear = Integer.parseInt(year_set) - 1;
                        year_set = String.valueOf(nowyear);
                    }
                    month_set = String.valueOf(nowmon);
                    onResume();
                    break;

                case R.id.next :
                    //次の月へ移動する
                    nowmon = Integer.parseInt(month_set) + 1;

                    if(nowmon == 13){
                        nowmon = 1;
                        nowyear = Integer.parseInt(year_set) + 1;
                        year_set = String.valueOf(nowyear);
                    }
                    month_set = String.valueOf(nowmon);
                    onResume();
                    break;
            }
        }
    };

    //支出テキストが押された時の処理
    View.OnClickListener shisyutuTextListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, ShisyutuList.class);
            startActivity(intent);
        }
    };

    //収入テキストが押された時
    View.OnClickListener syunyuTextListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(MainActivity.this, SyunyuList.class);
            startActivity(intent);
        }
    };
}
