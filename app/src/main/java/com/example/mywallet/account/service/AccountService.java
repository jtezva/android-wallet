package com.example.mywallet.account.service;

import android.util.Log;

import com.example.mywallet.account.model.Account;

import java.util.ArrayList;
import java.util.List;

public class AccountService {
    public List<Account> loadAccountList() {
        Log.d("AccountService","loadAccountList");
        List<Account> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            Account x = new Account(i, "Account " + i, 100.1d * ((double) i));
            list.add(x);
        }
        return list;
    }
}