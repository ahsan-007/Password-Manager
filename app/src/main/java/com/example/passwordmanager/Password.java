package com.example.passwordmanager;


public class Password {
    int icon;
    String email;
    String userName;
    String accountType;
    String password;
    public static Icons icons = new Icons();

    public Password(String userName, String email, String password, String accountType) {
        this.email = email;
        this.userName = userName;
        this.accountType = accountType;
        this.password = password;
        this.icon = icons.getIcon(accountType);
    }
}
//class Password{
//    public String username;
//    public String email;
//    public String password;
//    public String account;
//    public int ID;
//    public Password(String u,String e,String p,String a)
//    {
//        ID=0;
//        username=u;
//        email=e;
//        password=p;
//        account=a;
//    }
//}