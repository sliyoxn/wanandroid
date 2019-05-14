package com.example.sorena.wanandroidapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.sorena.wanandroidapp.bean.History;
import com.example.sorena.wanandroidapp.util.LogUtil;
import com.example.sorena.wanandroidapp.util.MyApplication;

import java.util.LinkedList;
import java.util.List;

public class SearchHistoryDataBaseOperator
{
    private static SearchHistoryDataBaseOperator operator;
    private static SearchHistoryDataBaseHelper helper;
    private SearchHistoryDataBaseOperator(){}

    static {
        operator = new SearchHistoryDataBaseOperator();
        helper = new SearchHistoryDataBaseHelper(MyApplication.getContext(),"History.db",null,1);
    }

    public void addData(String newWord){
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("word",newWord);
        db.insert("SearchHistory",null,values);
    }

    public void delData(String word){

        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("SearchHistory","word = ?",new String[]{word});
    }

    public void delData(int id){
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("SearchHistory","id = ?",new String[]{Integer.toString(id)});
    }

    public List<History> getDataList(){
        SQLiteDatabase db = helper.getWritableDatabase();
        List<History> histories = new LinkedList<>();
        Cursor cursor = db.query("SearchHistory",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                String word = cursor.getString(cursor.getColumnIndex("word"));
                histories.add(new History(id,word));
            }while (cursor.moveToNext());
        }else {
            LogUtil.d("日志:数据库","SearchHistory表为空");
        }
        cursor.close();
        return histories;
    }


    public static SearchHistoryDataBaseOperator getInstance(){
        return operator;
    }


    public void clearData() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("SearchHistory","1",null);
    }
}
