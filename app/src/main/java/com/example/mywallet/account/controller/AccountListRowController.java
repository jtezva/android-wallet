package com.example.mywallet.account.controller;

import android.view.View;

import com.example.mywallet.account.model.Account;

public class AccountListRowController {
    private AccountListController parentController;
    private Account account;

    public AccountListRowController(AccountListController parentController, Account account){
        this.parentController = parentController;
        this.account = account;
    }

    public void onEditClick(View view) {
        this.parentController.onEditClick(view, this.account);
    }

    public void onDeleteClick(View view) {
        this.parentController.onDeleteClick(view, this.account);
    }
}