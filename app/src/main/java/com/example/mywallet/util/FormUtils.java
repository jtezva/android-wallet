package com.example.mywallet.util;

import android.text.TextUtils;

import com.example.mywallet.core.constant.Constant;
import com.example.mywallet.core.exception.WalletException;

public class FormUtils {

    public static void validateMode(String mode) throws WalletException {
        if (TextUtils.isEmpty(mode)) {
            Utils.exception("Missing Form Mode");
        }
        if (!Constant.FORM_MODE_INSERT.equals(mode) && !Constant.FORM_MODE_UPDATE.equals(mode)) {
            Utils.exception("Invalid Form Mode");
        }
    }
}