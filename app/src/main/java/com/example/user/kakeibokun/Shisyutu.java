package com.example.user.kakeibokun;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Shisyutu extends Activity {

    EditText editText;
    TextView information;

    //日付を取得
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    String year_str = String.valueOf(year);
    String month_str = String.valueOf(month+1);
    String day_str = String.valueOf(day);

    String month_set = month_str;
    String year_set = year_str;
    String day_set = day_str;

    String tag = "なし";
    String kingaku ="0";

    //日付設定ダイアログのインスタンスを格納する変数
    private DatePickerDialog.OnDateSetListener dateListener;

    //カテゴリ編集モードの記憶用変数
    boolean kategory_set = false;
    String kategory_str;

    //データベースオブジェクトの作成
    DBAdapter dbAdapter = new DBAdapter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shisyutu);



        //プリファレンスオブジェクトの取得
        SharedPreferences pref = getSharedPreferences("Kategorys", MODE_PRIVATE);

        //editText
        editText = (EditText) findViewById(R.id.editText);
        information = (TextView)findViewById(R.id.information_view);

        //テンキー
        findViewById(R.id.one).setOnClickListener(buttonNumberListener);
        findViewById(R.id.twe).setOnClickListener(buttonNumberListener);
        findViewById(R.id.three).setOnClickListener(buttonNumberListener);
        findViewById(R.id.four).setOnClickListener(buttonNumberListener);
        findViewById(R.id.five).setOnClickListener(buttonNumberListener);
        findViewById(R.id.six).setOnClickListener(buttonNumberListener);
        findViewById(R.id.seven).setOnClickListener(buttonNumberListener);
        findViewById(R.id.eight).setOnClickListener(buttonNumberListener);
        findViewById(R.id.nine).setOnClickListener(buttonNumberListener);
        findViewById(R.id.zero).setOnClickListener(buttonNumberListener);
        findViewById(R.id.wzero).setOnClickListener(buttonNumberListener);

        //バックとリセット
        findViewById(R.id.reset).setOnClickListener(buttonClearListner);
        findViewById(R.id.back).setOnClickListener(buttonClearListner);

        //タグボタン
        findViewById(R.id.kategory1).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory3).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory4).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory5).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory6).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory7).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory8).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory9).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory10).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory11).setOnClickListener(buttonKategoryListener);

        Button tag1 = findViewById(R.id.kategory1);
        Button tag2 = findViewById(R.id.kategory2);
        Button tag3 = findViewById(R.id.kategory3);
        Button tag4 = findViewById(R.id.kategory4);
        Button tag5 = findViewById(R.id.kategory5);
        Button tag6 = findViewById(R.id.kategory6);
        Button tag7 = findViewById(R.id.kategory7);
        Button tag8 = findViewById(R.id.kategory8);
        Button tag9 = findViewById(R.id.kategory9);
        Button tag10 = findViewById(R.id.kategory10);
        Button tag11 = findViewById(R.id.kategory11);


        tag1.setText(pref.getString(String.valueOf(tag1.getId()), "水道代"));
        tag2.setText(pref.getString(String.valueOf(tag2.getId()), "電気代"));
        tag3.setText(pref.getString(String.valueOf(tag3.getId()), "ガス代"));
        tag4.setText(pref.getString(String.valueOf(tag4.getId()), "食費"));
        tag5.setText(pref.getString(String.valueOf(tag5.getId()), "たばこ"));
        tag6.setText(pref.getString(String.valueOf(tag6.getId()), "雑費"));
        tag7.setText(pref.getString(String.valueOf(tag7.getId()), "なし"));
        tag8.setText(pref.getString(String.valueOf(tag8.getId()), "なし"));
        tag9.setText(pref.getString(String.valueOf(tag9.getId()), "なし"));
        tag10.setText(pref.getString(String.valueOf(tag10.getId()), "なし"));
        tag11.setText(pref.getString(String.valueOf(tag11.getId()), "なし"));


        //登録ボタン
        findViewById(R.id.dec).setOnClickListener(buttonTorokuListener);

        //設定ボタン
        findViewById(R.id.setting).setOnClickListener(buttonSetteiListener);


        //日付設定ダイアログのイベントリスナーをインスタンス化
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int yearr, int monthOfYear, int dayOfMonth) {
                //データを日付に反映させる
                year_set = String.valueOf(yearr);
                month_set = String.valueOf(monthOfYear + 1);
                day_set = String.valueOf(dayOfMonth);
                onResume();
            }
        };

    }

    //onResumeで常に情報を表示
    protected void onResume(){
        super.onResume();

        //常に上にタグなどを表示
        information.setText(year_set + "年" + month_set + "月" + day_set + "日" + tag);
    }

    //数字ボタンが押された時の処理
    View.OnClickListener buttonNumberListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;

            // EditTextの後ろに文字を追加
            editText.append(button.getText());
        }
    };

    //削除系のボタンが押された時の処理
    View.OnClickListener buttonClearListner = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            switch(view.getId()){
                case R.id.back :
                    //一文字削除
                    String textNow = editText.getText().toString();
                    if (textNow.length() > 0){
                        editText.setText(textNow.substring(0,textNow.length()-1));
                        editText.setSelection(textNow.length()-1);
                    }
                    break;

                case R.id.reset :
                    //全文字削除
                    editText.setText("");
                    break;
            }
        }
    };

    //タグボタンが押された時の処理
    View.OnClickListener buttonKategoryListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;

            if(kategory_set == false) {
                //押されたボタンのテキストをタグにする
                tag = (String) button.getText();
                onResume();
            } else {
                //カテゴリーの名称を変更する
                //プリファレンスオブジェクトの取得
                SharedPreferences pref = getSharedPreferences("Kategorys", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(String.valueOf(button.getId()), kategory_str);
                editor.apply();
                button.setText(pref.getString(String.valueOf(button.getId()), ""));
                kategory_set = false;
            }
        }
    };

    //登録ボタンが押された処理(商品名の入力を行う）
    View.OnClickListener buttonTorokuListener = new View.OnClickListener() {
        String uchiwake;
        @Override
        public void onClick(View view) {
            // テキスト入力用Viewの作成
            final EditText editView2 = new EditText(Shisyutu.this);
            new AlertDialog.Builder(Shisyutu.this)
                    .setTitle(R.string.uchiwake)
                    .setView(editView2)
                    // OKボタンの設定
                    .setPositiveButton(R.string.kakutei, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            uchiwake = editView2.getText().toString();
                            //支出の登録
                            try {
                                dbAdapter.openDB();
                                kingaku = editText.getText().toString();
                                dbAdapter.saveDB1(year_set, month_set, day_set, tag, uchiwake, kingaku);
                                Log.d("支出","支出の登録に成功しました");
                                dbAdapter.closeDB();
                                finish();
                            } catch (Exception e){
                                //書き込み失敗時にメッセージを表示
                                showDialog(
                                        getApplicationContext(), "ERROR","データの書き込みに失敗しました"
                                );
                            }
                        }
                        })
                    // キャンセルボタンの設定
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            }
                    })
                    //スキップボタンの設定
                    .setNeutralButton(R.string.skip, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            uchiwake = "不明";
                            //支出の登録
                            try {
                                dbAdapter.openDB();
                                kingaku = editText.getText().toString();
                                dbAdapter.saveDB1(year_set, month_set, day_set, tag, uchiwake, kingaku);
                                Log.d("支出","支出の登録に成功しました");
                                dbAdapter.closeDB();
                                finish();
                            } catch (Exception e){
                                //書き込み失敗時にメッセージを表示
                                showDialog(
                                        getApplicationContext(), "ERROR","データの書き込みに失敗しました"
                                );
                            }
                        }
                        })
                    .show();
        }
    };

    //ダイアログを表示するメソッド
    private static void showDialog(Context context, String title, String text){
        AlertDialog.Builder varAlertDialog = new AlertDialog.Builder(context);
        varAlertDialog.setTitle(title);
        varAlertDialog.setMessage(text);
        varAlertDialog.setPositiveButton("OK", null);
        varAlertDialog.show();
    }

    //設定ボタンが押された時の処理
    View.OnClickListener buttonSetteiListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            //一つ目のダイアログを生成（選択）
            new AlertDialog.Builder(Shisyutu.this)
                    .setTitle(R.string.settei)
                    .setMessage(R.string.msg1)
                    .setPositiveButton(
                            R.string.tag,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whitch) {
                                    //カテゴリが押された時の動作
                                    // テキスト入力用Viewの作成
                                    final EditText editView = new EditText(Shisyutu.this);
                                    new AlertDialog.Builder(Shisyutu.this)
                                    .setTitle("カテゴリを入力してください")
                                    .setView(editView)
                                    // OKボタンの設定
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            kategory_str = editView.getText().toString();
                                            kategory_set = true;
                                            Toast.makeText(getApplicationContext(), "変更するカテゴリをタップ", Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    // キャンセルボタンの設定
                                    .setNegativeButton("キャンセル", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                        }
                                    })
                                    .show();
                                }
                            }
                    )
                    .setNegativeButton(
                            R.string.date,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //日付が押された時の処理
                                    DatePickerDialog dateDialog = new DatePickerDialog(Shisyutu.this,
                                            dateListener,
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH));
                                    //日付設定ダイアログを表示
                                    dateDialog.show();
                                }
                            }
                    )
                    .show();
        }
    };

    //コンフィギュレーションチェンジドを呼ぶ
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("TEST", "onConfigrationChanged()");
    }

}
