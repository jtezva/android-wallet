package com.example.mywallet.account.controller;

import com.example.mywallet.account.model.Account;

public class AccountListRowController {
    private AccountListController parentController;
    private Account account;

    public AccountListRowController(AccountListController parentController, Account account){
        this.parentController = parentController;
        this.account = account;
    }

    public void onEditClick() {
        this.parentController.onEditClick(this.account);
    }

    public void onDeleteClick() {
        this.parentController.onDeleteClick(this.account);
    }

    public void onTransactionClick() {
        this.parentController.onTransactionClick(this.account);
    }
}