package com.example.mywallet.core.exception;

public class WalletException extends RuntimeException {

    public WalletException(String message) {
        super(message);
    }

    public WalletException(String message, Exception e) {
        super(message, e);
    }
}