package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class Passwords extends AppCompatActivity {

    List<Password> passwords;
    RecyclerView passwordsRView;
    RecyclerView.Adapter RVAdapter;
    RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        passwords = new ArrayList<>();
        passwords.add(new Password(R.drawable.google,"Google","1234"));
        passwords.add(new Password(R.drawable.amazon,"Amazon","1234"));
        passwords.add(new Password(R.drawable.facebook,"Facebook","1234"));
        passwords.add(new Password(R.drawable.instagram,"Instagram","1234"));
        passwords.add(new Password(R.drawable.snapchat,"Snapchat","1234"));
        passwords.add(new Password(R.drawable.slack,"Slack","1234"));
        passwords.add(new Password(R.drawable.twitter,"Twitter","1234"));
        passwords.add(new Password(R.drawable.gmail,"Gmail","1234"));

        passwordsRView = findViewById(R.id.passwordsRV);
        RVAdapter = new PasswordsRVAdapter(passwords);
        passwordsRView.setAdapter(RVAdapter);

        layoutManager = new LinearLayoutManager(this);
        passwordsRView.setLayoutManager(layoutManager);
    }
}