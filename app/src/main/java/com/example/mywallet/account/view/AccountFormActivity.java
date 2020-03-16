package com.example.mywallet.account.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.mywallet.R;
import com.example.mywallet.account.model.Account;
import com.example.mywallet.util.Utils;

public class AccountFormActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_form);
    }
}
