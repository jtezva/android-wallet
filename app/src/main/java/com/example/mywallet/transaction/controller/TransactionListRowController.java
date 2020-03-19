package com.example.mywallet.transaction.controller;

import com.example.mywallet.transaction.model.Transaction;

public class TransactionListRowController {
    private TransactionListController parentController;
    private Transaction transaction;

    public TransactionListRowController(TransactionListController parentController, Transaction transaction){
        this.parentController = parentController;
        this.transaction = transaction;
    }

    public void onEditClick() {
        this.parentController.onEditClick(this.transaction);
    }

    public void onDeleteClick() {
        this.parentController.onDeleteClick(this.transaction);
    }
}