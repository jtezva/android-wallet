package com.example.mywallet.account.model;

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

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Account)) return false;
        return ((Account) obj).getId() == this.id;
    }

    @Override
    public String toString() {
        return Utils.nvl(this.id) + "," + Utils.nvl(this.name) + "," + Utils.nvl(this.amount);
    }

    public static Account fromString(String str) {
        Account a = new Account();
        if (str != null) {
            String[] tokens = str.split(",");
            if (tokens[0].length() > 0) {
                a.setId(Integer.valueOf(tokens[0]));
            }
            if (tokens[1].length() > 0) {
                a.setName(tokens[1]);
            }
            if (tokens[2].length() > 0) {
                a.setAmount(Double.valueOf(tokens[2]));
            }
        }
        return a;
    }
}