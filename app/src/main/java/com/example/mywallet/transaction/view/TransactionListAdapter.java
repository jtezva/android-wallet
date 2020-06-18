package com.example.mywallet.transaction.view;

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
import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.transaction.controller.TransactionListController;
import com.example.mywallet.transaction.controller.TransactionListRowController;
import com.example.mywallet.transaction.model.Transaction;

import java.util.List;

public class TransactionListAdapter extends ArrayAdapter<Transaction> {

    private TransactionListController controller;
    private List<Transaction> transactionList;
    private List<Account> accounts;

    public TransactionListAdapter(@NonNull Context context, TransactionListController controller,
                                  List<Transaction> transactionList, List<Account> accounts) {
        super(context, 0 , transactionList);
        this.controller = controller;
        this.transactionList = transactionList;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // model
        Transaction transaction = this.transactionList.get(position);

        // view
        View view = LayoutInflater.from(this.getContext())
                .inflate(R.layout.fragment_transaction_list_row, parent,false);
        TextView label = view.findViewById(R.id.transaction_list_row_stamp);
        label.setText("[" + transaction.getStamp() + "] $" + String.format("%.2f", transaction.getAmount()));

        String concept = transaction.getConcept();
        if (transaction.getTransferto() > 0) { // transfer
            if (transaction.getAccount() == this.controller.getAccount().getId()) { // we sent
                Account target = new Account();
                target.setId(transaction.getTransferto());
                int index = this.accounts.indexOf(target);
                if (index > -1) {
                    concept = "(TO " + this.accounts.get(index).getName() + ") " + concept;
                } else {
                    concept = "(TO #" + transaction.getTransferto() + ") " + concept;
                }
            } else { // we received
                Account origin = new Account();
                origin.setId(transaction.getAccount());
                int index = this.accounts.indexOf(origin);
                if (index > -1) {
                    concept = "(FROM " + this.accounts.get(index).getName() + ": \"" + concept + "\")";
                } else {
                    concept = "(FROM #" + transaction.getAccount() + ": \"" + concept + "\")";
                }
            }
        }

        TextView amount = view.findViewById(R.id.transaction_list_row_amount);
        amount.setText(concept);

        TextView balance = view.findViewById(R.id.transaction_list_row_balance);
        balance.setText("($" + String.format("%.2f", transaction.getAmountBefore()) + " => $" + String.format("%.2f", transaction.getAmountAfter()) + ")");

        // controller
        final TransactionListRowController rowController = new TransactionListRowController(this.controller, transaction);
        Button edit = view.findViewById(R.id.transaction_list_row_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowController.onEditClick();
            }
        });
        Button delete = view.findViewById(R.id.transaction_list_row_delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rowController.onDeleteClick();
            }
        });
        return view;
    }
}