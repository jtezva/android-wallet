package com.example.mywallet.account.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mywallet.R;
import com.example.mywallet.account.controller.AccountFormController;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.util.Utils;

public class AccountFormFragment extends Fragment {

    private Account account;
    private AccountFormController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //this.controller = new AccountListController(this);
        return inflater.inflate(R.layout.fragment_account_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // recover data
        Intent intent = this.getActivity().getIntent();
        String stringAccount = intent.getStringExtra("account");
        Log.d("AccountFormActivity.onCreate", Utils.nvl(stringAccount, "-null-"));
        if (stringAccount != null && stringAccount.length() > 0) {
            this.account = Account.fromString(stringAccount);
        }

        // controller
        this.controller = new AccountFormController(this, view, this.account);
        this.controller.start();

        // button action
        Button saveButton = view.findViewById(R.id.account_form_submit_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onSaveClick();
            }
        });
    }
}