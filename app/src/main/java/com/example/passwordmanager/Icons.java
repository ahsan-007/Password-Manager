package com.example.passwordmanager;

import com.example.passwordmanager.Icon;
import com.example.passwordmanager.Password;
import com.example.passwordmanager.R;

import java.util.ArrayList;

public class Icons {
    public ArrayList<Icon> allIcons;
    public Icons() {
        this.allIcons = new ArrayList<>();
        allIcons.add(new Icon(R.drawable.google,"Google"));
        allIcons.add(new Icon(R.drawable.amazon,"Amazon"));
        allIcons.add(new Icon(R.drawable.facebook,"Facebook"));
        allIcons.add(new Icon(R.drawable.instagram,"Instagram"));
        allIcons.add(new Icon(R.drawable.snapchat,"Snapchat"));
        allIcons.add(new Icon(R.drawable.slack,"Slack"));
        allIcons.add(new Icon(R.drawable.twitter,"Twitter"));
        allIcons.add(new Icon(R.drawable.gmail,"Gmail"));
        allIcons.add(new Icon(R.drawable.docker,"Docker"));
        allIcons.add(new Icon(R.drawable.linked_in,"Linked In"));
        allIcons.add(new Icon(R.drawable.trello,"Trello"));
        allIcons.add(new Icon(R.drawable.fiverr,"Fiverr"));
        allIcons.add(new Icon(R.drawable.upwork,"Upwork"));
        allIcons.add(new Icon(R.drawable.yahoo,"Yahoo"));

    }
    public int getIcon(String accType){
        for (int i=0;i<allIcons.size();i=i+1){
            if(accType.equalsIgnoreCase(allIcons.get(i).getAccountType())){
                return allIcons.get(i).getIcon();
            }
        }
        return R.drawable.default_account;
    }
}
