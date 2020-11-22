package com.example.user.kakeibokun;


//支出リストに必要なデータを取得するクラス

import android.util.Log;

public class ShisyutuListItem {
    protected int id;           // ID
    protected String year;  //年
    protected String month; //月
    protected String dayte;     // 日
    protected String kategorys;   // カテゴリ
    protected String p_name;    // 商品名
    protected String kingaku;    // 金額


    public ShisyutuListItem(int id, String year, String month, String dayte, String kategorys, String p_name, String kingaku) {
        this.id = id;
        this.year = year;
        this.month = month;
        this.dayte = dayte;
        this.kategorys = kategorys;
        this.p_name = p_name;
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
     * 商品名を取得
     * getMadeIn()
     *
     * @return madeIn String 商品名
     */
    public String getP_name() {
        return p_name;
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
