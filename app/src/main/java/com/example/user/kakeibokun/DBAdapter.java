package com.example.user.kakeibokun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.database.Cursor;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.List;

//DBに関連するクラス
public class DBAdapter {
    private static final String TAG = "DbOpenHelper"; // 不明
    //データベース名を定数に登録
    private static final String DB_NAME = "kakeibokun.db";
    //テーブル名を登録
    private static final String DB_TABLE_NAME1 = "shisyutu";
    private static final String DB_TABLE_NAME2 = "syunyu";
    //データベースのバージョンを登録
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db = null;
    private DBHelper dbHelper = null;
    protected Context context;

    //DBのカラム
    public final static String COL_ID = "id";
    public final static String COL_YEAR = "Year";
    public final static String COL_MONTH = "Month";
    public final static String COL_DAY = "Day";
    public final static String COL_Tag = "Tag";
    public final static String COL_Uchiwake = "Uchiwake";
    public final static String COL_Kingaku = "Kingaku";

    //コンストラクタ
    public DBAdapter(Context context) {
        this.context = context;
        dbHelper = new DBHelper(this.context);
    }

    //DBの読み書き
    public DBAdapter openDB() {
        db = dbHelper.getWritableDatabase();    //DBの読み書き
        return this;
    }

    //DBの読み込み
    public DBAdapter readDB() {
        db = dbHelper.getReadableDatabase();    //DBの読み込み
        return this;
    }

    //DBを閉じる
    public void closeDB() {
        db.close(); //DBを閉じる
        db = null;
    }

    //DBのレコードへ登録(支出）
    public void saveDB1(String year, String month, String day, String tag, String uchiwake, String kingaku) {
        db.beginTransaction();  //トランザクションの開始

        try {
            //挿入するデータはContentValuesに格納
            ContentValues val = new ContentValues();
            val.put("Year", year);
            val.put("Month", month);
            val.put("Day", day);
            val.put("Tag", tag);
            val.put("Uchiwake", uchiwake);
            val.put("Kingaku", kingaku);

            //shisyutuテーブルに1件追加
            db.insert(DB_TABLE_NAME1, null, val);
            db.setTransactionSuccessful();  //トランザクションへのコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();    //トランザクションの終了
        }
    }

    //DBのレコードへ登録(収入）
    public void saveDB2(String year, String month, String day, String tag, String kingaku) {
        db.beginTransaction();  //トランザクションの開始

        try {
            //挿入するデータはContentValuesに格納
            ContentValues val = new ContentValues();
            val.put("Year", year);
            val.put("Month", month);
            val.put("Day", day);
            val.put("Tag", tag);
            val.put("Kingaku", kingaku);

            //shisyutuテーブルに1件追加
            db.insert(DB_TABLE_NAME2, null, val);
            db.setTransactionSuccessful();  //トランザクションへのコミット
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();    //トランザクションの終了
        }
    }

    //DBのデータを取得（支出)
    public Cursor getDB1(String year, String month) {
        //queryメソッド　DBのデータを取得
        return db.rawQuery("select rowid, * from shisyutu where Year = ? and Month = ? order by Day + 0", new String[]{year, month}); //rowidはエラーが出る
    }

    //DBのデータを取得（収入)
    public Cursor getDB2(String year, String month) {
        //queryメソッド　DBのデータを取得
        return db.rawQuery("select rowid, * from syunyu where Year = ? and Month = ? order by Day + 0", new String[]{year, month}); //rowidはエラーが出る
    }

