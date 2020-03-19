package com.example.mywallet.transaction.dao;

public interface TransactionContract {
    String SQL_CREATE_TTRANSACTION = "CREATE TABLE " +
            TransactionContract.TABLE_NAME + " ( " +
            TransactionContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TransactionContract.COLUMN_AMOUNT   + " REAL, " +
            TransactionContract.COLUMN_CONCEPT  + " TEXT, " +
            TransactionContract.COLUMN_ACCOUNT  + " INTEGER, " +
            TransactionContract.COLUMN_TRANSFER + " INTEGER, " +
            TransactionContract.COLUMN_STAMP    + " TEXT"
            + ") ";

    String SQL_DELETE_TTRANSACTION = "DROP TABLE IF EXISTS " + TransactionContract.TABLE_NAME;

    String TABLE_NAME = "TTRANSACTION";
    String COLUMN_ID = "ID";
    String COLUMN_AMOUNT = "AMOUNT";
    String COLUMN_CONCEPT = "CONCEPT";
    String COLUMN_ACCOUNT = "ACCOUNT";
    String COLUMN_TRANSFER = "TRANSFER";
    String COLUMN_STAMP = "STAMP";
}