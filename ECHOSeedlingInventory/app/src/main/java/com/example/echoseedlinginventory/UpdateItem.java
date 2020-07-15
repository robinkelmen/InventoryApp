package com.example.echoseedlinginventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Collections;

public class UpdateItem extends AppCompatActivity {


    public TextView actTitle;
    public EditText mainName,edit_price,edit_low_num, language, otherName;
     public ExpandableRelativeLayout exLNames;
     public ExpandableRelativeLayout exLPrice;
     public ExpandableRelativeLayout exLTags;
     public ExpandableRelativeLayout exLAmount;
     public  Button tagButt;
     public  Button amountButt;
     public Button update_other_names;
     public Button priceButt;
     public Button addNameButt;
     public Button submitButt;
     public Button cancelButt;
     public RecyclerView namesView;
     public RecyclerView tagsView;
     public ArrayList<String> currentTags;
     public ArrayList<String> allTags;
     public ArrayList<Integer> allTagIDs;
     public ArrayList<String> currentNames;
     public Item myItem;
     public NumberPicker np;
     public TextView currentAmount;
     public int newAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_item);
        ArrayList<Item> item = (ArrayList<Item>) getIntent().getSerializableExtra("item");
        myItem = item.get(0);
        currentNames = new ArrayList<>();
        allTagIDs = new ArrayList<>();
        allTags = new ArrayList<>();


        actTitle = findViewById(R.id.update_title);
        actTitle.setText("Edit " + myItem.getMainName());
        mainName = findViewById(R.id.update_main_name);
        edit_price = findViewById(R.id.update_add_price);
        edit_low_num = findViewById(R.id.update_too_low);
        language = findViewById(R.id.update_language_field);
        otherName = findViewById(R.id.update_other_name_field);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Update Item");
        actionBar.setDisplayHomeAsUpEnabled(true );

        //Buttons
        update_other_names = findViewById(R.id.update_other_name_butt);
        priceButt = findViewById(R.id.update_price);
        tagButt = findViewById(R.id.update_tags);
        addNameButt = findViewById(R.id.update_add_other_name);
        submitButt = findViewById(R.id.update_submit);
        cancelButt = findViewById(R.id.update_cancel);
        amountButt = findViewById(R.id.update_amount_butt);


        //recyclerviews
        namesView = findViewById(R.id.edit_name_recycler);
        tagsView = findViewById(R.id.edit_tags_recycler);

        final TagViewAdapter namesAdapter = new TagViewAdapter(this, currentNames, 3);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        namesView.setLayoutManager(mLayoutManager);
        namesView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(10), 1));
        namesView.setItemAnimator(new DefaultItemAnimator());
        namesView.setAdapter(namesAdapter);
        makeNames();
        namesAdapter.notifyDataSetChanged();

        TagViewAdapter tagsAdapter = new TagViewAdapter(this, allTags, 4);
        RecyclerView.LayoutManager TLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);//LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);//GridLayoutManager(this, 3);
        tagsView.setLayoutManager(TLayoutManager);
        tagsView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(5), 1));
        tagsView.setItemAnimator(new DefaultItemAnimator());
        tagsView.setAdapter(tagsAdapter);
         makeTags();
         tagsAdapter.notifyDataSetChanged();




        np = findViewById(R.id.update_amount_picker);
        currentAmount = findViewById(R.id.update_item_amount_text);
        currentAmount.setText(myItem.getAmountAvailable()+"");
        np.setMaxValue(1000);

        exLNames = findViewById(R.id.expand_other_names);
        exLPrice = findViewById(R.id.expand_price);
        exLTags = findViewById(R.id.expand_tags);
        exLAmount = findViewById(R.id.edit_amount);
        exLTags.collapse();
        exLNames.collapse();
        exLPrice.collapse();

        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                newAmount = newVal;


            }
        });
        submitButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                
                Toast.makeText(getApplicationContext(), newAmount+"", Toast.LENGTH_LONG).show();
            }
        });
        cancelButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateItem.super.onBackPressed();
            }
        });
        addNameButt.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

                String languageString = language.getText().toString();
                String otherNameString = otherName.getText().toString();
                if(!languageString.equals("") && !otherNameString.equals("")){
                    otherName.setHintTextColor(Color.LTGRAY);
                    language.setHintTextColor(Color.LTGRAY);
                    Toast.makeText(getApplicationContext(), languageString, Toast.LENGTH_SHORT).show();
                    if(myItem.getLanguages().contains(languageString)) {

                        Toast.makeText(getApplicationContext(), "Language Already Exists", Toast.LENGTH_SHORT).show();
                        language.setHintTextColor(Color.RED);
                        language.setHint("Enter New Language");

                    }
                    if (myItem.getOtherNames().contains(otherNameString)) {
                        otherName.setHintTextColor(Color.RED);
                        otherName.setHint("Enter New Name");

                        Toast.makeText(getApplicationContext(), "Name Already Exists", Toast.LENGTH_SHORT).show();
                    }
                    if(!myItem.getOtherNames().contains(otherNameString) && !myItem.getLanguages().contains(languageString))
                    {
                        otherName.setHintTextColor(Color.LTGRAY);
                        language.setHintTextColor(Color.LTGRAY);
                        myItem.getLanguages().add(languageString);
                        myItem.getOtherNames().add(otherNameString);
                        makeNames();
                        namesAdapter.notifyDataSetChanged();
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



    }

    @Override
    protected void onStart() {
        super.onStart();
        NetworkConnection.checkConnection(this);
    }

    public  void showOtherNames(View view){


        if(!exLNames.isExpanded()){
            update_other_names.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black_24dp,0);
        }else{
            update_other_names.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_black_24dp,0);
        }
        exLNames.toggle();

    }
    public void showPriceAndLowCount(View view){

        if(!exLPrice.isExpanded()){
            priceButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black_24dp,0);
        }else{
            priceButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_black_24dp,0);
        }
        exLPrice.toggle();


    }
    public void showCheckedUncheckedTags(View view){
        if(!exLTags.isExpanded()){
            tagButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black_24dp,0);
        }else{
            tagButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_black_24dp,0);
        }
        exLTags.toggle();

    }
    public void showNumberPicker(View view){
        if(!exLAmount.isExpanded()){
            amountButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_up_black_24dp,0);
        }else{
            amountButt.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_keyboard_arrow_down_black_24dp,0);
        }
        exLAmount.toggle();
    }


    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    private void makeNames(){
        ArrayList<String> names = myItem.getOtherNames();
        ArrayList<String> languages = myItem.getLanguages();



        for (int i = 0; i < names.size(); i++){
            String toAdd = "Language: " + languages.get(i)+ " || Name: " + names.get(i);
            if(!currentNames.contains(toAdd))
                currentNames.add("Language: " + languages.get(i)+ " || Name: " + names.get(i));
        }


        Collections.sort(currentNames);

    }

    private void makeTags(){
        DBConnect.getTags(this,allTags,allTagIDs );
        Collections.sort(allTags);
        TagViewAdapter.setPreChecked(myItem.getTags());



    }





}
