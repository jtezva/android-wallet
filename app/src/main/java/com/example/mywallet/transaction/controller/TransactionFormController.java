package com.example.mywallet.transaction.controller;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.transaction.view.TransactionFormFragment;

import java.util.Calendar;
import java.util.List;

public class TransactionFormController {

    private TransactionFormFragment fragment;
    private boolean isUpdate;
    private Transaction transaction;
    private Account account;
    private AccountService accountService;

    public TransactionFormController(TransactionFormFragment fragment, boolean isUpdate,
                                     Transaction transaction, Account account) {
        this.fragment = fragment;
        this.isUpdate = isUpdate;
        this.transaction = transaction;
        this.account = account;

        this.accountService = new AccountService(this.fragment.getContext());
    }

    public void start() {
        this.setTime();
        List<Account> accounts = this.loadAccountSpinners();
        if (this.isUpdate) {
            // this.load
        } else  {
            if (this.account != null) {
                this.selectOriginAccount(accounts);
            }
        }
    }

    private void selectOriginAccount(List<Account> accounts) {
        int index = accounts.indexOf(this.account);
        if (index > -1) {
            this.fragment.getInputAccountSpinner().setSelection(index);
        }
    }

    private void setTime() {
        Calendar now = Calendar.getInstance();
        this.fragment.getInputDate().setText(
                String.format("%02d", now.get(Calendar.DAY_OF_MONTH)) + "/" +
                String.format("%02d", (now.get(Calendar.MONTH) + 1)) + "/" +
                now.get(Calendar.YEAR));

        this.fragment.getInputTime().setText(
                String.format("%02d", now.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d", (now.get(Calendar.MINUTE) + 1)));
    }

    private List<Account> loadAccountSpinners() {
        List<Account> accountList = this.accountService.loadAccountList();
        accountList.add(0, new Account(0, "None", 0d));

        // adapter
        ArrayAdapter<Account> adapter = new ArrayAdapter<>(this.fragment.getContext(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter.addAll(accountList);
        this.fragment.getInputAccountSpinner().setAdapter(adapter);
        this.fragment.getInputTransferSpinner().setAdapter(adapter);
        return accountList;
    }

    public void onSaveClick() {
        Log.d("TransactionFormController", "onSaveClick >");
    }
}