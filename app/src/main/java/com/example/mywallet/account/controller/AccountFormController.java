package com.example.mywallet.account.controller;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mywallet.R;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.account.view.AccountFormFragment;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class AccountFormController {
    private Account account;
    private boolean editMode;
    private AccountFormFragment fragment;
    private View view;
    private AccountService service;

    private TextView title;
    private EditText inputName;
    private EditText inputAmount;

    public AccountFormController(AccountFormFragment fragment, View view, Account account) {
        this.fragment = fragment;
        this.view = view;
        this.account = account;

        // init params
        if (this.account != null) {
            this.editMode = true;
        }
        this.service = new AccountService(view.getContext());
        this.title = view.findViewById(R.id.account_form_title);
        this.inputName = view.findViewById(R.id.account_form_name_input);
        this.inputAmount = view.findViewById(R.id.account_form_amount_input);
    }

    public void start() {
        if (this.editMode) {
            this.title.setText("Edit Account (id: " + this.account.getId() + ")");
            this.inputName.setText(this.account.getName());
            this.inputAmount.setText(String.valueOf(this.account.getAmount()));
        } else {
            this.title.setText("New Account");
            this.inputAmount.setText("0");
        }
    }

    public void onSaveClick() {
        try {
            Log.d("AccountFormController", "onSaveClick");
            String name = this.inputName.getText().toString();
            String amount = this.inputAmount.getText().toString();
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
            if (this.editMode) {
                this.service.updateAccount(this.account.getId(), new Account(name, Double.valueOf(amount)));
                Utils.toast(this.view.getContext(), "Account updated");
            } else {
                this.service.insertAccount(new Account(name, Double.valueOf(amount)));
                Utils.toast(this.view.getContext(), "Account added");
            }
            this.fragment.getActivity().setResult(Activity.RESULT_OK);
            this.fragment.getActivity().finish();
        } catch (Exception e) {
            Utils.handle(view.getContext(), "Saving changes", e);
        }
    }
}