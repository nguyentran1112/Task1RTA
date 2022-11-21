package com.example.androidassignment12015.sqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.androidassignment12015.Model.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemDao {
    private SQLiteDatabase db;
    private static final String TABLE_NAME = "Items";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_IS_IMPORTED = "is_imported";
    DatabaseHelper dbHelper;

    public ItemDao(Context context) {dbHelper = new DatabaseHelper(context);

    }

    public void addItem(Item item) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, item.getId());
        values.put(KEY_NAME, item.getName());
        values.put(KEY_LOCATION, item.getLocation());
        values.put(KEY_IS_IMPORTED, item.isImported());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
//    public boolean check(int itemId) {
//        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(itemId) },null, null, null);
//        if(cursor != null) {
//            cursor.moveToFirst();
//            return true;
//        }
//        else {
//            return false;
//        }
//    }
    public int CheckIsDataAlreadyInDBorNot(int itemId) {
        int count = 0;

        db = dbHelper.getReadableDatabase();
        String Query = "Select * from " + TABLE_NAME + " where " + "id" + " = " + itemId;
        Cursor cursor = db.rawQuery(Query, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }


    public List<Item> getAllItems() {
        db = dbHelper.getWritableDatabase();
        List<Item>  itemList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Item item = new Item(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3));
            itemList.add(item);
            cursor.moveToNext();
        }
        return itemList;
    }

    public void updateItem(Item item) {
        db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, item.getName());
        values.put(KEY_LOCATION, item.getLocation());
        values.put(KEY_IS_IMPORTED, item.isImported());
        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(item.getId()) });
        db.close();
    }

    public void deleteItem(int itemId) {
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(itemId) });
        db.close();
    }
}
