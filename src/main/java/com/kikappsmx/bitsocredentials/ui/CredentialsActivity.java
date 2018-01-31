package com.kikappsmx.bitsocredentials.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.kikappsmx.bitsocredentials.R;
import com.kikappsmx.bitsocredentials.utils.Encryption;

import javax.crypto.SecretKey;

import static com.kikappsmx.bitsocredentials.utils.Constants.WALLET_KEY;
import static com.kikappsmx.bitsocredentials.utils.Constants.WALLET_SECRET;

public abstract class CredentialsActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText key, secret, pin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);
        setupActionBar();
        setupViews();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }

    private void setupViews() {
        key = findViewById(R.id.key);
        secret = findViewById(R.id.secret);
        pin = findViewById(R.id.edit_text_pin);
        findViewById(R.id.setup).setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        saveCredentials();
    }

    private void saveCredentials() {
        key.setError(null);
        secret.setError(null);
        pin.setError(null);

        String key = this.key.getText().toString();
        String secret = this.secret.getText().toString();
        String pin = this.pin.getText().toString();

        if (key.isEmpty()) {
            this.key.setError(getString(R.string.invalid_key));
            this.key.requestFocus();
            return;
        }

        if (secret.isEmpty()) {
            this.secret.setError(getString(R.string.invalid_secret));
            this.secret.requestFocus();
            return;
        }

        if (pin.isEmpty() || pin.length() < 4) {
            this.pin.setError(getString(R.string.invalid_pin));
            this.pin.requestFocus();
            return;
        }

        SecretKey secretKey = Encryption.getSecretKey(pin);
        String encryptedKey = Encryption.getEncryptedString(key, secretKey);
        String encryptedSecret = Encryption.getEncryptedString(secret, secretKey);

        if (encryptedKey.isEmpty() || encryptedSecret.isEmpty()) {
            onError();
        } else {
            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
            editor.putString(WALLET_KEY, encryptedKey).apply();
            editor.putString(WALLET_SECRET, encryptedSecret).apply();
            onStored();
        }
    }

    public abstract void onStored();

    public abstract void onError();
}