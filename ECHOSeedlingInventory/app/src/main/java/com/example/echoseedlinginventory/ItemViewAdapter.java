package com.example.echoseedlinginventory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemViewAdapter extends RecyclerView.Adapter<ItemViewAdapter.MyViewHolder> implements Filterable {


   private Context mContext;
   private ArrayList<Item> internalItemList;
   private ArrayList<Item> fullList;
   private Bitmap image;
   private ViewGroup vg;





    public class MyViewHolder extends RecyclerView.ViewHolder implements PopupMenu.OnMenuItemClickListener{
        public TextView title, count, price, dialogSCount, dialogDCount;
        public ImageView itemImage;
        public Button editButt;
        public PopupMenu popupMenu;
        public View holderView;
        public NumberPicker saleNumberPicker;
        public NumberPicker deathNumberPicker;
        public LayoutInflater layoutInflater;
        public View dialogSaleView;
        public View dialogDeathView;
        public Item holderItem;




        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            count = (TextView) view.findViewById(R.id.count);
            price = (TextView) view.findViewById(R.id.price);
            itemImage = (ImageView) view.findViewById(R.id.thumbnail);
            editButt = (Button) view.findViewById(R.id.edit_from_card);


            holderView = view;
            layoutInflater = ListFragment.myInflater;



        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {


            switch (menuItem.getItemId()) {
                case R.id.pop_sale:

                    showDialog(dialogSaleView).show();
                    break;
                case R.id.pop_death:
                    showDialog(dialogDeathView).show();
                    break;
                case R.id.pop_edit:
                    Intent myIntent = new Intent(holderView.getContext(), UpdateItem.class);
                    ArrayList<Item> item = new ArrayList<>();
                    item.add(holderItem);
                    myIntent.putExtra("item", item);
                    holderView.getContext().startActivity(myIntent);
                    break;
                default:
                    break;

            }
            return true;
        }

        public Dialog showDialog( View toInflate){

                AlertDialog.Builder builder = new AlertDialog.Builder(holderView.getContext());
                // Get the layout inflater
                //LayoutInflater inflater = ListFragment.myInflater;

                // Inflate and set the layout for the dialog
                // Pass null as the parent view because its going in the dialog layout
                if(toInflate.getParent() != null){
                    ((ViewGroup)toInflate.getParent()).removeView(toInflate);
                }
                builder.setView(toInflate)

                        // Add action buttons
                        .setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                return builder.create();
            }




    }


    public ItemViewAdapter(Context mContext, ArrayList<Item> itemList) {
        this.mContext = mContext;
        Collections.sort(itemList);
        this.internalItemList = itemList;





    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_card, parent, false);
        vg = parent;
        fullList = new ArrayList<>(internalItemList);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Item item = internalItemList.get(position);
        holder.holderItem = item;
        holder.title.setText(item.getMainName());
        holder.count.setText(item.getAmountAvailable() + " available");
        holder.price.setText(item.getPrice() + " TZH");

        holder.dialogSaleView = holder.layoutInflater.inflate(R.layout.number_picker_dialog, null);


        holder.saleNumberPicker = holder.dialogSaleView.findViewById(R.id.amount_picker);
        holder.saleNumberPicker.setMinValue(0);
        holder.saleNumberPicker.setMaxValue(item.getAmountAvailable());
        holder.dialogSCount = holder.dialogSaleView.findViewById(R.id.list_item_amount_text);
        holder.dialogSCount.setText(item.getAmountAvailable()+"");

        holder.dialogDeathView = holder.layoutInflater.inflate(R.layout.death_number_picker_dialog,null);
        holder.deathNumberPicker = holder.dialogDeathView.findViewById(R.id.amount_picker_death);
        holder.deathNumberPicker.setMinValue(0);
        holder.deathNumberPicker.setMaxValue(item.getAmountAvailable());
        holder.dialogDCount = holder.dialogDeathView.findViewById(R.id.list_item_amount_text_death);
        holder.dialogDCount.setText(item.getAmountAvailable()+"");



        final Item entryItem = item;

        // loading item image using Glide library

              new DowloadImage(image, mContext).execute(item.getUrl());
        Glide.with(mContext).load(image).into(holder.itemImage);




        //Glide.with(mContext).load(image.).into(holder.itemImage);
        holder.itemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),item_details.class);
                ByteArrayOutputStream _bs = new ByteArrayOutputStream();
                intent.putExtra("title",entryItem.getMainName());
                intent.putExtra("count",entryItem.getAmountAvailable()  + " Available");
                intent.putExtra("price",entryItem.getPrice() + " TZH");

                Toast.makeText(mContext, entryItem.getTags()+"", Toast.LENGTH_LONG).show();

                intent.putExtra("tag",  entryItem.getTags());
                intent.putExtra("names",  entryItem.getOtherNames());
                intent.putExtra("languages",  entryItem.getLanguages());
                if(image != null) {

                    image.compress(Bitmap.CompressFormat.PNG, 50, _bs);

                }
                intent.putExtra("thumbnail", _bs.toByteArray());



                mContext.startActivity(intent);
            }
        });
        holder.editButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               holder.popupMenu = new PopupMenu(mContext, view);
               holder.popupMenu.setOnMenuItemClickListener(holder);
               holder.popupMenu.inflate(R.menu.item_pop_up);
               holder.popupMenu.show();
            }
        });


    }




    @Override
    public int getItemCount() {
        return internalItemList.size();
    }

    private Filter myFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Item> filteredList = new ArrayList<>();
            if(charSequence == null || charSequence.length() == 0){
                filteredList.addAll(fullList);
            }else{
                String filterpattern = charSequence.toString().toLowerCase().trim();

                for (Item item: fullList) {
                    if(item.getMainName().toLowerCase().contains(filterpattern)){
                        filteredList.add(item);

                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return  results;
        }
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            internalItemList.clear();

            internalItemList.addAll((ArrayList) filterResults.values);

            notifyDataSetChanged();

        }
    };
    @Override
    public Filter getFilter() {
        return myFilter;
    }

}
