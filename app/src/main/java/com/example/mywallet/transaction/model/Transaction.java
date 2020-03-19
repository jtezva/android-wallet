package com.example.mywallet.transaction.model;

import android.text.TextUtils;

import com.example.mywallet.util.Utils;

public class Transaction {

    private int id;
    private double amount;
    private String concept;
    private int account;
    private int transferto;
    private String stamp;
    private double amountBefore;
    private double amountAfter;

    public Transaction() {}

    public Transaction(int id, double amount, String concept, int account, int transferto,
                       String stamp) {
        this.id = id;
        this.amount = amount;
        this.concept = concept;
        this.account = account;
        this.transferto = transferto;
        this.stamp = stamp;
    }

    @Override
    public String toString() {
        return this.concept + " (" + this.id + ")";
    }

    public String toPipes() {
        return Utils.nvl(this.id) + "|" + Utils.nvl(this.amount) + "|" + Utils.nvl(this.concept) +
                "|" + Utils.nvl(this.account) + "|" + Utils.nvl(this.transferto) + "|" +
                Utils.nvl(this.stamp);
    }

    public static Transaction fromPipes(String str) {
        Transaction t = new Transaction();
        if (str != null) {
            String[] tokens = str.split("\\|");
            if (!TextUtils.isEmpty(tokens[0])) {
                t.setId(Integer.valueOf(tokens[0]));
            }
            if (!TextUtils.isEmpty(tokens[1])) {
                t.setAmount(Double.valueOf(tokens[1]));
            }
            if (!TextUtils.isEmpty(tokens[2])) {
                t.setConcept(tokens[2]);
            }
            if (!TextUtils.isEmpty(tokens[3])) {
                t.setAccount(Integer.valueOf(tokens[3]));
            }
            if (!TextUtils.isEmpty(tokens[4])) {
                t.setTransferto(Integer.valueOf(tokens[4]));
            }
            if (!TextUtils.isEmpty(tokens[5])) {
                t.setStamp(tokens[5]);
            }
        }
        return t;
    }

    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }

    public int getTransferto() {
        return transferto;
    }

    public void setTransferto(int transferto) {
        this.transferto = transferto;
    }

    public String getStamp() {
        return stamp;
    }

    public void setStamp(String stamp) {
        this.stamp = stamp;
    }

    public double getAmountBefore() {
        return amountBefore;
    }

    public void setAmountBefore(double amountBefore) {
        this.amountBefore = amountBefore;
    }

    public double getAmountAfter() {
        return amountAfter;
    }

    public void setAmountAfter(double amountAfter) {
        this.amountAfter = amountAfter;
    }
}