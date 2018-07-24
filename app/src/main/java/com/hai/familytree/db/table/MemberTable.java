package com.hai.familytree.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hai.familytree.db.DatabaseManager;
import com.hai.familytree.model.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hai on 18/07/2018.
 */

public class MemberTable {
    public static final String TABLE_NAME = "member";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ICON = "icon";
    public static final String COLUMN_GENDER = "gender";
    public static final String COLUMN_FATHER = "father";
    public static final String COLUMN_MOTHER = "MOTHER";
    public static final String COLUMN_COUPLE = "couple";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NAME + " TEXT,"
                    + COLUMN_ICON + " INTEGER,"
                    + COLUMN_GENDER + " INTEGER DEFAULT 0,"
                    + COLUMN_FATHER + " INTEGER,"
                    + COLUMN_MOTHER + " INTEGER,"
                    + COLUMN_COUPLE + " INTEGER"
                    + ")";
    public int insertMember(Member member, DatabaseManager data) {
        SQLiteDatabase db = data.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, member.getName());
        values.put(COLUMN_ICON, member.getIcon());
        values.put(COLUMN_GENDER, member.getGender());
        values.put(COLUMN_FATHER, member.getFatherId());
        values.put(COLUMN_MOTHER, member.getMotherId());
        values.put(COLUMN_COUPLE, member.getCoupleId());
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return (int) id;
    }
    public List<Member> getAllMembers(DatabaseManager databaseManager) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;

        SQLiteDatabase db = databaseManager.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        List<Member> members = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Member member = new Member();
                member.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
                member.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                member.setIcon(cursor.getInt(cursor.getColumnIndex(COLUMN_ICON)));
                member.setGender(cursor.getInt(cursor.getColumnIndex(COLUMN_GENDER)));
                member.setFatherId(cursor.getInt(cursor.getColumnIndex(COLUMN_FATHER)));
                member.setMotherId(cursor.getInt(cursor.getColumnIndex(COLUMN_MOTHER)));
                member.setCoupleId(cursor.getInt(cursor.getColumnIndex(COLUMN_COUPLE)));
                Log.d("membersList",member.getName());
                members.add(member);
            } while (cursor.moveToNext());
        }

        db.close();

        return members;
    }
    public void deleteNote(Member member,DatabaseManager databaseManager) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?",
                new String[]{String.valueOf(member.getId())});
        db.close();
    }
    public void updateMember(Member member,DatabaseManager databaseManager) {
        SQLiteDatabase db = databaseManager.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME,member.getName());
        values.put(COLUMN_ICON, member.getIcon());
        values.put(COLUMN_GENDER, member.getGender());
        values.put(COLUMN_FATHER, member.getFatherId());
        values.put(COLUMN_MOTHER, member.getMotherId());
        values.put(COLUMN_GENDER,member.getGender());
        values.put(COLUMN_COUPLE,member.getCoupleId());

        // updating row
        int id =  db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(member.getId())});
    }

}
