package com.example.passwordmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Passwords extends AppCompatActivity {

    List<Password> passwords;
    RecyclerView passwordsRView;
    RecyclerView.Adapter RVAdapter;
    RecyclerView.LayoutManager layoutManager;
    CardView addAccount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passwords);

        passwords = new DBhandler(this).getPasswords();

        passwordsRView = findViewById(R.id.passwordsRV);
        RVAdapter = new PasswordsRVAdapter(passwords);
        passwordsRView.setAdapter(RVAdapter);

        layoutManager = new LinearLayoutManager(this);
        passwordsRView.setLayoutManager(layoutManager);

        addAccount = findViewById(R.id.addAccount);
        addAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Passwords.this,AccountDetails.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();

        passwords = new DBhandler(Passwords.this).getPasswords();

        passwordsRView = findViewById(R.id.passwordsRV);
        RVAdapter = new PasswordsRVAdapter(passwords);
        passwordsRView.setAdapter(RVAdapter);

        layoutManager = new LinearLayoutManager(this);
        passwordsRView.setLayoutManager(layoutManager);
    }
}