package com.example.passwordmanager;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Log;

import androidx.annotation.RequiresApi;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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
    EncryptedData encryptData(@NotNull String plaintext, @NotNull Cipher cipher);
    String decryptData(byte[] ciphertext, @NotNull Cipher cipher);
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
        Cipher cipher = this.getCipher();
        SecretKey secretKey = this.getSecretKey(keyName);
        try {
            assert cipher != null;
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

    @NotNull
    public EncryptedData encryptData(@NotNull String plaintext, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter(plaintext, "plaintext");
        Intrinsics.checkNotNullParameter(cipher, "cipher");
        Charset var10001 = StandardCharsets.UTF_8;
        Intrinsics.checkNotNullExpressionValue(var10001, "Charset.forName(\"UTF-8\")");
        boolean var6 = false;
        byte[] var7 = plaintext.getBytes(var10001);
        Intrinsics.checkNotNullExpressionValue(var7, "(this as java.lang.String).getBytes(charset)");
        byte[] ciphertext = new byte[0];
        try {
            ciphertext = cipher.doFinal(var7);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        Intrinsics.checkNotNullExpressionValue(ciphertext, "ciphertext");
        byte[] var10003 = cipher.getIV();
        Intrinsics.checkNotNullExpressionValue(var10003, "cipher.iv");
        return new EncryptedData(ciphertext, var10003);
    }

    @NotNull
    public String decryptData(byte[] ciphertext, @NotNull Cipher cipher) {
        Intrinsics.checkNotNullParameter(ciphertext, "ciphertext");
        Intrinsics.checkNotNullParameter(cipher, "cipher");
        byte[] plaintext = new byte[0];
        try {
            plaintext = cipher.doFinal(ciphertext);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        Intrinsics.checkNotNullExpressionValue(plaintext, "plaintext");
        Charset var10000 = StandardCharsets.UTF_8;
        Intrinsics.checkNotNullExpressionValue(var10000, "Charset.forName(\"UTF-8\")");
        boolean var6 = false;
        return new String(plaintext, var10000);
    }
}
