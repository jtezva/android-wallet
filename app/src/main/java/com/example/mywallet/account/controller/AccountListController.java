package com.example.mywallet.account.controller;

import android.util.Log;
import android.view.View;

import com.example.mywallet.account.view.AccountListFragment;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.account.view.AccountListAdapter;

import java.util.ArrayList;
import java.util.List;

public class AccountListController {

    private AccountListFragment view;
    private AccountListAdapter adapter;
    private AccountService service;
    private List<Account> accountList;

    public AccountListController(AccountListFragment view) {
        this.view = view;
        this.service = new AccountService();
    }

    public void start() {
        Log.d("AccountListController", "start");
        this.accountList = new ArrayList<>();
        this.adapter = new AccountListAdapter(this.view.getContext(), this, this.accountList);
        this.view.setListAdapter(this.adapter);
        this.load();
    }

    public void load() {
        Log.d("AccountListController", "load");
        List<Account> list = this.service.loadAccountList();
        this.accountList.clear();
        this.accountList.addAll(list);
        this.adapter.notifyDataSetChanged();
    }

    public void onEditClick(View view, Account account) {
        Log.d("AccountListController.onEditClick", account.toString());
        account.setAmount(account.getAmount() + 1d);
        this.adapter.notifyDataSetChanged();
    }

    public void onDeleteClick(View view, Account account) {
        Log.d("AccountListController.onDeleteClick", account.toString());
        this.accountList.remove(account);
        this.adapter.notifyDataSetChanged();
    }
}