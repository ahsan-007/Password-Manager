package com.example.passwordmanager;


public class Password {
    int icon;
    String accountType;
    String password;

    public Password(int icon, String accountType, String password) {
        this.icon = icon;
        this.accountType = accountType;
        this.password = password;
    }

    public int getIcon() {
        return icon;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getPassword() {
        return password;
    }
}
