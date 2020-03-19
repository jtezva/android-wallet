package com.example.mywallet.transaction.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.core.constant.Constant;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.transaction.service.TransactionService;
import com.example.mywallet.transaction.view.TransactionFormActivity;
import com.example.mywallet.transaction.view.TransactionListAdapter;
import com.example.mywallet.transaction.view.TransactionListFragment;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class TransactionListController {

    private TransactionListFragment fragment;
    private Account account;
    private TransactionListAdapter adapter;
    private TransactionService service;
    private AccountService accountService;
    private List<Transaction> transactionList;

    public TransactionListController(TransactionListFragment fragment, Account account) {
        this.fragment = fragment;
        this.account = account;
        this.service = new TransactionService(this.fragment.getContext());
        this.accountService = new AccountService(this.fragment.getContext());
    }

    public void start() {
        Log.d("TransactionListController", "start");
        this.transactionList = new ArrayList<>();
        List<Account> accounts = this.accountService.loadAccountList();
        this.adapter = new TransactionListAdapter(this.fragment.getContext(), this,
                this.transactionList, accounts);
        this.fragment.setListAdapter(this.adapter);
        this.load();
    }

    public void load() {
        Log.d("TransactionListController", "load");
        List<Transaction> list = this.service.loadTransactionList(account.getId());

        // regression
        double after = this.account.getAmount();
        for (Transaction t : list) {
            double before;
            if (t.getTransferto() == 0 || t.getTransferto() == this.account.getId()) { // no transfer or we received
                before = after - t.getAmount();
            } else { // we sent
                before = after + t.getAmount();
            }
            t.setAmountAfter(after);
            t.setAmountBefore(before);
            after = before;
        }

        this.transactionList.clear();
        this.transactionList.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    public void onEditClick(Transaction transaction) {
        Log.d("TransactionListController.onEditClick", transaction.toPipes());
        Intent intent = new Intent(this.fragment.getContext(), TransactionFormActivity.class);
        intent.putExtra(Constant.INTENT_TOKEN_FORM_MODE, Constant.FORM_MODE_UPDATE);
        intent.putExtra(Constant.INTENT_TOKEN_TRANSACTION, transaction.toPipes());
        this.fragment.startActivityForResult(intent, Constant.ACTIVITY_CODE_TRANSACTION_FORM_UPDATE);
    }

    public void onDeleteClick(final Transaction transaction) {
        Log.d("TransactionListController.onDeleteClick", transaction.toPipes());
        new AlertDialog.Builder(this.fragment.getContext())
                .setTitle("Delete")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            service.deleteTransaction(transaction, account);
                            Utils.toast(fragment.getContext(), "Transaction deleted");
                            fragment.getActivity().finish();
                        } catch (Exception e) {
                            Utils.handle(fragment.getContext(), "Deleting transaction", e);
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    // getters & setters
    public Account getAccount() {
        return account;
    }
}