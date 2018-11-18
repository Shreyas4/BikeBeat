package com.bike.beat.application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.text.SimpleDateFormat;

/**
 * Created by shrey on 4/1/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME="BikeValues.db";
    public static final String TABLE_NAME="values_table";

    public static final String Results_Table="results_from_trips";
    public static final String COL_DATERES="Date";
    public static final String COL_DISTCOV="Distance_Covered";
    public static final String COL_TT="Time_Taken";
    public static final String COL_AVGSPEED="Avg_Speed";
    public static final String COL_TOPSPEED="Top_Speed";
    public static final String COL_FUELCONSUMED="Fuel_consumed";
    public static final String COL_CURLEV="Curr_Level";
    public static final String COL_FUELAVG="Fuel_avg";

    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+TABLE_NAME+" (String TEXT);");
        db.execSQL("create table "+Results_Table+" (ID INTEGER PRIMARY KEY AUTOINCREMENT,"+COL_DATERES+" TEXT, "+COL_DISTCOV+" FLOAT(2,4), " +
                ""+COL_TT+" FLOAT(2,4), "+COL_AVGSPEED+" FLOAT(2,4),"+COL_TOPSPEED+" FLOAT(2,4),"+COL_CURLEV+" FLOAT(2,4)," +
                ""+COL_FUELCONSUMED+" FLOAT(2,4), " +
                ""+COL_FUELAVG+" FLOAT(2,4) );");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_NAME);
        db.execSQL("drop table if exists "+Results_Table);
        onCreate(db);
    }
    public boolean insertData(String str)
    {

        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("String",str);
        long result = db.insert(TABLE_NAME,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    public void truncData(){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
    }
    public Cursor getAllData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);

        //return table_data;
        return res;
    }

    //
    //PURIFY LOCATIONNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN
    //





    public boolean insertDataRes(String date, Double Distance_Covered, Double Time_Taken, Double Avg_Speed, Double Top_Speed, Double cur_lev,
                                 Double Fuel_consumed, Double Fuel_avg)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(COL_DATERES,date);
        contentValues.put(COL_DISTCOV,Distance_Covered);
        contentValues.put(COL_TT,Time_Taken);
        contentValues.put(COL_AVGSPEED,Avg_Speed);
        contentValues.put(COL_TOPSPEED,Top_Speed);
        contentValues.put(COL_CURLEV,cur_lev);
        contentValues.put(COL_FUELCONSUMED,Fuel_consumed);
        contentValues.put(COL_FUELAVG,Fuel_avg);
        long result = db.insert(Results_Table,null,contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }

    public Cursor getResCursor()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+Results_Table,null);

        return res;
    }

    public Cursor getLastRes()
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+Results_Table+" ORDER BY ID DESC LIMIT 1",null);

        return res;
    }
}
