package com.example.mywallet.account.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public long insert(WalletDbHelper dbh, Account account) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountContract.COLUMN_NAME, account.getName());
        values.put(AccountContract.COLUMN_AMOUNT, account.getAmount());
        long newRowId = db.insert(AccountContract.TABLE_NAME, null, values);
        return newRowId;
    }

    public int update(WalletDbHelper dbh, int id, Account account) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(AccountContract.COLUMN_NAME, account.getName());
        values.put(AccountContract.COLUMN_AMOUNT, account.getAmount());
        int count = db.update(AccountContract.TABLE_NAME, values,
                AccountContract.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        return count;
    }

    public List<Account> findAll(WalletDbHelper dbh) {
        SQLiteDatabase db = dbh.getReadableDatabase();
        String[] projection = {
                AccountContract.COLUMN_ID,
                AccountContract.COLUMN_NAME,
                AccountContract.COLUMN_AMOUNT
        };

        String sortOrder = AccountContract.COLUMN_ID + " ASC";

        Cursor cursor = db.query(AccountContract.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder);

        List<Account> list = new ArrayList<>();
        while(cursor.moveToNext()) {
            Account a = new Account(
                    cursor.getInt(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_AMOUNT))
            );
            list.add(a);
        }
        return list;
    }

    public int delete (WalletDbHelper dbh, int id) {
        SQLiteDatabase db = dbh.getWritableDatabase();
        int deleted = db.delete(AccountContract.TABLE_NAME,
                AccountContract.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)});
        return deleted;
    }
}