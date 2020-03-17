package com.example.mywallet.account.controller;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;

import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.account.view.AccountFormFragment;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccountFormController {

    private AccountFormFragment fragment;
    private boolean isUpdate;
    private Account account;

    private AccountService service;

    public AccountFormController(AccountFormFragment fragment, boolean isUpdate, Account account) {
        this.fragment = fragment;
        this.isUpdate = isUpdate;
        this.account = account;

        this.service = new AccountService(this.fragment.getContext());
    }

    public void start() {
        if (this.isUpdate) {
            this.fragment.getInputTitle().setText("Edit Account (id: " + this.account.getId() + ")");
            this.fragment.getInputName().setText(this.account.getName());
            this.fragment.getInputAmount().setText(String.valueOf(this.account.getAmount()));
        } else {
            this.fragment.getInputTitle().setText("New Account");
            this.fragment.getInputAmount().setText("0");
        }
    }

    public void onSaveClick() {
        try {
            Log.d("AccountFormController", "onSaveClick");
            String name = this.fragment.getInputName().getText().toString();
            String amount = this.fragment.getInputAmount().getText().toString();
            List<String> errors = new ArrayList<>();
            if (TextUtils.isEmpty(name)) {
                errors.add("NAME");
            }
            if (TextUtils.isEmpty(amount)) {
                errors.add("AMOUNT");
            }
            if (errors.size() > 0) {
                Utils.exception("Please review: " + TextUtils.join(", ", errors));
            }
            if (this.isUpdate) {
                this.service.updateAccount(this.account.getId(), new Account(name, Double.valueOf(amount)));
                Utils.toast(this.fragment.getContext(), "Account updated");
            } else {
                this.service.insertAccount(new Account(name, Double.valueOf(amount)));
                Utils.toast(this.fragment.getContext(), "Account added");
            }
            this.fragment.getActivity().setResult(Activity.RESULT_OK);
            this.fragment.getActivity().finish();
        } catch (Exception e) {
            Utils.handle(this.fragment.getContext(), "Saving changes", e);
        }
    }
}