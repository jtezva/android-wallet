package com.example.mywallet.account.controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.example.mywallet.account.view.AccountFormActivity;
import com.example.mywallet.account.view.AccountListFragment;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.account.view.AccountListAdapter;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccountListController {

    private AccountListFragment fragment;
    private AccountListAdapter adapter;
    private AccountService service;
    private List<Account> accountList;

    public AccountListController(AccountListFragment view) {
        this.fragment = view;
        this.service = new AccountService(this.fragment.getContext());
    }

    public void start() {
        Log.d("AccountListController", "start");
        this.accountList = new ArrayList<>();
        this.adapter = new AccountListAdapter(this.fragment.getContext(), this, this.accountList);
        this.fragment.setListAdapter(this.adapter);
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
        Intent intent = new Intent(this.fragment.getContext(), AccountFormActivity.class);
        intent.putExtra("account", account.toString());
        this.fragment.startActivityForResult(intent, 2);
    }

    public void onDeleteClick(View v, final Account account) {
        Log.d("AccountListController.onDeleteClick", account.toString());
        new AlertDialog.Builder(this.fragment.getContext())
                .setTitle("Delete")
                .setMessage("Do you really want to delete?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        try {
                            service.deleteAccountById(account.getId());
                            Utils.toast(fragment.getContext(), "Account deleted");
                            load();
                        } catch (Exception e) {
                            Utils.handle(fragment.getContext(), "Deleting account", e);
                        }
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public void onAddClick(View view) {
        Intent intent = new Intent(this.fragment.getContext(), AccountFormActivity.class);
        this.fragment.startActivityForResult(intent, 1);
    }
}