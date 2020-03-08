package com.example.mywallet.account.service;

import android.content.Context;
import android.util.Log;

import com.example.mywallet.account.dao.AccountDao;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;

import java.util.List;

public class AccountService {
    private Context context;
    private AccountDao dao;

    public AccountService(Context context) {
        this.context = context;
        this.dao = new AccountDao();
    }

    public void insertDummyData() {
        Log.d("AccountService","insertDummyData");
        WalletDbHelper dbh = new WalletDbHelper(this.context);
        try {
            Account a = new Account(0, "Inserted " + System.currentTimeMillis(), Math.random());
            long aId = this.dao.insert(dbh, a);
            Log.d("AccountService.insertDummyData", "Inserted: " + aId);
        } catch (Exception e) {
            Log.e("AccountService.insertDummyData", e.getMessage());
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
    }

    public List<Account> loadAccountList() {
        Log.d("AccountService","loadAccountList");
        List<Account> list = null;
        WalletDbHelper dbh = new WalletDbHelper(this.context);
        try {
            list = this.dao.findAll(dbh);
        } catch (Exception e) {
            Log.e("AccountService.loadAccountList", e.getMessage());
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        return list;
    }
}