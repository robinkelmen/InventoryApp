package com.example.echoseedlinginventory;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Intent;

public class ManageFragment extends Fragment {

    Button addButt;
    Button addTagsButt;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View manageView =  inflater.inflate(R.layout.fragment_manage, null);

        addButt = manageView.findViewById(R.id.add_button);
        addTagsButt = manageView.findViewById(R.id.add_tags);


        addButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), AddItem.class);
                getActivity().startActivity(myIntent);
            }
        });
        addTagsButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = new Intent(getActivity(), AddTags.class);
                getActivity().startActivity(mIntent);
            }
        });


        return  manageView;
    }



}
