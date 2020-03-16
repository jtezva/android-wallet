package com.example.mywallet.account.service;

import android.content.Context;
import android.util.Log;

import com.example.mywallet.account.dao.AccountDao;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;
import com.example.mywallet.util.Utils;

import java.util.List;

public class AccountService {
    private Context context;
    private AccountDao dao;

    public AccountService(Context context) {
        this.context = context;
        this.dao = new AccountDao();
    }

    public List<Account> loadAccountList() {
        Log.d("AccountService","loadAccountList");
        List<Account> list = null;
        WalletDbHelper dbh = new WalletDbHelper(this.context);
        try {
            list = this.dao.findAll(dbh);
        } catch (Exception e) {
            Utils.raise("Loading accounts", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        return list;
    }

    public void deleteAccountById (int id) {
        WalletDbHelper dbh = new WalletDbHelper(this.context);
        try {
            int deletedRows = this.dao.delete(dbh, id);
            if (deletedRows < 1) {
                Utils.exception("Delete failed");
            }
        } catch (Exception e) {
            Utils.raise("Deleting account", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
    }

    public void insertAccount (Account account) {
        WalletDbHelper dbh = new WalletDbHelper(this.context);
        try {
            this.dao.insert(dbh, account);
        } catch (Exception e) {
            Utils.raise("Adding account", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
    }

    public void updateAccount (int id, Account account) {
        WalletDbHelper dbh = new WalletDbHelper(this.context);
        try {
            int updatedRows = this.dao.update(dbh, id, account);
            if (updatedRows < 1) {
                Utils.exception("Update failed");
            }
        } catch (Exception e) {
            Utils.raise("Updating account", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
    }
}