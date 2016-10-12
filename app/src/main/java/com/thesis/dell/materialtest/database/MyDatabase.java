package com.thesis.dell.materialtest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.thesis.dell.materialtest.pojo.Datastream;
import com.thesis.dell.materialtest.log.L;

import java.util.ArrayList;
import java.util.Date;

import static com.thesis.dell.materialtest.MyApplication.getAppContext;

/**
 * Created by DellMain on 14.09.2015.
 */
public class MyDatabase {

//    SQLiteStatement statement = new SQLiteStatement(this, sql.toString(), bindArgs);
//    try {
//        return statement.executeInsert();
//    } finally {
//        statement.close();
//    }

    private MyHelper mHelper;
    private SQLiteDatabase mDatabase;

    public MyDatabase(Context context) {
        mHelper = new MyHelper(context);
        mDatabase = mHelper.getWritableDatabase();
    }

    public void insertValues(ArrayList<Datastream> listValues, boolean clearPrevious) {
        if (clearPrevious) {
            deleteData();
        }
        //beginTransaction(null /* transactionStatusCallback */, true); allowed to comment like this?
        //create a sql prepared statement
        String sql = "INSERT INTO " + MyHelper.TABLE_NAME + " VALUES (?,?,?);";
        //compile the statement and start a transaction
        SQLiteStatement statement = mDatabase.compileStatement(sql);
        mDatabase.beginTransaction();
        for (int i = 0; i < listValues.size(); i++) {
            Datastream currentData = listValues.get(i);
            statement.clearBindings();
            //for a given column index, simply bind the data to be put inside that index
            statement.bindString(2, currentData.getCurrent_value());
            statement.bindLong(3, currentData.getCurrent_valueAt() == null ? -1 : currentData.getCurrent_valueAt().getTime());
            L.m("Inserting entry " +i);
            statement.execute();
        }
        //set the transaction as successful and end the transaction
        //L.m("inserting entries " + listValues.size() + new Date(System.currentTimeMillis()));
        mDatabase.setTransactionSuccessful();
        mDatabase.endTransaction();
    }

    /**
     * 1 get the number of elements in the array
     * 2 return the number
     * 3 use that number in for loop to go through all elements in the List
     * 4 print out the list
    **/

    public int getTotalNumberOfElements() {

        int numberOfElements = -1;
        //get a list of columns to be retrieved, we need all of them
        String[] columns = {MyHelper.COLUMN_VALUE, MyHelper.COLUMN_DATE_AT};
        //make a simple query with dbname, columns and all other params as null
        Cursor cursor = mDatabase.query(MyHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            numberOfElements = cursor.getCount();
            return numberOfElements;
        }
        return numberOfElements;
    }
    public ArrayList<Datastream> readDatastreams()
    {
        //init an empty array list
        ArrayList<Datastream> listDatastream = new ArrayList<>();

        //get a list of columns to be retrieved, we need all of them
        String[] columns = {MyHelper.COLUMN_VALUE, MyHelper.COLUMN_DATE_AT};
        //make a simple query with dbname, columns and all other params as null
        Cursor cursor = mDatabase.query(MyHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            //L.m(" loading entries " + cursor.getCount() + new Date(System.currentTimeMillis()));
            do {

                //create a new Datastream object and retrieve the data from the cursor to be stored in this Datastream object
                Datastream datastream = new Datastream();
                //each step is a 2 part process, find the index of the column first, find the data of that column using
                //that index and finally set our blank Datastream object to contain our data
                datastream.setCurrent_value(cursor.getString(cursor.getColumnIndex(MyHelper.COLUMN_VALUE)));
                long valueAtMilliseconds = cursor.getLong(cursor.getColumnIndex(MyHelper.COLUMN_DATE_AT));
                datastream.setCurrent_valueAt(valueAtMilliseconds != -1 ? new Date(valueAtMilliseconds) : null);
                //add the datastream to the list of datastream objects which we plan to return
                L.m("Getting datastream object " + datastream.getCurrent_value() + " - "
                        + datastream.getCurrent_valueAt());
                listDatastream.add(datastream);
            }
            while (cursor.moveToNext());
        }

        //Nothing is added`?
        //L.t(getAppContext(), listDatastream.get(0).getCurrent_value().toString() + "-" +
        //        listDatastream.get(0).getCurrent_valueAt().toString());
        return listDatastream;
    }

    //delete all rows from our table
    public void deleteData() {
        mDatabase.delete(MyHelper.TABLE_NAME, null, null);
    }

    public long insertData(String value, String valueDate) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(MyHelper.COLUMN_VALUE, value);
        cv.put(MyHelper.COLUMN_DATE_AT, valueDate);
        //ID indicates the row id that was inserted if successfull
        long id = db.insert(MyHelper.TABLE_NAME, null, cv);
        return id;
    }

    public String getColumnValueLast() {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        //select COLUMN_VALUE from TESTTABLE
        String[] columns = {MyHelper.COLUMN_VALUE};
        Cursor cursor = db.query(MyHelper.TABLE_NAME, columns, null, null, null, null, null );

//        StringBuffer buffer = new StringBuffer();
//        while (cursor.moveToNext()) {
//            int index1 = cursor.getColumnIndex(MyHelper.COLUMN_VALUE);
//            String value = cursor.getString(index1);
//            buffer.append(value);
//        }
//        return buffer.toString();

        String lastValue = "-1";
        if (cursor.moveToLast()) {
            int index1 = cursor.getColumnIndex(MyHelper.COLUMN_VALUE);
            String value = cursor.getString(index1);
            lastValue = value;
        }
        return lastValue;
    }

    static class MyHelper extends SQLiteOpenHelper {

        private static final String DB_NAME = "MyDB";
        private static final String TABLE_NAME = "TableValue";
        private static final int DB_VERSION = 2;
        private static final String COLUMN_UID = "_id";
        private static final String COLUMN_VALUE = "current_value";
        private static final String COLUMN_DATE_AT = "at";
        private static final String CREATE_TABLE = "CREATE TABLE "
                + TABLE_NAME
                + " ("
                + COLUMN_UID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_VALUE
                + " VARCHAR(100), "
                + COLUMN_DATE_AT
                + " VARCHAR(100));";
        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        private Context context; //mContext;

        public MyHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
            // or mContext = context
            this.context = context;
            //L.t(context, "constructor called");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
                //L.t(context, "onCreate called");
            } catch (SQLException e) {
                L.t(context, "" + e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
/*            if(newVersion>oldVersion)
            {
                //copyDatabase();
            }*/

            try {
                //L.t(context, "onUpgrade called");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            } catch (SQLException e) {
                L.t(context, "" + e);
            }

        }
    }
}
