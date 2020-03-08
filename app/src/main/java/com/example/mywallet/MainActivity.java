package com.example.mywallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mywallet.core.dao.WalletDbHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
