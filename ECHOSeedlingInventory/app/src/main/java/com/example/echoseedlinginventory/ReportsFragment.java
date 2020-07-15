package com.example.echoseedlinginventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ReportsFragment extends Fragment {



    public EditText email;

    public Button send;

    public CheckedTextView salesCheck;
    public CheckedTextView donationsCheck;
    public CheckedTextView deathCheck;
    public CheckedTextView fullInventoryCheck;


    @Nullable
    @Override


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_reports, null);


       email = view.findViewById(R.id.email);
       send = view.findViewById(R.id.send_email);
       salesCheck = view.findViewById(R.id.Sales_check);
       donationsCheck = view.findViewById(R.id.Donations_check);
       deathCheck = view.findViewById(R.id.Deaths_check);
       fullInventoryCheck = view.findViewById(R.id.Full_inventory_check);

       salesCheck.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(salesCheck.isChecked()) salesCheck.setChecked(false);
               else salesCheck.setChecked(true);
           }
       });
       donationsCheck.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(donationsCheck.isChecked()) donationsCheck.setChecked(false);
               else donationsCheck.setChecked(true);
           }
       });
       deathCheck.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(deathCheck.isChecked()) deathCheck.setChecked(false);
               else deathCheck.setChecked(true);
           }
       });
       fullInventoryCheck.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(fullInventoryCheck.isChecked()) fullInventoryCheck.setChecked(false);
               else fullInventoryCheck.setChecked(true);
           }
       });

       send.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               DBConnect.sendEmail(getContext(),email.getText().toString());
           }
       });



       return view;

    }
}
