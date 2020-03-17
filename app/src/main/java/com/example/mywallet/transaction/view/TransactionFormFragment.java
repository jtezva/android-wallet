package com.example.mywallet.transaction.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mywallet.R;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.constant.Constant;
import com.example.mywallet.transaction.controller.TransactionFormController;
import com.example.mywallet.transaction.model.Transaction;
import com.example.mywallet.util.FormUtils;
import com.example.mywallet.util.Utils;

public class TransactionFormFragment extends Fragment {

    private TransactionFormController controller;

    // input
    private EditText inputAmount;
    private EditText inputConcept;
    private Spinner inputAccountSpinner;
    private Spinner inputTransferSpinner;
    private EditText inputDate;
    private EditText inputTime;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_transaction_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // input
            this.inputAmount = view.findViewById(R.id.transaction_form_amount_input);
            this.inputConcept = view.findViewById(R.id.transaction_form_concept_input);
            this.inputAccountSpinner = view.findViewById(R.id.transaction_form_account_input);
            this.inputTransferSpinner = view.findViewById(R.id.transaction_form_transfer_input);
            this.inputDate = view.findViewById(R.id.transaction_form_date_input);
            this.inputTime = view.findViewById(R.id.transaction_form_time_input);

            // recover data
            Intent intent = this.getActivity().getIntent();
            String mode = intent.getStringExtra(Constant.INTENT_TOKEN_FORM_MODE); // mode
            FormUtils.validateMode(mode);
            boolean isUpdate = Constant.FORM_MODE_UPDATE.equals(mode);

            Transaction transaction = null;
            Account account = null;
            if (isUpdate) {
                String transactionPipes = intent.getStringExtra(Constant.INTENT_TOKEN_TRANSACTION);
                if (!TextUtils.isEmpty(transactionPipes)) {
                    transaction = Transaction.fromPipes(transactionPipes);
                }
            } else {
                String accountPipes = intent.getStringExtra(Constant.INTENT_TOKEN_ACCOUNT);
                if (!TextUtils.isEmpty(accountPipes)) {
                    account = Account.fromPipes(accountPipes);
                }
            }

            // controller
            this.controller = new TransactionFormController(this, isUpdate, transaction, account);
            this.controller.start();

            // controls
            Button submit = view.findViewById(R.id.transaction_form_submit_button);
            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.onSaveClick();
                }
            });
        } catch (Exception e) {
            Utils.handle(this.getContext(), "Transaction form view start", e);
            this.getActivity().finish();
        }
    }

    // getters & setters
    public EditText getInputAmount() {
        return inputAmount;
    }

    public EditText getInputConcept() {
        return inputConcept;
    }

    public Spinner getInputAccountSpinner() {
        return inputAccountSpinner;
    }

    public Spinner getInputTransferSpinner() {
        return inputTransferSpinner;
    }

    public EditText getInputDate() {
        return inputDate;
    }

    public EditText getInputTime() {
        return inputTime;
    }
}