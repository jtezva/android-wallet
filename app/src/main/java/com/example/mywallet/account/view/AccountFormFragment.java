package com.example.mywallet.account.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mywallet.R;
import com.example.mywallet.account.controller.AccountFormController;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.core.constant.Constant;
import com.example.mywallet.util.FormUtils;
import com.example.mywallet.util.Utils;

public class AccountFormFragment extends Fragment {

    private AccountFormController controller;

    // input
    private TextView inputTitle;
    private EditText inputName;
    private EditText inputAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        try {
            // input
            this.inputTitle = view.findViewById(R.id.account_form_title);
            this.inputName = view.findViewById(R.id.account_form_name_input);
            this.inputAmount = view.findViewById(R.id.account_form_amount_input);

            // recover data
            Intent intent = this.getActivity().getIntent();
            String mode = intent.getStringExtra(Constant.INTENT_TOKEN_FORM_MODE);
            FormUtils.validateMode(mode);
            boolean isUpdate = Constant.FORM_MODE_UPDATE.equals(mode);

            Account account = null;
            if (isUpdate) {
                String accountPipes = intent.getStringExtra(Constant.INTENT_TOKEN_ACCOUNT);
                if (TextUtils.isEmpty(accountPipes)) {
                    Utils.exception("Account missing for update");
                }
                account = Account.fromPipes(accountPipes);
            }

            // controller
            this.controller = new AccountFormController(this, isUpdate, account);
            this.controller.start();

            // controls
            Button saveButton = view.findViewById(R.id.account_form_submit_button);
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    controller.onSaveClick();
                }
            });
        } catch (Exception e) {
            Utils.handle(this.getContext(), "Account form view start", e);
            this.getActivity().finish();
        }
    }

    // getters & setters
    public EditText getInputName() {
        return inputName;
    }

    public EditText getInputAmount() {
        return inputAmount;
    }

    public TextView getInputTitle() {
        return inputTitle;
    }
}