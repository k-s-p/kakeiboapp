package com.example.user.kakeibokun;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class Syunyu extends Activity{

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
        setContentView(R.layout.activity_syunyu);

        //プリファレンスオブジェクトの取得
        SharedPreferences pref = getSharedPreferences("Kategorys2", MODE_PRIVATE);

        //editText
        editText = (EditText) findViewById(R.id.editText2);
        information = (TextView)findViewById(R.id.information_view2);

        //テンキー
        findViewById(R.id.one2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.twe2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.three2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.four2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.five2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.six2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.seven2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.eight2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.nine2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.zero2).setOnClickListener(buttonNumberListener);
        findViewById(R.id.wzero2).setOnClickListener(buttonNumberListener);

        //バックとリセット
        findViewById(R.id.reset2).setOnClickListener(buttonClearListner);
        findViewById(R.id.back2).setOnClickListener(buttonClearListner);

        //タグボタン
        findViewById(R.id.kategory1_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory2_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory3_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory4_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory5_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory6_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory7_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory8_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory9_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory10_2).setOnClickListener(buttonKategoryListener);
        findViewById(R.id.kategory11_2).setOnClickListener(buttonKategoryListener);

        Button tag1 = findViewById(R.id.kategory1_2);
        Button tag2 = findViewById(R.id.kategory2_2);
        Button tag3 = findViewById(R.id.kategory3_2);
        Button tag4 = findViewById(R.id.kategory4_2);
        Button tag5 = findViewById(R.id.kategory5_2);
        Button tag6 = findViewById(R.id.kategory6_2);
        Button tag7 = findViewById(R.id.kategory7_2);
        Button tag8 = findViewById(R.id.kategory8_2);
        Button tag9 = findViewById(R.id.kategory9_2);
        Button tag10 = findViewById(R.id.kategory10_2);
        Button tag11 = findViewById(R.id.kategory11_2);


        tag1.setText(pref.getString(String.valueOf(tag1.getId()), "バイト"));
        tag2.setText(pref.getString(String.valueOf(tag2.getId()), "なし"));
        tag3.setText(pref.getString(String.valueOf(tag3.getId()), "なし"));
        tag4.setText(pref.getString(String.valueOf(tag4.getId()), "なし"));
        tag5.setText(pref.getString(String.valueOf(tag5.getId()), "なし"));
        tag6.setText(pref.getString(String.valueOf(tag6.getId()), "なし"));
        tag7.setText(pref.getString(String.valueOf(tag7.getId()), "なし"));
        tag8.setText(pref.getString(String.valueOf(tag8.getId()), "なし"));
        tag9.setText(pref.getString(String.valueOf(tag9.getId()), "なし"));
        tag10.setText(pref.getString(String.valueOf(tag10.getId()), "なし"));
        tag11.setText(pref.getString(String.valueOf(tag11.getId()), "なし"));


        //登録ボタン
        findViewById(R.id.dec2).setOnClickListener(buttonTorokuListener);

        //設定ボタン
        findViewById(R.id.setting2).setOnClickListener(buttonSetteiListener);


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
                case R.id.back2 :
                    //一文字削除
                    String textNow = editText.getText().toString();
                    if (textNow.length() > 0){
                        editText.setText(textNow.substring(0,textNow.length()-1));
                        editText.setSelection(textNow.length()-1);
                    }
                    break;

                case R.id.reset2 :
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
                SharedPreferences pref = getSharedPreferences("Kategorys2", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(String.valueOf(button.getId()), kategory_str);
                editor.apply();
                button.setText(pref.getString(String.valueOf(button.getId()), ""));
                kategory_set = false;
            }
        }
    };

    //登録ボタンが押された処理(収入の登録を行う)
    View.OnClickListener buttonTorokuListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new AlertDialog.Builder(Syunyu.this)
                    // OKボタンの設定
                    .setPositiveButton(R.string.kakutei, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            //支出の登録
                            try {
                                dbAdapter.openDB();
                                kingaku = editText.getText().toString();
                                dbAdapter.saveDB2(year_set, month_set, day_set, tag, kingaku);
                                Log.d("収入","収入の登録に成功しました");
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
            new AlertDialog.Builder(Syunyu.this)
                    .setTitle(R.string.settei)
                    .setMessage(R.string.msg1)
                    .setPositiveButton(
                            R.string.tag,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whitch) {
                                    //カテゴリが押された時の動作
                                    // テキスト入力用Viewの作成
                                    final EditText editView = new EditText(Syunyu.this);
                                    new AlertDialog.Builder(Syunyu.this)
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
                                    DatePickerDialog dateDialog = new DatePickerDialog(Syunyu.this,
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
