package com.example.mywallet.account.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.ListFragment;

import com.example.mywallet.R;
import com.example.mywallet.account.controller.AccountListController;

public class AccountListFragment extends ListFragment {

    private AccountListController controller;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.controller = new AccountListController(this);
        return inflater.inflate(R.layout.fragment_account_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.controller.start();

        Button addButton = view.findViewById(R.id.account_list_add_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                controller.onAddClick();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("AccountListFragment.onActivityResult", requestCode + ": " + resultCode);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            Log.d("AccountListFragment.onActivityResult", "correct insert result");
            this.controller.load();
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            Log.d("AccountListFragment.onActivityResult", "correct update result");
            this.controller.load();
        }
    }
}