package com.example.mywallet.transaction.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.mywallet.R;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.constant.Constant;
import com.example.mywallet.transaction.controller.TransactionListController;
import com.example.mywallet.util.Utils;

public class TransactionListFragment extends ListFragment {

    private TransactionListController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //
        return inflater.inflate(R.layout.fragment_transaction_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // recover data
            Intent intent = this.getActivity().getIntent();
            String accountPipes = intent.getStringExtra(Constant.INTENT_TOKEN_ACCOUNT);
            if (TextUtils.isEmpty(accountPipes)) {
                Utils.exception("Missing account to load transactions");
            }
            Account account = Account.fromPipes(accountPipes);

            // input
            TextView title = view.findViewById(R.id.fragment_transaction_list_title);
            title.setText(account.getName() + ": $" + account.getAmount());

            this.controller = new TransactionListController(this, account);
            this.controller.start();
        } catch (Exception e) {
            Utils.handle(this.getContext(), "Transaction list start", e);
            this.getActivity().finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AccountListFragment.onActivityResult", requestCode + ": " + resultCode);
        //TODO
    }
}