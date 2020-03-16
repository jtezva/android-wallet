package com.example.mywallet.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.mywallet.core.exception.WalletException;

public class Utils {

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static String nvl(Object obj) {
        return Utils.nvl(obj, "");
    }

    public static String nvl(Object obj, String replace) {
        if (obj == null) {
            return replace;
        }
        if (obj instanceof String) {
            if ("null".equals(obj)) {
                return replace;
            }
            return (String) obj;
        }
        return obj.toString();
    }

    public static void raise(String process, Exception e) throws WalletException {
        if (e instanceof WalletException) {
            throw (WalletException) e;
        } else {
            WalletException we = new WalletException("Error in process: " + process.toLowerCase(), e);
            Log.e(we.getMessage(), e.toString());
            throw we;
        }
    }

    public static void handle(Context context, String process, Exception e) {
        if (e instanceof WalletException) {
            Utils.toast(context, e.getMessage());
        } else {
            try {
                Utils.raise(process, e);
            } catch (WalletException e2) {
                Utils.handle(context, process, e2);
            }
        }
    }

    public static void exception(String message) throws WalletException{
        throw new WalletException(message);
    }
}