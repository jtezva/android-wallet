package com.example.mywallet.core.constant;

public interface Constant {
    String INTENT_TOKEN_FORM_MODE = "mode";
    String INTENT_TOKEN_ACCOUNT = "account";
    String INTENT_TOKEN_TRANSACTION = "transaction";

    String FORM_MODE_INSERT = "insert";
    String FORM_MODE_UPDATE = "update";

    int ACTIVITY_CODE_ACCOUNT_FORM_INSERT = 1;
    int ACTIVITY_CODE_ACCOUNT_FORM_UPDATE = 2;
    int ACTIVITY_CODE_TRANSACTION_FORM_INSERT = 3;
    int ACTIVITY_CODE_TRANSACTION_FORM_UPDATE = 4;
    int ACTIVITY_CODE_TRANSACTION_LIST = 5;
}