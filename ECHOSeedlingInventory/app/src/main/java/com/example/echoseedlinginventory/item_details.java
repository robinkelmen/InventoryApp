package com.example.echoseedlinginventory;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class item_details extends AppCompatActivity {

    public TextView title, count, price,othernamesView;
    public ImageView itemImage;
    public Item item;
    public Button backButt;
    private RecyclerView recyclerView;
    private static ArrayList<String> tagList;
    private static ArrayList<Integer> tagIds;
    private static ArrayList<String> othernames;
    private static ArrayList<String> language;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);
        tagIds = new ArrayList<>();
        tagIds = new ArrayList<>();
        //DBConnect.getTags(this,"http://192.168.1.23/get_tags.php",tagList,tagIds);

        title = findViewById(R.id.detail_title);
        count = findViewById(R.id.detail_count);
        price = findViewById(R.id.detail_price);
        itemImage = findViewById(R.id.detail_thumbnail);
        othernamesView = findViewById(R.id.detail_othernames);


        Intent intent = getIntent();



        recyclerView = findViewById(R.id.detail_tag_recycler);
        tagList = new ArrayList<>((ArrayList<String>)intent.getSerializableExtra("tag"));
        TagViewAdapter adapter = new TagViewAdapter(this, tagList, 2);
        RecyclerView.LayoutManager mLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(dpToPx(3), 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        othernames = new ArrayList<>((ArrayList<String>) intent.getSerializableExtra("names"));
        language = new ArrayList<>((ArrayList<String>) intent.getSerializableExtra("languages"));
        String displaynames = " Other Names: \n";

        if(othernames.size() == 0){
            displaynames = "No Other Names Available for this item ";
        }else {

            for (int i = 0; i < othernames.size(); i++) {
                displaynames = displaynames + "Language: " + language.get(i) + " : Name: " + othernames.get(i) + "\n";

            }
        }
        othernamesView.setText(displaynames);



        title.setText( intent.getStringExtra("title"));
        count.setText(intent.getStringExtra("count"));
        price.setText(intent.getStringExtra("price"));

        Bitmap _bitmap = BitmapFactory.decodeByteArray(
                intent.getByteArrayExtra("thumbnail"),0,intent.getByteArrayExtra("thumbnail").length);
        itemImage.setImageBitmap(_bitmap);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Item Details");
        actionBar.setDisplayHomeAsUpEnabled(true );








    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}
