package com.example.mywallet.transaction.controller;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.example.mywallet.account.model.Account;
import com.example.mywallet.account.service.AccountService;
import com.example.mywallet.core.exception.WalletException;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.transaction.service.TransactionService;
import com.example.mywallet.transaction.view.TransactionFormFragment;
import com.example.mywallet.util.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TransactionFormController {

    private TransactionFormFragment fragment;
    private boolean isUpdate;
    private Transaction transaction;
    private Account account;
    private AccountService accountService;
    private TransactionService service;

    public TransactionFormController(TransactionFormFragment fragment, boolean isUpdate,
                                     Transaction transaction, Account account) {
        this.fragment = fragment;
        this.isUpdate = isUpdate;
        this.transaction = transaction;
        this.account = account;

        this.accountService = new AccountService(this.fragment.getContext());
        this.service = new TransactionService(this.fragment.getContext());
    }

    public void start() {
        this.setTime();
        List<Account> accounts = this.loadAccountSpinners();
        if (this.isUpdate) {
            // this.load
        } else  {
            if (this.account != null) {
                this.selectOriginAccount(accounts);
            }
        }
    }

    private void selectOriginAccount(List<Account> accounts) {
        int index = accounts.indexOf(this.account);
        if (index > -1) {
            this.fragment.getInputAccountSpinner().setSelection(index);
        }
    }

    private void setTime() {
        Calendar now = Calendar.getInstance();
        this.fragment.getInputDate().setText(
                now.get(Calendar.YEAR) + "/" +
                String.format("%02d", (now.get(Calendar.MONTH) + 1)) + "/" +
                String.format("%02d", now.get(Calendar.DAY_OF_MONTH)));

        this.fragment.getInputTime().setText(
                String.format("%02d", now.get(Calendar.HOUR_OF_DAY)) + ":" +
                String.format("%02d", (now.get(Calendar.MINUTE) + 1)));
    }

    private List<Account> loadAccountSpinners() {
        List<Account> accountList = this.accountService.loadAccountList();
        accountList.add(0, new Account(0, "None", 0d));

        // adapter
        ArrayAdapter<Account> adapter = new ArrayAdapter<>(this.fragment.getContext(),
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        adapter.addAll(accountList);
        this.fragment.getInputAccountSpinner().setAdapter(adapter);
        this.fragment.getInputTransferSpinner().setAdapter(adapter);
        return accountList;
    }

    public void onSaveClick() {
        Log.d("TransactionFormController", "onSaveClick >");
        try {
            // recover data
            String strAmount = this.fragment.getInputAmount().getText().toString();
            String concept = this.fragment.getInputConcept().getText().toString();
            Account selAccount = (Account) this.fragment.getInputAccountSpinner().getSelectedItem();
            Account selTransfer = (Account) this.fragment.getInputTransferSpinner().getSelectedItem();
            String strDate = this.fragment.getInputDate().getText().toString();
            String strTime = this.fragment.getInputTime().getText().toString();
            Log.d("TransactionFormController", strAmount + "|" + concept + "|" +
                    selAccount.getId() + "|" + selTransfer.getId() + "|" + strDate + "|" + strTime);

            // validate data
            List<String> errors = new ArrayList<>();
            if (TextUtils.isEmpty(strAmount) || strAmount.equals("-")) {
                errors.add("AMOUNT");
            }
            if (TextUtils.isEmpty(concept)) {
                errors.add("CONCEPT");
            }
            if (selAccount.getId() == 0) {
                errors.add("ACCOUNT");
            }
            if (TextUtils.isEmpty(strDate) || strDate.length() != 10 ||
                    strDate.charAt(4) != '/' ||
                    strDate.charAt(7) != '/') {
                errors.add("DATE");
            }
            if (TextUtils.isEmpty(strTime) || strTime.length() != 5 || strTime.charAt(2) != ':') {
                errors.add("TIME");
            }
            if (selAccount.getId() == selTransfer.getId()) {
                errors.add("ACCOUNT & TRANSFER EQUALS");
            }
            if (errors.size() > 0) {
                throw new WalletException("Please review: " + TextUtils.join(", ", errors));
            }

            boolean isTransfer = selTransfer.getId() > 0;
            if (!this.isUpdate) {
                if (!isTransfer) {
                    this.service.registerTransaction(new Transaction(0,
                            Double.parseDouble(strAmount), concept, selAccount.getId(),
                            selTransfer.getId(), strDate + " " + strTime));
                    Utils.toast(this.fragment.getContext(), "Transaction registered");
                } else {
                    this.service.registerTransference(new Transaction(0,
                            Double.parseDouble(strAmount), concept, selAccount.getId(),
                            selTransfer.getId(), strDate + " " + strTime));
                    Utils.toast(this.fragment.getContext(), "Transference registered");
                }
            } else {
                if (!isTransfer) {
                    this.service.updateTransaction(this.transaction.getId(), new Transaction(0,
                            Double.parseDouble(strAmount), concept, selAccount.getId(),
                            selTransfer.getId(), strDate + " " + strTime));
                    Utils.toast(this.fragment.getContext(), "Transaction updated");
                } else {
                    this.service.updateTransference(this.transaction.getId(), new Transaction(0,
                            Double.parseDouble(strAmount), concept, selAccount.getId(),
                            selTransfer.getId(), strDate + " " + strTime));
                    Utils.toast(this.fragment.getContext(), "Transference updated");
                }
            }
            this.fragment.getActivity().setResult(Activity.RESULT_OK);
            this.fragment.getActivity().finish();
        } catch (Exception e) {
            Utils.handle(this.fragment.getContext(), "Saving transaction", e);
        }
        Log.d("TransactionFormController", "onSaveClick <");
    }
}