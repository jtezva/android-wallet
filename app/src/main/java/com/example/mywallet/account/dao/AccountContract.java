package com.example.mywallet.account.dao;

public interface AccountContract {

    public static final String SQL_CREATE_TACCOUNT = "CREATE TABLE " +
            AccountContract.TABLE_NAME + " ( " +
            AccountContract.COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            AccountContract.COLUMN_NAME_NAME + " TEXT, " +
            AccountContract.COLUMN_NAME_AMOUNT + " REAL "
            + ") ";

    public static final String SQL_DELETE_TACCOUNT = "DROP TABLE IF EXISTS " + AccountContract.TABLE_NAME;

    public static final String TABLE_NAME = "TACCOUNT";
    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_NAME = "NAME";
    public static final String COLUMN_NAME_AMOUNT = "AMOUNT";
}