package com.example.mywallet.account.service;

import android.content.Context;
import android.util.Log;

import com.example.mywallet.account.dao.AccountDao;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;
import com.example.mywallet.transaction.dao.TransactionDao;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.util.Utils;

import java.util.List;

public class AccountService {
    private Context context;
    private AccountDao dao;
    private TransactionDao transactionDao;

    public AccountService(Context context) {
        this.context = context;
        this.dao = new AccountDao();
        this.transactionDao = new TransactionDao();
    }

    public List<Account> loadAccountList() {
        Log.d("AccountService","loadAccountList >");
        List<Account> list = null;
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
            list = this.dao.findAll(dbh);
        } catch (Exception e) {
            Utils.raise("Loading accounts", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        Log.d("AccountService","loadAccountList < items: " + list.size());
        return list;
    }

    public void deleteAccountById (int id) {
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
            List<Transaction> transactions = transactionDao.findAllByAccountOrTransfer(dbh, id);
            if (transactions != null && transactions.size() > 0) {
                Utils.exception("Can not delete if it has transactions");
            }

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
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
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
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
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