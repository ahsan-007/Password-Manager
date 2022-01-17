package com.example.passwordmanager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasswordsRVAdapter extends RecyclerView.Adapter<PasswordsRVAdapter.PasswordViewHolder> {
    List<Password> passwords;

    public PasswordsRVAdapter(List<Password> passwords) {
        this.passwords = passwords;
    }

    @NonNull
    @Override
    public PasswordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.password,parent,false);
        return new PasswordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordViewHolder holder, int position) {
        holder.data = passwords.get(position);
        holder.icon.setImageResource(holder.data.icon);
        //holder.icon.setImageResource(R.drawable.google);
        holder.accountType.setText(holder.data.accountType);
    }

    @Override
    public int getItemCount() {
        return passwords.size();
    }

    public class PasswordViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView accountType;
        Password data;
        public PasswordViewHolder(@NonNull View itemView) {
            super(itemView);
            this.icon = itemView.findViewById(R.id.icon);
            this.accountType = itemView.findViewById(R.id.accType);
            //itemView.setBackground(R.drawable.custom_rectangle);
        }

    }
}
