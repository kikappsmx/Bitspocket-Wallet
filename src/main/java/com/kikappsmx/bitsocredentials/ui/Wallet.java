package com.kikappsmx.bitsocredentials.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.kikappsmx.bitsocredentials.R;
import com.kikappsmx.bitsocredentials.data.entities.BalancesResponse;
import com.kikappsmx.bitsocredentials.data.network.BitsoApiManager;
import com.kikappsmx.bitsocredentials.utils.Authorization;
import com.kikappsmx.bitsocredentials.utils.Encryption;

import javax.crypto.SecretKey;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Wallet {

    private static final Wallet ourInstance = new Wallet();

    public static Wallet getInstance() {
        return ourInstance;
    }

    private OnRequestWalletListener listener;
    private String secret;
    private String key;

    private Wallet() {
    }

    /**
     * @param context         the activity context
     * @param encryptedKey    the encrypted key stored in preferences
     * @param encryptedSecret the encrypted secret stored in preferences
     * @param listener        an interface which is responsible to provide the balance
     */
    @SuppressLint("InflateParams")
    public void requestWallet(final Context context, String encryptedKey, String encryptedSecret, OnRequestWalletListener listener) {
        this.listener = listener;
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setCancelable(false)
                .setTitle(R.string.enter_your_pin)
                .setMessage(R.string.enter_your_pin_message)
                .setView(view)
                .setNegativeButton(R.string.cancel, (dialog, which) -> ((Activity) context).onBackPressed())
                .show();

        EditText etPin = view.findViewById(R.id.edit_text_pin);
        etPin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 4) {
                    SecretKey secretKey = Encryption.getSecretKey(editable.toString());
                    key = Encryption.getDecryptedString(encryptedKey, secretKey);
                    secret = Encryption.getDecryptedString(encryptedSecret, secretKey);
                    if (key.isEmpty() || secret.isEmpty()) {
                        etPin.setError(context.getString(R.string.error_decrypting));
                        etPin.requestFocus();
                        return;
                    }

                    alertDialog.cancel();


                    refreshWallet();
                } else {
                    etPin.setError(null);
                }
            }
        });
    }

    public void refreshWallet() {
        String authorization = "";
        try {
            authorization = Authorization.getAuthorization(key, secret);
        } catch (Exception e) {
            listener.onError(e.getCause());
        }

        new BitsoApiManager().requestBalances(authorization)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(listener::onComplete, listener::onError);
    }

    public interface OnRequestWalletListener {

        void onComplete(BalancesResponse balancesResponse);

        void onError(Throwable e);
    }
}
