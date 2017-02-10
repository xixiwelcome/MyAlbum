package com.cc.milkalbum.dao;

import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.cc.milkalbum.model.Girl;

import java.util.ArrayList;
import java.util.List;

public class GirlDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "girls.db";//数据库名称
    private static final String TABLE_FAVORITE_GIRLS = "favoriteGirls";
    private static final int SCHEMA_VERSION = 1;//版本号,则是升级之后的,升级方法请看onUpgrade方法里面的判断

    public GirlDBHelper(Context context) {//构造函数,接收上下文作为参数,直接调用的父类的构造函数
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_FAVORITE_GIRLS + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, authorName TEXT, " +
                "authorHeadImg TEXT, catalog TEXT, title TEXT, issue TEXT, pictureCount INT, " +
                "createTime TIMESTAMP default (datetime('now', 'localtime')));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public List<Girl> getAll(String where, String orderBy) {//返回表中的数据, 默认创建时间排序
        List<Girl> girls = new ArrayList<Girl>();
        StringBuilder buf = new StringBuilder("SELECT * FROM " + TABLE_FAVORITE_GIRLS);

        if (where!=null) {
            buf.append(" WHERE ");
            buf.append(where);
        }

        if (orderBy!=null) {
            buf.append(" ORDER BY ");
            buf.append(orderBy);
        } else {
            buf.append(" ORDER BY createTime desc");
        }

        Cursor c = getReadableDatabase().rawQuery(buf.toString(), null);

        if(c != null && c.moveToFirst()) {
            do {
                girls.add(getGirl(c));
            }while (c.moveToNext());
        }
        return girls;
    }

    public Girl getById(int id) {
        Log.d("cbx", "查询, id: " + id);
        String[] args={id+""};

        Cursor c = getReadableDatabase()
                .rawQuery("SELECT * FROM " + TABLE_FAVORITE_GIRLS + " WHERE _id=?",
                        args);
        if(c != null && c.moveToFirst()){
            Girl girl = getGirl(c);
            Log.d("cbx", "查询结果: " + girl.toString());
            return girl;
        }
        Log.w("cbx", "查询结果: null");
        return null;
    }

    public void insert(Girl girl) {
        Log.d("cbx", "插入->" + girl.toString());
        Girl g = getById(girl.getId());
        if(g != null) {
            Log.w("cbx", "已存在！");
            return;
        }

        ContentValues cv=new ContentValues();

        cv.put("_id", girl.getId());
        cv.put("authorName", girl.getAuthorName());
        cv.put("authorHeadImg", girl.getAuthorHeadImg());
        cv.put("catalog", girl.getCatalog());
        cv.put("title", girl.getTitle());
        cv.put("issue", girl.getIssue());
        cv.put("pictureCount", girl.getPictureCount());

        getWritableDatabase().insert(TABLE_FAVORITE_GIRLS, "authorName", cv);
        Log.d("cbx", "插入成功！");
    }

    public void delete(int id) {
        Log.d("cbx", "删除, id: " + id);
        getWritableDatabase().delete(TABLE_FAVORITE_GIRLS, "_id=?", new String[]{id + ""});
        Log.d("cbx", "删除成功！");
    }

    public Girl getGirl(Cursor c) {

        if(c != null) {
            return new Girl(getId(c), getAuthorName(c),
                    getAuthorHeadImg(c), getTitle(c),
                    getCatalog(c), getIssue(c), getPictureCount(c));
        }
        return null;
    }

    public int getId(Cursor c) {
        return(c.getInt(0));
    }

    public String getAuthorName(Cursor c) {
        return(c.getString(1));
    }

    public String getAuthorHeadImg(Cursor c) {
        return(c.getString(2));
    }

    public String getCatalog(Cursor c) {
        return(c.getString(3));
    }

    public String getTitle(Cursor c) {
        return(c.getString(4));
    }

    public String getIssue(Cursor c) {
        return(c.getString(5));
    }

    public int getPictureCount(Cursor c) {
        return(c.getInt(6));
    }
}