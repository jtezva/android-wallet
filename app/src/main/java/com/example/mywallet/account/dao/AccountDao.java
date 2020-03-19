package com.example.mywallet.account.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;
import com.example.mywallet.core.exception.WalletException;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccountDao {

    public long insert(WalletDbHelper dbh, Account account) throws WalletException {
        long newRowId = 0l;
        SQLiteDatabase db = null;
        try {
            db = dbh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(AccountContract.COLUMN_NAME, account.getName());
            values.put(AccountContract.COLUMN_AMOUNT, account.getAmount());
            newRowId = db.insert(AccountContract.TABLE_NAME, null, values);
        } catch (Exception e) {
            Utils.raise("Saving new account", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return newRowId;
    }

    public int update(WalletDbHelper dbh, int id, Account account) throws WalletException {
        int count = 0;
        SQLiteDatabase db = null;
        try {
            db = dbh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(AccountContract.COLUMN_NAME, account.getName());
            values.put(AccountContract.COLUMN_AMOUNT, account.getAmount());
            count = db.update(AccountContract.TABLE_NAME, values,
                    AccountContract.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Utils.raise("Updating account", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return count;
    }

    public List<Account> findAll(WalletDbHelper dbh) throws WalletException {
        List<Account> list = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbh.getReadableDatabase();
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

            while (cursor.moveToNext()) {
                Account a = new Account(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_AMOUNT))
                );
                list.add(a);
            }
        } catch (Exception e) {
            Utils.raise("Loading account list", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    public int delete (WalletDbHelper dbh, int id) throws WalletException {
        int deleted = 0;
        SQLiteDatabase db = null;
        try {
            db = dbh.getWritableDatabase();
            deleted = db.delete(AccountContract.TABLE_NAME,
                    AccountContract.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Utils.raise("Deleting account", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return deleted;
    }

    public Account getById(WalletDbHelper dbh, int id) throws WalletException {
        Account account = null;
        SQLiteDatabase db = null;
        try {
            db = dbh.getReadableDatabase();
            String[] projection = {
                    AccountContract.COLUMN_ID,
                    AccountContract.COLUMN_NAME,
                    AccountContract.COLUMN_AMOUNT
            };

            String sortOrder = AccountContract.COLUMN_ID + " ASC";

            Cursor cursor = db.query(AccountContract.TABLE_NAME,
                    projection,
                    AccountContract.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)},
                    null,
                    null,
                    sortOrder);

            if (cursor.moveToNext()) {
                account = new Account(
                        cursor.getInt(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(AccountContract.COLUMN_AMOUNT))
                );
            }

            if (account == null) {
                Utils.exception("Account with id " + id + " not found");
            }
        } catch (Exception e) {
            Utils.raise("Loading account by id", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return account;
    }
}