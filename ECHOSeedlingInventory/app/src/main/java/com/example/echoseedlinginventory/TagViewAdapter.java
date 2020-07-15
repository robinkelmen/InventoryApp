package com.example.echoseedlinginventory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class TagViewAdapter extends RecyclerView.Adapter<TagViewAdapter.MyViewHolder> {

    private Context mContext;



    private List<String> tagList;

    public static List<String> checkedTags;
    public static ArrayList<String> checkedNames;
    public static ArrayList<String> uncheckedTags;
    public static ArrayList<String> newlyChecked;
    public static List<Integer> tagIDs;
    public static ArrayList<String> preChecked;
    public HashMap<String, Integer> keys;
    public int protocol;


    public TagViewAdapter(Context mContext, List<String> tagListN, int protocol) {
        this.mContext = mContext;
        tagList = tagListN;
        checkedTags = new ArrayList<>();
        checkedNames = new ArrayList<>();
        uncheckedTags = new ArrayList<>();
        newlyChecked = new ArrayList<>();
        preChecked = new ArrayList<>();
        tagIDs = new ArrayList<>();
        keys = new HashMap<>();
        this.protocol = protocol;
    }

    @NonNull
    @Override
    public TagViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_view, parent, false);

        return new TagViewAdapter.MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull final TagViewAdapter.MyViewHolder holder, final int position) {
            String tag = tagList.get(position);

           // keys.put(tag,position);

            holder.myTag.setText(tag);
            switch (protocol) {

                case 1: holder.myTag.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        if (holder.myTag.isChecked()) {
                            Objects.requireNonNull(holder).myTag.setChecked(false);
                            int tagPos = checkedTags.indexOf(holder.myTag.getText().toString());
                            String currentTag = holder.myTag.getText().toString();
                            checkedTags.remove(tagPos);
                            if (!checkedTags.contains(currentTag))
                                Toast.makeText(mContext, "Tag Removed", Toast.LENGTH_SHORT).show();
                        } else {
                            Objects.requireNonNull(holder).myTag.setChecked(true);
                            Toast.makeText(mContext, "New Tag Added", Toast.LENGTH_SHORT).show();
                            String checkedTag = holder.myTag.getText().toString();
                            checkedTags.add(checkedTag);
                        }
                    }
                });
                break;
                case 2:
                    holder.myTag.setChecked(true);
                    break;
                case 3:
                    holder.myTag.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            if (holder.myTag.isChecked()) {
                                Objects.requireNonNull(holder).myTag.setChecked(false);
                                int tagPos = checkedNames.indexOf(holder.myTag.getText().toString());
                                String currentTag = holder.myTag.getText().toString();
                                checkedNames.remove(tagPos);
                                if (!checkedNames.contains(currentTag))
                                    Toast.makeText(mContext, "Name Re-Added", Toast.LENGTH_SHORT).show();
                            } else {
                                Objects.requireNonNull(holder).myTag.setChecked(true);
                                Toast.makeText(mContext, "Name Removed", Toast.LENGTH_SHORT).show();
                                String checkedTag = holder.myTag.getText().toString();
                                checkedNames.add(checkedTag);
                            }
                        }
                    });
                    break;
                case 4:
                    if(preChecked.contains(holder.myTag.getText().toString()))
                        holder.myTag.setChecked(true);
                    holder.myTag.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            String currentTag = holder.myTag.getText().toString();
                            if (holder.myTag.isChecked() && newlyChecked.contains(currentTag)) {
                                Objects.requireNonNull(holder).myTag.setChecked(false);
                                int tagPos = newlyChecked.indexOf(currentTag);

                                newlyChecked.remove(tagPos);
                                if (!checkedNames.contains(currentTag))
                                    Toast.makeText(mContext, "Tag Removed", Toast.LENGTH_SHORT).show();
                            }else if(holder.myTag.isChecked() && !newlyChecked.contains(holder.myTag.getText().toString())){
                                Objects.requireNonNull(holder).myTag.setChecked(false);
                                Toast.makeText(mContext, "Old Tag Removed", Toast.LENGTH_SHORT).show();
                                uncheckedTags.add(currentTag);

                            } else{
                                Objects.requireNonNull(holder).myTag.setChecked(true);
                                Toast.makeText(mContext, "New Tag Added to Item", Toast.LENGTH_SHORT).show();
                                if(uncheckedTags.contains(currentTag))
                                    uncheckedTags.remove(currentTag);

                                newlyChecked.add(currentTag);
                            }
                        }
                    });
                    break;

                    default:
                        break;

            }



    }


    public List<String> getCheckedTags(){

        return this.checkedTags;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        public CheckedTextView myTag;
        public MyViewHolder(View view) {
            super(view);
            myTag = view.findViewById(R.id.tag_check);


        }

    }





    @Override
    public int getItemCount() {
        return tagList.size();
    }
    public static void setPreChecked(ArrayList<String> preChecked1){
        preChecked = preChecked1;
    }
}
