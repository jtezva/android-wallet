package com.example.mywallet.transaction.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mywallet.account.dao.AccountContract;
import com.example.mywallet.core.dao.WalletDbHelper;
import com.example.mywallet.core.exception.WalletException;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TransactionDao {

    public long insert(WalletDbHelper dbh, Transaction transaction) throws WalletException {
        long newRowId = 0l;
        SQLiteDatabase db = null;
        try {
            db = dbh.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(TransactionContract.COLUMN_AMOUNT, transaction.getAmount());
            values.put(TransactionContract.COLUMN_CONCEPT, transaction.getConcept());
            values.put(TransactionContract.COLUMN_ACCOUNT, transaction.getAccount());
            values.put(TransactionContract.COLUMN_TRANSFER, transaction.getTransferto());
            values.put(TransactionContract.COLUMN_STAMP, transaction.getStamp());
            newRowId = db.insert(TransactionContract.TABLE_NAME, null, values);
        } catch (Exception e) {
            Utils.raise("Saving new transaction", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return newRowId;
    }

    public List<Transaction> findAllByAccountOrTransfer(WalletDbHelper dbh, int accountId)
            throws WalletException {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = null;
        try {
            db = dbh.getReadableDatabase();
            String[] projection = {
                    TransactionContract.COLUMN_ID,
                    TransactionContract.COLUMN_AMOUNT,
                    TransactionContract.COLUMN_CONCEPT,
                    TransactionContract.COLUMN_ACCOUNT,
                    TransactionContract.COLUMN_TRANSFER,
                    TransactionContract.COLUMN_STAMP
            };

            String sortOrder = TransactionContract.COLUMN_STAMP + " DESC";

            Cursor cursor = db.query(TransactionContract.TABLE_NAME,
                    projection,
                    TransactionContract.COLUMN_ACCOUNT + "=? or " + TransactionContract.COLUMN_TRANSFER + "=?",
                    new String[]{String.valueOf(accountId), String.valueOf(accountId)},
                    null,
                    null,
                    sortOrder);

            while (cursor.moveToNext()) {
                Transaction t = new Transaction(
                        cursor.getInt(cursor.getColumnIndexOrThrow(TransactionContract.COLUMN_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(TransactionContract.COLUMN_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.COLUMN_CONCEPT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TransactionContract.COLUMN_ACCOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(TransactionContract.COLUMN_TRANSFER)),
                        cursor.getString(cursor.getColumnIndexOrThrow(TransactionContract.COLUMN_STAMP))
                );
                list.add(t);
            }
        } catch (Exception e) {
            Utils.raise("Loading transaction list", e);
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
            deleted = db.delete(TransactionContract.TABLE_NAME,
                    TransactionContract.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)});
        } catch (Exception e) {
            Utils.raise("Deleting transaction", e);
        } finally {
            if (db != null) {
                db.close();
            }
        }
        return deleted;
    }
}