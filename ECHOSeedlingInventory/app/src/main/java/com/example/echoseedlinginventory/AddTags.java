package com.example.echoseedlinginventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddTags extends AppCompatActivity {

    public EditText tag;
    public Button addTagButt;
    public ArrayList<String> newTagsList;
    public static ArrayList<String> oldTagList;
    public static ArrayList<Integer> oldTagIDs;
    public RequestQueue requestQueue;
    public StringRequest request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tags);

        newTagsList = new ArrayList<>();

        oldTagList = new ArrayList<>();
        oldTagIDs = new ArrayList<>();
        DBConnect.getTags(this,oldTagList, oldTagIDs);
        tag = findViewById(R.id.tag_field);
        addTagButt = findViewById(R.id.add_tag);
        requestQueue =  Volley.newRequestQueue(this);


        addTagButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(!tag.getText().toString().isEmpty()) {
                   String tagString = tag.getText().toString();

                   if (newTagsList.contains(tagString) || oldTagList.contains(tagString)) {
                       Toast.makeText(view.getContext(), tagString + " already Exists", Toast.LENGTH_SHORT).show();
                   } else {
                       DBConnect.addTags(view.getContext(),  tagString);
                       Toast.makeText(view.getContext(), "Adding: " + tagString, Toast.LENGTH_SHORT).show();
                       newTagsList.add(tagString);

                       tag.setHint("Enter New Tag");
                   }
               }else{
                   Toast.makeText(view.getContext(), "Please Enter Tag Name", Toast.LENGTH_SHORT).show();
               }


            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Tags");
        actionBar.setDisplayHomeAsUpEnabled(true );




    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Toast.makeText(this,"Returning To Manage Inventory", Toast.LENGTH_SHORT).show();

            onBackPressed();

        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }





}
