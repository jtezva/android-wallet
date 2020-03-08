package com.example.mywallet.account.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.mywallet.R;
import com.example.mywallet.account.controller.AccountListController;
import com.example.mywallet.account.controller.AccountListRowController;
import com.example.mywallet.account.model.Account;

import java.util.List;

public class AccountListAdapter extends ArrayAdapter<Account> {

    private AccountListController controller;
    private List<Account> accountList;

    public AccountListAdapter(@NonNull Context context, AccountListController controller, List<Account> accountList) {
        super(context, 0 , accountList);
        this.controller = controller;
        this.accountList = accountList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // model
        Account account = this.accountList.get(position);

        // view
        View view = LayoutInflater.from(this.getContext())
                .inflate(R.layout.fragment_account_list_row, parent,false);
        TextView label = view.findViewById(R.id.fragment_account_list_row_label);
        label.setText(account.getName());
        TextView amount = view.findViewById(R.id.fragment_account_list_row_amount);
        amount.setText(account.getAmount() + "");

        // controller
        final AccountListRowController rowController = new AccountListRowController(this.controller, account);
        Button edit = view.findViewById(R.id.fragment_account_list_row_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowController.onEditClick(v);
            }
        });
        Button delete = view.findViewById(R.id.fragment_account_list_row_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowController.onDeleteClick(v);
            }
        });
        return view;
    }
}