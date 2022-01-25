package com.example.passwordmanager;

public class Icon {
    int icon;
    String accountType;

    public Icon(int icon, String accountType) {
        this.icon = icon;
        this.accountType = accountType;
    }

    public int getIcon() {
        return icon;
    }

    public String getAccountType() {
        return accountType;
    }
}
