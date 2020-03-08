package com.example.mywallet.account.model;

import androidx.annotation.Nullable;

public class Account {

    private int id;
    private String name;
    private Double amount;

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
        return "{ id: " + this.id + ", name: '" + this.name + "', amount: " + this.amount + " }";
    }
}