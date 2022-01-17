package com.example.passwordmanager;

import androidx.annotation.Nullable;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Objects;

public final class EncryptedData {
    @NotNull
    private final byte[] ciphertext;
    @NotNull
    private final byte[] initializationVector;

    public static boolean areEqual(Object first, Object second) {
        return Objects.equals(first, second);
    }

    public boolean equals(@Nullable Object other) {
        if ((EncryptedData) this == other) {
            return true;
        } else if (!areEqual(this.getClass(), other != null ? other.getClass() : null)) {
            return false;
        } else {
            byte[] var2 = this.ciphertext;
            byte[] var3 = ((EncryptedData) other).ciphertext;
            boolean var4 = false;
            if (!Arrays.equals(var2, var3)) {
                return false;
            } else {
                var2 = this.initializationVector;
                var3 = ((EncryptedData) other).initializationVector;
                var4 = false;
                return Arrays.equals(var2, var3);
            }
        }
    }


    public int hashCode() {
        byte[] var2 = this.ciphertext;
        boolean var3 = false;
        int result = Arrays.hashCode(var2);
        int var10000 = 31 * result;
        var2 = this.initializationVector;
        var3 = false;
        result = var10000 + Arrays.hashCode(var2);
        return result;
    }

    @NotNull
    public final byte[] getCiphertext() {
        return this.ciphertext;
    }

    @NotNull
    public final byte[] getInitializationVector() {
        return this.initializationVector;
    }

    public EncryptedData(@NotNull byte[] ciphertext, @NotNull byte[] initializationVector) {
//        super();
        this.ciphertext = ciphertext;
        this.initializationVector = initializationVector;
    }

    @NotNull
    public final byte[] component1() {
        return this.ciphertext;
    }

    @NotNull
    public final byte[] component2() {
        return this.initializationVector;
    }

    @NotNull
    public final EncryptedData copy(@NotNull byte[] ciphertext, @NotNull byte[] initializationVector) {
        return new EncryptedData(ciphertext, initializationVector);
    }

//    public static EncryptedData copy$default(EncryptedData var0, byte[] var1, byte[] var2, int var3, Object var4) {
//        if ((var3 & 1) != 0) {
//            var1 = var0.ciphertext;
//        }
//
//        if ((var3 & 2) != 0) {
//            var2 = var0.initializationVector;
//        }
//
//        return var0.copy(var1, var2);
//    }

    @NotNull
    public String toString() {
        return "EncryptedData(ciphertext=" + Arrays.toString(this.ciphertext) + ", initializationVector=" + Arrays.toString(this.initializationVector) + ")";
    }
}
