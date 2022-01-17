package com.example.passwordmanager;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.annotation.RequiresApi;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;

public interface CryptographyManager {
    Cipher getInitializedCipherForEncryption(@NotNull String keyName);
    Cipher getInitializedCipherForDecryption(@NotNull String keyName, byte[] initializationVector);
}

final class CryptographyManagerImpl implements CryptographyManager {
    @RequiresApi(23)
    @NotNull
    public Cipher getInitializedCipherForEncryption(@NotNull String keyName) {
        Cipher cipher = this.getCipher();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                generateSecretKey(new KeyGenParameterSpec.Builder(
                        keyName,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                        .setUserAuthenticationRequired(true)
                        .setUserAuthenticationParameters(100,
                                KeyProperties.AUTH_BIOMETRIC_STRONG | KeyProperties.AUTH_DEVICE_CREDENTIAL)
                        .build());
            }
        }
        SecretKey secretKey = this.getSecretKey(keyName);
        try {
            assert cipher != null;
            cipher.init(1, (Key) secretKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    @RequiresApi(23)
    @NotNull
    public Cipher getInitializedCipherForDecryption(@NotNull String keyName, byte[] initializationVector) {
//        Intrinsics.checkNotNullParameter(keyName, "keyName");
//        Intrinsics.checkNotNullParameter(initializationVector, "initializationVector");
        Log.i("INITIALIZATION VECTOR2", initializationVector.toString());
        Cipher cipher = this.getCipher();
        SecretKey secretKey = this.getSecretKey(keyName);
        try {
            assert cipher != null;
            Log.i("INITIALIZATION VECTOR3", initializationVector.toString());
            Log.i("INITIALIZATION VECTOR4", String.valueOf(initializationVector.length));
            cipher.init(2, (Key) secretKey, (AlgorithmParameterSpec) (new IvParameterSpec(initializationVector)));

        } catch (InvalidAlgorithmParameterException | InvalidKeyException e) {
            e.printStackTrace();
        }
        return cipher;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void generateSecretKey(KeyGenParameterSpec keyGenParameterSpec) {
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
        }
        try {
            assert keyGenerator != null;
            keyGenerator.init(keyGenParameterSpec);
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        keyGenerator.generateKey();
    }

    private SecretKey getSecretKey(@NotNull String keyName) {
        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
        try {
            if (keyStore != null) {
                keyStore.load(null);
            }
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
        try {
            assert keyStore != null;
            return ((SecretKey) keyStore.getKey(keyName, null));
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private Cipher getCipher() {
        try {
            return Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
