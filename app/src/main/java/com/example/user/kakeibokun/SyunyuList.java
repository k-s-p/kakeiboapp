package com.example.user.kakeibokun;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SyunyuList extends Activity{
    private DBAdapter dbAdapter;
    private SyunyuList.MyBaseAdapter myBaseAdapter; //MyBaseAdapterは後で作成
    private List<SyunyuListItem> items;
    private ListView mListView03;
    protected SyunyuListItem syunyuListItem;

    //日付を取得
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH);
    final int day = calendar.get(Calendar.DAY_OF_MONTH);

    String year_str = String.valueOf(year);
    String month_str = String.valueOf(month+1);
    String day_str = String.valueOf(day);


    TextView nowmonth;
    String month_set = month_str;
    String year_set = year_str;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syunyuhyouzi);

        //次の月と前の月ボタン
        findViewById(R.id.next3).setOnClickListener(buttonMoveListner);
        findViewById(R.id.back3).setOnClickListener(buttonMoveListner);

        //DBHelperのコンストラクタ呼び出し
        dbAdapter = new DBAdapter(this);

        //itemsのArrayList生成
        items = new ArrayList<>();

        //MyBaseAdapterのコンストラクタ呼び出し(myBaseAdapterのオブジェクト生成)
        myBaseAdapter = new SyunyuList.MyBaseAdapter(this, items);

        //ListViewの結び付け
        mListView03 = (ListView)findViewById(R.id.syunyuView);

        loadMyList(); //DBを読み込む＆更新する処理

        //行を長押しした時の処理
        mListView03.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long id) {
                //アラートダイアログの表示
                AlertDialog.Builder builder = new AlertDialog.Builder(SyunyuList.this);
                builder.setTitle("削除");
                builder.setMessage("削除しますか?");
                //Okの時の処理
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        //IDを取得する
                        dbAdapter.openDB(); //DBの読み込み（読み書き）
                        syunyuListItem = items.get(position);
                        int listId = syunyuListItem.getId();

                        dbAdapter.selectDelete2(listId); //DBから取得したidの項目を消去
                        Log.d("Long click : ", String.valueOf(listId));
                        dbAdapter.closeDB(); //DBを閉じる
                        loadMyList();
                    }
                });

                builder.setNeutralButton("キャンセル", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                //ダイアログの表示
                AlertDialog dialog = builder.create();
                dialog.show();

                return false;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //メインページの上に現在の月を表示
        nowmonth = (TextView) findViewById(R.id.month3);
        nowmonth.setText(month_set + "月");

        loadMyList();
    }

    //DBを読み込む＆更新する処理
    private void loadMyList(){
        //ArrayAdapterに対してListViewのリスト(items)の更新
        items.clear();

        dbAdapter.openDB(); //DBの読み込み

        //DBのデータを取得
        Cursor c = dbAdapter.getDB2(year_set, month_set);

        if(c.moveToFirst()){
            do {
                //SyunyuListItemのコンストラクタ呼び出し
                syunyuListItem = new SyunyuListItem(
                        c.getInt(0),
                        //なぜかわからないがi=1を飛ばさないとエラーが出る。。。(おそらくi=1にidでi=0にrowidが入っているような気がする)
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6));

                Log.d("取得したCursor(ID):", String.valueOf(c.getInt(0)));
                Log.d("取得したCursor(年):", c.getString(2));
                Log.d("取得したCursor（月):", c.getString(3));
                Log.d("取得したCursor(日):", c.getString(4));
                Log.d("取得したCursor(カテゴリ):", c.getString(5));
                Log.d("取得したCursor（金額）:", c.getString(6));



                items.add(syunyuListItem); //取得した要素をitemsに追加

            }while(c.moveToNext());
        }
        c.close();
        dbAdapter.closeDB();                           //DBを閉じる
        mListView03.setAdapter(myBaseAdapter);  //ListViewにmyBaseAdapterをセット
        myBaseAdapter.notifyDataSetChanged();     //Viewの更新
    }

    //BaseAdapterを継承したクラス
    public class MyBaseAdapter extends BaseAdapter {
        private Context context;
        private List<SyunyuListItem> items;

        //毎回findViewByIdをすることなく、高速化ができるようするholderクラス
        private class ViewHolder{
            TextView text05Kategorys2;
            TextView text05Kingaku2;
            TextView text05Day2;
        }

        //コンストラクタの生成
        public MyBaseAdapter(Context context, List<SyunyuListItem> items){
            this.context = context;
            this.items = items;
        }

        //Listの要素数を返す
        @Override
        public int getCount(){
            return items.size();
        }

        //indexやオブジェクトを返す
        @Override
        public Object getItem(int position){
            return items.get(position);
        }

        //IDをほかのindexに返す
        @Override
        public long getItemId(int position){
            return position;
        }

        //新しいデータが表示されるタイミングで呼び出される
        @Override
        public View getView(int position, View converView, ViewGroup parent){
            View view = converView;
            SyunyuList.MyBaseAdapter.ViewHolder holder;

            //データを取得
            syunyuListItem = items.get(position);

            if(view == null){
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.syunyu_sheet_listview, parent, false);

                TextView text05Kategorys = (TextView) view.findViewById(R.id.text05Kategorys2);
                TextView text05Kingaku = (TextView) view.findViewById(R.id.text05Kingaku2);
                TextView text05Day = (TextView) view.findViewById(R.id.text05Day2);

                //holderにviewを持たせておく
                holder = new SyunyuList.MyBaseAdapter.ViewHolder();
                holder.text05Kategorys2 = text05Kategorys;
                holder.text05Kingaku2 = text05Kingaku;
                holder.text05Day2 = text05Day;
                view.setTag(holder);
            }else {
                //初めて表示されるときにつけておいたtagを元にviewを取得する
                holder = (SyunyuList.MyBaseAdapter.ViewHolder) view.getTag();
            }

            //取得した各データを各TextViewにセット
            holder.text05Kategorys2.setText(syunyuListItem.getKategorys());
            holder.text05Kingaku2.setText(syunyuListItem.getKingaku() + "円");
            holder.text05Day2.setText(syunyuListItem.getDayte());

            return view;
        }
    }

    //前の月と次の月ボタンが押された時の処理
    View.OnClickListener buttonMoveListner = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            int nowmon;
            int nowyear;
            switch(view.getId()){
                case R.id.back3 :
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

                case R.id.next3 :
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
}
