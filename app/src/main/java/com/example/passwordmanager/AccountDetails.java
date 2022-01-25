package com.example.passwordmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;

public class AccountDetails extends AppCompatActivity {

    Spinner spinner;
    Button accountDetailsBtn;
    Button deleteDetails;
    EditText userName;
    EditText email;
    EditText password;
    ImageButton togglePassword;

    DBhandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        dbHandler = new DBhandler(this);

        spinner = findViewById(R.id.account_types);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,R.array.account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        userName = findViewById(R.id.userName);
        email = findViewById(R.id.email);
        password=findViewById(R.id.password);
        accountDetailsBtn = findViewById(R.id.accountDetailsBtn);

        togglePassword = findViewById(R.id.togglePassword);
        togglePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getTransformationMethod() != null) {
                    password.setTransformationMethod(null);
                } else {
                    password.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });


        deleteDetails=findViewById(R.id.delDetails);
        deleteDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.delete(email.getText().toString(),spinner.getSelectedItem().toString());
                finish();
            }
        });

        Intent intent = getIntent();
        String accType = intent.getStringExtra("accountType");
        if(accType==null){
            accountDetailsBtn.setText("Save Details");
            deleteDetails.setVisibility(View.INVISIBLE);
        }
        else {
            Password pass = dbHandler.getPassword(intent.getStringExtra("accountType"));

            userName.setText(pass.userName);
            email.setText(pass.email);
            password.setText(pass.password);
            int ind = Arrays.asList((getResources().getStringArray(R.array.account_types))).
                    indexOf(pass.accountType);
            spinner.setSelection(ind);

            userName.setEnabled(false);
            email.setEnabled(false);
            spinner.setEnabled(false);

        }

        accountDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if(userName.getText().toString().equals("")){
                        Toast.makeText(AccountDetails.this,"User Name Missing!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(email.getText().toString().equals("")){
                        Toast.makeText(AccountDetails.this,"Email Missing!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(password.getText().toString().equals("")){
                        Toast.makeText(AccountDetails.this,"Password Missing!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    if(spinner.getSelectedItem().equals("Account Type")){
                        Toast.makeText(AccountDetails.this,"Select Account Type!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    int index = email.getText().toString().indexOf("@");
                    if (index == -1 || index == email.getText().toString().length()-1){
                        Toast.makeText(AccountDetails.this,"Invalid Email!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    Password pass = new Password(userName.getText().toString(),
                            email.getText().toString(),
                            password.getText().toString(),
                            spinner.getSelectedItem().toString()
                    );

                    if(accountDetailsBtn.getText().toString().equalsIgnoreCase("save details")) {
                        if(dbHandler.isAccountPresent(pass.accountType)){
                            Toast.makeText(AccountDetails.this,"Account Already Registered!",Toast.LENGTH_LONG).show();
                            return;
                        }else {
                            dbHandler.insert(pass);
                        }
                    }else{
                        dbHandler.update(pass);
                    }
                    finish();
            }
        });

    }
}