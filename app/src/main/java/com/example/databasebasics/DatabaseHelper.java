package com.example.databasebasics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String CUSTOMER_TABLE = "CUSTOMER_TABLE";
    public static final String COLUMN_CUSTOMER_NAME = "CUSTOMER_NAME";
    public static final String COLUMN_CUSTOMER_AGE = "CUSTOMER_AGE";
    public static final String COLUMN_ACTIVE_CUSTOMER = "ACTIVE_CUSTOMER";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "customer.db", null, 1);
    }

    // THIS IS CALLED THE FIRST TIME A DATABASE IS ACCESSED. so here should be code to create a new database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + CUSTOMER_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_CUSTOMER_NAME + " TEXT, " + COLUMN_CUSTOMER_AGE + " INT, " + COLUMN_ACTIVE_CUSTOMER + " BOOL)";

        db.execSQL(createTableStatement);

    }

    //this is called if the database version number changes. it prevents previous users from breaking when you change the database design

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addOne(CustomerModel customerModel) {

        SQLiteDatabase db = this.getWritableDatabase();//for inserting actions
        ContentValues cv = new ContentValues(); // content values work like Hashmaps and store data in pairs
        cv.put(COLUMN_CUSTOMER_NAME, customerModel.getName());
        cv.put(COLUMN_CUSTOMER_AGE, customerModel.getAge());
        cv.put(COLUMN_ACTIVE_CUSTOMER, customerModel.isActive());

        long insert = db.insert(CUSTOMER_TABLE, null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }

    public boolean deleteOne(CustomerModel customerModel) {
        // find customerModel in database.  if it found, delete it and return true.
        // if it is not found, return false

        SQLiteDatabase db = this.getWritableDatabase();
        String queryString = "DELETE FROM " + CUSTOMER_TABLE + " WHERE " + COLUMN_ID + " = " + customerModel.getId();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            return true;
        }else {
            return false;

        }


    }

    //Create a method that will select all records from the table.
    public List<CustomerModel> getEveryone() {
        //To generate a returned list we have to define it first
        // 1. Create an empty list 2. Fill it from the database query 3. Return it to the MainActivity
        List<CustomerModel> returnList = new ArrayList<>();

        //get data from the database
        String queryString = "SELECT * FROM " + CUSTOMER_TABLE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(queryString, null); //Cursor is result set(complex arrays of items means rows of rows of data if lots of people selected) from a SQL statement

        if (cursor.moveToFirst()) {

            // loop through the cursor and create new customer obj for each row. put them into the return list
            do {
                //introduce new local variables of the type that we expect to come from database
                int customerID = cursor.getInt(0);
                String customerName = cursor.getString(1);
                int customerAge = cursor.getInt(2);
                boolean customerActive = cursor.getInt(3) == 1 ? true : false;

                CustomerModel newCustomer = new CustomerModel(customerID, customerName, customerAge, customerActive);
                returnList.add(newCustomer);

            } while (cursor.moveToNext());


        } else {
            //failure, do not add anyting to the list.

        }
        // close both the cursor and db when done.
        cursor.close();
        db.close();

        return returnList;


    }
}
