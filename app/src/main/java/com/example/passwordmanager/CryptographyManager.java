package com.example.passwordmanager;

import javax.crypto.Cipher;
import org.jetbrains.annotations.NotNull;

public interface CryptographyManager {
    Cipher getInitializedCipherForEncryption(@NotNull String keyName);
    Cipher getInitializedCipherForDecryption(@NotNull String keyName, byte[] initializationVector);
}
