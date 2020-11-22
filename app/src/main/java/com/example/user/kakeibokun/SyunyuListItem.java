package com.example.user.kakeibokun;

import android.util.Log;

//収入リストの表示に必要な情報を取得するクラス

public class SyunyuListItem {
    protected int id;           // ID
    protected String year;  //年
    protected String month; //月
    protected String dayte;     // 日
    protected String kategorys;   // カテゴリ
    protected String kingaku;    // 金額


    public SyunyuListItem(int id, String year, String month, String dayte, String kategorys, String kingaku) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.dayte = dayte;
        this.kategorys = kategorys;
        this.kingaku = kingaku;
    }

    /**
     * IDを取得
     * getId()
     *
     * @return id int ID
     */
    public int getId() {
        Log.d("取得したID(row)：", String.valueOf(id));
        return id;
    }

    /**
     * カテゴリを取得
     * getProduct()
     *
     * @return product String カテゴリ
     */
    public String getKategorys() {
        return kategorys;
    }

    /**
     * 金額を取得
     * getNumber()
     *
     * @return number String 金額
     */
    public String getKingaku() {
        return kingaku;
    }

    /**
     * 日を取得
     * getPrice()
     *
     * @return price String 日
     */
    public String getDayte() {
        return dayte;
    }

    //月を取得
    public String getMonth(){
        return month;
    }

    //年を取得
    public String getYear(){
        return year;
    }
}
