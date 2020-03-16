package com.example.mywallet.account.dao;

public interface AccountContract {
    String SQL_CREATE_TACCOUNT = "CREATE TABLE " +
            AccountContract.TABLE_NAME + " ( " +
            AccountContract.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AccountContract.COLUMN_NAME + " TEXT, " +
            AccountContract.COLUMN_AMOUNT + " REAL "
            + ") ";

    String SQL_DELETE_TACCOUNT = "DROP TABLE IF EXISTS " + AccountContract.TABLE_NAME;

    String TABLE_NAME = "TACCOUNT";
    String COLUMN_ID = "ID";
    String COLUMN_NAME = "NAME";
    String COLUMN_AMOUNT = "AMOUNT";
}