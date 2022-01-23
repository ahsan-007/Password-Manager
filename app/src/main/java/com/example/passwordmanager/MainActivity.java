package com.example.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {
    private EditText textInputView;
    private TextView textOutputView;
    private Boolean readyToEncrypt = false;
    private final CryptographyManager cryptographyManager = CryptographyManagerImpl.CryptographyManager();
    private String secretKeyName;
    private byte[] ciphertext;
    private byte[] initializationVector;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    Button passwords;
    Button accountDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        secretKeyName = "biometric_sample_encryption_key";
        biometricPrompt = createBiometricPrompt();
        promptInfo = createPromptInfo();
        biometricPrompt.authenticate(promptInfo);

        passwords = findViewById(R.id.button3);
        accountDetails = findViewById(R.id.button4);

        passwords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Passwords.class);
                startActivity(intent);
            }
        });

        accountDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AccountDetails.class);
                startActivity(intent);
            }
        });

    }

    private BiometricPrompt createBiometricPrompt() {
        Executor executor = ContextCompat.getMainExecutor(this);
        return (new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(),
                        "Authentication error: " + errString, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(),
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, Passwords.class);
                startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(getApplicationContext(), "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }));
    }

    private BiometricPrompt.PromptInfo createPromptInfo() {
        return new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setAllowedAuthenticators(BiometricManager.Authenticators.DEVICE_CREDENTIAL | BiometricManager.Authenticators.BIOMETRIC_STRONG)
                .setConfirmationRequired(false)
                .build();
    }

    private void checkBiometricSupport() {
        String info = "";
        BiometricManager manager = BiometricManager.from(this);
        switch (manager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                info = "App can authenticate using biometrics";
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                info = "No biometric feature is available on this device";
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                info = "Biometric feature is currently unavailable";
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                info = "Need to register atleast one fingerprint";
                break;
            default:
                info = "Unknown Cause";
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                break;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                break;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                break;
        }
        Toast.makeText(getApplicationContext(),
                info, Toast.LENGTH_LONG).show();
    }

    private void authenticateToEncrypt() {
        readyToEncrypt = true;
        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager
                .BIOMETRIC_SUCCESS) {
            Cipher cipher = cryptographyManager.getInitializedCipherForEncryption(secretKeyName);
            biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));
        }
    }

    private void authenticateToDecrypt() {
        readyToEncrypt = false;
        if (BiometricManager.from(this).canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK | BiometricManager.Authenticators.DEVICE_CREDENTIAL) == BiometricManager
                .BIOMETRIC_SUCCESS) {
            Log.i("INITIALIZATION VECTOR1", initializationVector.toString());
            Cipher cipher = cryptographyManager.getInitializedCipherForDecryption(secretKeyName, initializationVector);
            biometricPrompt.authenticate(promptInfo, new BiometricPrompt.CryptoObject(cipher));

        }

    }

    private void processData(BiometricPrompt.CryptoObject cryptoObject) {
        String data = "";
        if (readyToEncrypt) {
            String text = textInputView.getText().toString();
            EncryptedData encryptedData = cryptographyManager.encryptData(text, Objects.requireNonNull(cryptoObject.getCipher()));

            ciphertext = encryptedData.getCiphertext();
            initializationVector = encryptedData.getInitializationVector();

            data = new String(ciphertext, StandardCharsets.UTF_8);
        } else {
            data = cryptographyManager.decryptData(ciphertext, Objects.requireNonNull(cryptoObject.getCipher()));
        }
        textOutputView.setText(data);
    }
}