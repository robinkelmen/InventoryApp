package com.example.echoseedlinginventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.Layout;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddItem extends AppCompatActivity {

    private RecyclerView recyclerView;
    private static TagViewAdapter adapter;
    private static List<String> tagList;
    private static List<Integer> tagIDs;
    private List<String> languageList;
    private List<String> otherNameList;
    private Button addName, submit,cancel, tagButt;
    private EditText mainName, price,lowNum,language, otherName;
    private List<String> checkTags;
    private List<String> allItems;
    private static StringRequest request;
    private static RequestQueue requestQueue;
    private static List<Integer> ItemID;
    private static ExpandableRelativeLayout exLTags;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

      requestQueue = Volley.newRequestQueue(this);
        tagList = new ArrayList<>();
        ItemID = new ArrayList<>();
        tagIDs = new ArrayList<>();
        languageList = new ArrayList<>();
        otherNameList = new ArrayList<>();
        checkTags = new ArrayList<>();



        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add New Item");
        actionBar.setDisplayHomeAsUpEnabled(true);



        recyclerView =  findViewById(R.id.tag_recycler_add);

        adapter = new TagViewAdapter(this,tagList, 1);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(5), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        makeTags();
        adapter.notifyDataSetChanged();

        tagButt = findViewById(R.id.expand_tags_butt);
        exLTags = findViewById(R.id.show_tags_add_item);
        exLTags.collapse();
        addName = findViewById(R.id.add_other_name);
        language = findViewById(R.id.language_field);
        otherName = findViewById(R.id.other_name_field);
        submit = findViewById(R.id.submit_new_item);
        cancel = findViewById(R.id.cancel_new_item);
       mainName = findViewById(R.id.main_name);
       price = findViewById(R.id.add_price);
       lowNum = findViewById(R.id.too_low);

        addName.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                String languageString = language.getText().toString();
                String otherNameString = otherName.getText().toString();
                if(!languageString.equals("") && !otherNameString.equals("")){
                    otherName.setHintTextColor(Color.LTGRAY);
                    language.setHintTextColor(Color.LTGRAY);
                    if(languageList.contains(languageString)) {

                        Toast.makeText(getApplicationContext(), "Language Already Exists", Toast.LENGTH_SHORT).show();
                        language.setHintTextColor(Color.RED);
                        language.setHint("Enter New Language");

                    }
                   if (otherNameList.contains(otherNameString)) {
                       otherName.setHintTextColor(Color.RED);
                       otherName.setHint("Enter New Name");

                       Toast.makeText(getApplicationContext(), "Name Already Exists", Toast.LENGTH_SHORT).show();
                    }
                   if(!otherNameList.contains(otherNameString) && !languageList.contains(languageString))
                        {
                        otherName.setHintTextColor(Color.LTGRAY);
                        language.setHintTextColor(Color.LTGRAY);
                        languageList.add(languageString);
                        otherNameList.add(otherNameString);
                        Toast.makeText(getApplicationContext(), "Language: " + languageString + ": Name: " + otherNameString + " added", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    if(languageString.equals("")) {
                        Toast.makeText(getApplicationContext(), "No Language Entered", Toast.LENGTH_SHORT).show();
                        language.setHintTextColor(Color.RED);
                    }
                    if (otherNameString.equals("")){
                        Toast.makeText(getApplicationContext(), "No Other Name Entered", Toast.LENGTH_SHORT).show();
                        otherName.setHintTextColor(Color.RED);
                    }

                }


            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder build = new AlertDialog.Builder(view.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Adding New Item")
                        .setMessage("Are you sure you want to close this activity?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AddItem.super.onBackPressed();
                            }

                        })
                        .setNegativeButton("No", null);
                AlertDialog cancelPressed = build.create();
                cancelPressed.show();

            }


        });
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {



                final String mainNameString = mainName.getText().toString();
                final String priceString = price.getText().toString();
                final String lowNumString = lowNum.getText().toString();
            if(!mainNameString.equals("") && !priceString.equals("")  && !lowNumString.equals("")){
                AlertDialog.Builder build = new AlertDialog.Builder(view.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Adding New Item")
                        .setMessage("Are you sure you want to add this Item?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                checkTags = TagViewAdapter.checkedTags;
                                addItem(mainNameString, Integer.parseInt(priceString), Integer.parseInt(lowNumString), checkTags,otherNameList,languageList);

                                AddItem.super.onBackPressed();
                            }

                        })
                        .setNegativeButton("No", null);
                AlertDialog submitPressed = build.create();
                submitPressed.show();

            }else{
                Toast.makeText(getApplicationContext(), "Fill in Highlighted Fields", Toast.LENGTH_SHORT).show();
                if (mainNameString.equals("")) {

                    mainName.setHintTextColor(Color.RED);
                    mainName.setHint("Please Enter Name");
                }

                if (priceString.equals("")) {

                    price.setHintTextColor(Color.RED);
                    price.setHint("500");
                }
                if (lowNumString.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Indicated The Lowest Amount an Item Should Be", Toast.LENGTH_SHORT).show();
                    lowNum.setHintTextColor(Color.RED);
                    lowNum.setHint("50");
                }
            }



            }
        });




    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkConnection.checkConnection(this);
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private TextView createNewTextView(String language, String name) {
        final TextView textView = new TextView(this.getApplicationContext());

        textView.setText("Langauge: " + language + " Other name: " + name);
        return textView;
    }




    private void addItem(String itemName, double itemPrice, int lowNum, List<String> checkTags, List<String> otherNameList, List<String> languageList){

        DBConnect.addItem(this, itemName,itemPrice,lowNum, checkTags, otherNameList, languageList);

    }
    public  void showTags(View view){


        if(!exLTags.isExpanded()){
            tagButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black_24dp,0);
        }else{
            tagButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_black_24dp,0);
        }
        exLTags.toggle();

    }

    public void makeTags(){

        DBConnect.setRecyclerViewAdapter(adapter);
        DBConnect.getTags(this, tagList, tagIDs);
        Collections.sort(tagList);
    }



}
