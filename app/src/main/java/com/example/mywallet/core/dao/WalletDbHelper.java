package com.example.mywallet.core.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mywallet.account.dao.AccountContract;
import com.example.mywallet.transaction.dao.TransactionContract;

public class WalletDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "MyWallet.db";

    public WalletDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(AccountContract.SQL_CREATE_TACCOUNT);
        db.execSQL(TransactionContract.SQL_CREATE_TTRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(AccountContract.SQL_DELETE_TACCOUNT);
        db.execSQL(TransactionContract.SQL_DELETE_TTRANSACTION);
        onCreate(db);
    }
}
