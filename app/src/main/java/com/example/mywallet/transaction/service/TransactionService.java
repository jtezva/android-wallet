package com.example.mywallet.transaction.service;

import android.content.Context;
import android.util.Log;

import com.example.mywallet.account.dao.AccountDao;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.dao.WalletDbHelper;
import com.example.mywallet.core.exception.WalletException;
import com.example.mywallet.transaction.dao.TransactionDao;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.util.Utils;

import java.util.List;

public class TransactionService {

    private Context context;
    private AccountDao accountDao;
    private TransactionDao dao;

    public TransactionService(Context context) {
        this.context = context;
        this.accountDao = new AccountDao();
        this.dao = new TransactionDao();
    }

    public long registerTransaction(Transaction transaction) throws WalletException {
        Log.d("TransactionService", "registerTransaction > " + transaction.toPipes());
        long id = 0l;
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
            id = dao.insert(dbh, transaction);
            if (id < 1l) {
                Utils.exception("Transaction not inserted");
            }

            Account account = accountDao.getById(dbh, transaction.getAccount());
            account.setAmount(account.getAmount() + transaction.getAmount());
            int updated = accountDao.update(dbh, account.getId(), account);
            if (updated == 0) {
                Utils.exception("Account not updated");
            }
        } catch (Exception e) {
            Utils.raise("Saving new transaction", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        Log.d("TransactionService", "registerTransaction < id: " + id);
        return id;
    }

    public long registerTransference(Transaction transaction) throws WalletException {
        Log.d("TransactionService", "registerTransference > " + transaction.toPipes());
        long id = 0l;
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
            Double amount = Math.abs(transaction.getAmount());
            transaction.setAmount(amount);

            // insert transaction
            id = dao.insert(dbh, transaction);
            if (id < 1l) {
                Utils.exception("Transference not inserted");
            }

            Account accountFrom = accountDao.getById(dbh, transaction.getAccount());
            Account accountTo = accountDao.getById(dbh, transaction.getTransferto());

            accountFrom.setAmount(accountFrom.getAmount() - amount);
            int updated = accountDao.update(dbh, accountFrom.getId(), accountFrom);
            if (updated == 0) {
                Utils.exception("Origin account not updated");
            }

            accountTo.setAmount(accountTo.getAmount() + amount);
            updated = accountDao.update(dbh, accountTo.getId(), accountTo);
            if (updated == 0) {
                Utils.exception("Target account not updated");
            }
        } catch (Exception e) {
            Utils.raise("Saving new transference", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        Log.d("TransactionService", "registerTransference < id: " + id);
        return id;
    }

    public List<Transaction> loadTransactionList(int accountId) throws WalletException {
        Log.d("TransactionService","loadTransactionList >");
        List<Transaction> list = null;
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);
            list = this.dao.findAllByAccountOrTransfer(dbh, accountId);
        } catch (Exception e) {
            Utils.raise("Loading transactions", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        Log.d("TransactionService","loadTransactionList < items: " + list.size());
        return list;
    }

    public void updateTransaction(int id, Transaction transaction) {
        Log.d("TransactionService", "updateTransaction > " + id + ", " +
                transaction.toPipes());
    }

    public void updateTransference(int id, Transaction transaction) {
        Log.d("TransactionService", "updateTransference > " + id + ", " +
                transaction.toPipes());
    }

    public void deleteTransaction(Transaction transaction, Account account) throws WalletException {
        Log.d("TransactionService","deleteTransaction > " + transaction.toPipes() +
                " account: " + account.toString());
        WalletDbHelper dbh = null;
        try {
            dbh = new WalletDbHelper(this.context);

            int deleted = this.dao.delete(dbh, transaction.getId());
            if (deleted == 0) {
                Utils.exception("Transaction delete failed");
            }

            Account me = this.accountDao.getById(dbh, account.getId());

            if (transaction.getTransferto() == 0) { // no transfer
                me.setAmount(me.getAmount() - transaction.getAmount());
                int updated = this.accountDao.update(dbh, me.getId(), me);
                if (updated == 0) {
                    Utils.exception("Account rollback failed");
                }
            } else { // transfer
                double amount = Math.abs(transaction.getAmount());
                boolean imSender = transaction.getAccount() == account.getId();
                if (imSender) {
                    me.setAmount(me.getAmount() + transaction.getAmount());
                    int updated = this.accountDao.update(dbh, me.getId(), me);
                    if (updated == 0) {
                        Utils.exception("Transfer sent recover failed");
                    }

                    Account receiver = this.accountDao.getById(dbh, transaction.getTransferto());
                    receiver.setAmount(receiver.getAmount() - transaction.getAmount());
                    updated = this.accountDao.update(dbh, receiver.getId(), receiver);
                    if (updated == 0) {
                        Utils.exception("Transfer sent target substraction failed");
                    }
                } else {
                    me.setAmount(me.getAmount() - transaction.getAmount());
                    int updated = this.accountDao.update(dbh, me.getId(), me);
                    if (updated == 0) {
                        Utils.exception("Transfer received return failed");
                    }

                    Account sender = this.accountDao.getById(dbh, transaction.getAccount());
                    sender.setAmount(sender.getAmount() + transaction.getAmount());
                    updated = this.accountDao.update(dbh, sender.getId(), sender);
                    if (updated == 0) {
                        Utils.exception("Transfer received sender refund failed");
                    }
                }
            }
        } catch (Exception e) {
            Utils.raise("Deleting transaction", e);
        } finally {
            if (dbh != null) {
                dbh.close();
            }
        }
        Log.d("TransactionService","deleteTransaction <");
    }
}