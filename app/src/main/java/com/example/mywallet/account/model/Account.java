package com.example.mywallet.account.model;

import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.example.mywallet.util.Utils;

public class Account {

    private int id;
    private String name;
    private Double amount;

    public Account() {}

    public Account(String name, Double amount) {
        this.name = name;
        this.amount = amount;
    }

    public Account(int id, String name, Double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return this.name + " (" + this.id + ")";
    }

    public String toPipes() {
        return Utils.nvl(this.id) + "|" + Utils.nvl(this.name) + "|" + Utils.nvl(this.amount);
    }

    public static Account fromPipes(String str) {
        Account a = new Account();
        if (str != null) {
            String[] tokens = str.split("\\|");
            if (!TextUtils.isEmpty(tokens[0])) {
                a.setId(Integer.valueOf(tokens[0]));
            }
            if (!TextUtils.isEmpty(tokens[1])) {
                a.setName(tokens[1]);
            }
            if (!TextUtils.isEmpty(tokens[2])) {
                a.setAmount(Double.valueOf(tokens[2]));
            }
        }
        return a;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Account)) return false;
        return ((Account) obj).getId() == this.id;
    }

    // getters & setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}