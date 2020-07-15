package com.example.echoseedlinginventory;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.res.Resources;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.CheckedOutputStream;
import java.util.zip.Inflater;

public class ListFragment extends Fragment {

    private RecyclerView recyclerView;
    public static ItemViewAdapter adapter;
    private ArrayList<Item> itemList;
    private SearchView searchView;
    static LayoutInflater myInflater;
    SwipeRefreshLayout swipeRefreshLayout;
    private boolean noInternet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View listView = inflater.inflate(R.layout.fragment_list, null);
        myInflater = inflater;


        itemList = new ArrayList<>();
        adapter = new ItemViewAdapter(this.getContext(),itemList);
        swipeRefreshLayout = listView.findViewById(R.id.swipe_refresh);
        if(NetworkConnection.checkConnection(getContext())){
            makeItems();
        }else{

        }

        adapter.notifyDataSetChanged();
        recyclerView =  listView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this.getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10)));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        searchView = listView.findViewById(R.id.search_view);

        //searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Toast.makeText(getContext(), "Refreshing Items... " , Toast.LENGTH_LONG).show();
                        if(NetworkConnection.checkConnection(getContext()))
                            makeItems();
                        else{
                            if(swipeRefreshLayout.isRefreshing())
                                swipeRefreshLayout.setRefreshing(false);
                        }


                    }
                }
        );



    searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String s) {
            return false;
        }

        @Override
        public boolean onQueryTextChange(String s) {
            adapter.getFilter().filter(s);
            return false;
        }
    });

        return listView;
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    private void makeItems(){

        itemList.clear();
        DBConnect.getAllItems(getContext(), itemList);
        Toast.makeText(getContext(), "Retrieving Items ... " + itemList.size(), Toast.LENGTH_LONG).show();


        adapter.notifyDataSetChanged();
        if(swipeRefreshLayout.isRefreshing())
            swipeRefreshLayout.setRefreshing(false);
    }

}
