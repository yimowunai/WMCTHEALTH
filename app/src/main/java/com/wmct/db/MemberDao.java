package com.wmct.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wmct.bean.Family;
import com.wmct.bean.Member;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by little grey on 2016/1/15.
 */
public class MemberDao {
    private DataBaseHelper dbHelper;

    public MemberDao(Context context) {
        dbHelper = new DataBaseHelper(context);
    }

    public void insert(Member member) {  //添加member信息
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memberid", member.getMemberid());
        values.put("familyPhone", member.getFamilyPhone());
        values.put("membername", member.getMembername());
        values.put("age", member.getAge());
        values.put("gender", member.getGender());
        values.put("image", member.getImage());
        database.insert("member", null, values);
        database.close();
    }

    public void delete(Member member) {   //删除不想要的member
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        String[] whereArgs = {String.valueOf(member.getMemberid())};
        database.delete("member", "memberid = ?", whereArgs);
        database.close();
    }

    public void update(Member member) {  //修改member信息
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("membername", member.getMembername());
        values.put("age", member.getAge());
        values.put("gender", member.getGender());
        values.put("image", member.getImage());
        String[] wherArgs = {String.valueOf(member.getMemberid())};
        database.update("member", values, "memberid = ?", wherArgs);
        database.close();
    }

    public Member query(long memberid) {  //查询单个member的信息
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] whereArgs = {String.valueOf(memberid)};
        Cursor cursor = database.query("member", null, "memberid = ?", whereArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getInt(cursor.getColumnIndex("id"));
            long member_id = cursor.getInt(cursor.getColumnIndex("memberid"));
            String familyPhone = cursor.getString(cursor.getColumnIndex("familyPhone"));
            String membername = cursor.getString(cursor.getColumnIndex("membername"));
            int age = cursor.getInt(cursor.getColumnIndex("age"));
            int gender = cursor.getInt(cursor.getColumnIndex("gender"));
            String image = cursor.getString(cursor.getColumnIndex("image"));

            Member qmember = new Member(id, member_id, familyPhone, membername, age, gender, image);
            return qmember;
        }
        cursor.close();
        database.close();
        return null;
    }

    public List<Member> queryAll(Family family) {  //查询同一family的所有member的信息，返回List
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        String[] whereArgs = {family.getPhone()};
        Cursor cursor = database.query("member", null, "familyPhone = ?", whereArgs, null, null, null);
        if (cursor != null && cursor.getCount() > 0) {
            List<Member> memberList = new ArrayList<>();
            while (cursor.moveToNext()) {
                long id = cursor.getInt(cursor.getColumnIndex("id"));
                long member_id = cursor.getInt(cursor.getColumnIndex("memberid"));
                String familyPhone = cursor.getString(cursor.getColumnIndex("familyPhone"));
                String membername = cursor.getString(cursor.getColumnIndex("membername"));
                int age = cursor.getInt(cursor.getColumnIndex("age"));
                int gender = cursor.getInt(cursor.getColumnIndex("gender"));
                String image = cursor.getString(cursor.getColumnIndex("image"));

                Member qallmember = new Member(id, member_id, familyPhone, membername, age, gender, image);
                memberList.add(qallmember);
            }
            cursor.close();
            database.close();
            return memberList;
        }
        cursor.close();
        database.close();
        return null;
    }
}