    public void allDelete1(String position) {
        db.beginTransaction();
        try {
            db.delete(DB_TABLE_NAME1, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void allDelete2(String position) {
        db.beginTransaction();
        try {
            db.delete(DB_TABLE_NAME2, null, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void selectDelete1(int position) {
        db.beginTransaction();
        try {
            db.delete("shisyutu", "rowid=" + position, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public void selectDelete2(int position) {
        db.beginTransaction();
        try {
            db.delete(DB_TABLE_NAME2, "rowid=" + position, null);
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    //トータルの支出を計算する
    public int TogalShisyutu(String month, String year){
        int Total = 0;
        String SQL = "";
        SQL += "select ";
        SQL += "sum(ifnull(Kingaku,0)) as Total ";
        SQL += "from shisyutu ";
        SQL += "where Month = '" + month +"'";
        SQL += " and Year = '" + year + "'";

        Cursor cursor = db.rawQuery(SQL,null);
        if (cursor.moveToNext()){
            Total = cursor.getInt(0);
        }
        cursor.close();

        return Total;
    }

    //収入の合計を取得する
    public int TotalSyunyu(String month, String year){
        int Total = 0;

        String SQL = "";
        SQL += "select ";
        SQL += "sum(ifnull(Kingaku,0)) as Total ";
        SQL += "from syunyu ";
        SQL += "where Month = '" + month +"'";
        SQL += " and Year = '" + year + "'";

        Cursor cursor = db.rawQuery(SQL,null);
        if (cursor.moveToNext()){
            Total = cursor.getInt(0);
        }
        cursor.close();

        return Total;
    }

    //カテゴリーごとの合計値を取得する
    public int TogalKategory(String kategory, String month, String year){
        int Total = 0;
        String SQL = "";
        SQL += "select ";
        SQL += "sum(ifnull(Kingaku,0)) as Total ";
        SQL += "from shisyutu ";
        SQL += "where Tag = '" + kategory + "'";
        SQL += " and Month = '" + month +"'";
        SQL += " and Year = '" + year + "'";

        Cursor cursor = db.rawQuery(SQL,null);
        if (cursor.moveToNext()){
            Total = cursor.getInt(0);
        }
        cursor.close();

        return Total;
    }

    //DBから現在のTagの欄に入力されているものをArrayList形式で受け取る
    public List<String> TagList(String month, String year){
        List<String> list = new ArrayList<String>();
        String tag_name;
        Cursor cursor = db.rawQuery("select distinct Tag from shisyutu where Month = " + month + " and Year = " + year, null);
        while (cursor.moveToNext()) {
            tag_name = cursor.getString(0);
            list.add(tag_name);
        }
        cursor.close();
        return list;
    }

    //DBの生成やアップグレードを管理するSQLiteOpenHelperを継承
    private static class DBHelper extends SQLiteOpenHelper {

        //データベースを作成、または開く、管理するための処理
        //ヘルパークラスのコンストラクターの呼び出し
        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        //テーブルを作成するメソッドの定義
        //DB生成時に呼ばれる
        @Override
        public void onCreate(SQLiteDatabase db) {
            createShisyutuTable(db);
            createSyunyuTable(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //古いバージョンのテーブルが存在する場合はこれを削除
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME1 + ";");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_NAME2 + ";");
            //新規テーブルの作成
            onCreate(db);
        }

        private void createShisyutuTable(SQLiteDatabase db) {
            //テーブル作成SQL
            String sql = "CREATE TABLE " + DB_TABLE_NAME1 + "("
                    + " id INTENGER PRIMARY KEY, "
                    + " Year TEXT,"
                    + " Month TEXT,"
                    + " Day TEXT,"
                    + " Tag TEXT,"
                    + " Uchiwake TEXT,"
                    + " Kingaku TEXT"
                    + ");";
            db.execSQL(sql);
            Log.d(TAG, "テーブルshisyutuが作成されました");
        }

        private void createSyunyuTable(SQLiteDatabase db) {
            //テーブル作成SQL
            String sql = "CREATE TABLE " + DB_TABLE_NAME2 + "("
                    + " id INTENGER PRIMARY KEY, "
                    + " Year TEXT,"
                    + " Month TEXT,"
                    + " Day TEXT,"
                    + " Tag TEXT,"
                    + " Kingaku TEXT"
                    + ");";
            db.execSQL(sql);
            Log.d(TAG, "テーブルsyunyuが作成されました");
        }
    }
}


