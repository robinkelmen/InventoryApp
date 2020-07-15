package com.example.echoseedlinginventory;

import android.animation.TimeInterpolator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class NetworkConnection {



    public static View noConnection;
    public LayoutInflater layoutInflater;
    public static boolean  connection  = false;
    public static Activity mainActivity;
    public static TimeInterpolator interpolator = new TimeInterpolator() {
        @Override
        public float getInterpolation(float input) {
            return 0;
        }
    };




    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // this method checks if someone is connected while using public wifi that may require authentication

   /* public static boolean internetIsConnected() {
        try {
            String command = "ping -c 1 google.com";
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }*/
    public static Dialog showDialog(View toInflate, final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        // Get the layout inflater
        //LayoutInflater inflater = ListFragment.myInflater;

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        if(toInflate.getParent() != null){
            ((ViewGroup)toInflate.getParent()).removeView(toInflate);
        }
        builder.setView(toInflate)

                // Add action buttons
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        ( (AppCompatActivity) context).onBackPressed();
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
    public static boolean checkConnection(Context context){

         ExpandableRelativeLayout noNet = mainActivity.findViewById(R.id.banner_no_internet);
       TextView noNetText = mainActivity.findViewById(R.id.banner_no_internet_text);
        noNetText.setBackgroundColor(Color.RED);
        noNetText.setText("Not Connected ... Swipe Down to Refresh");
        if (!isNetworkAvailable(context) ){
            noConnection = ((AppCompatActivity) context).getLayoutInflater().inflate(R.layout.no_internet_dialog, null);
            if(!noNet.isExpanded())
                noNet.expand();
            showDialog(noConnection, context).show();
            connection = false;







        }else{
            connection = true;
            noNetText.setBackgroundColor(((AppCompatActivity) context).getResources().getColor(R.color.Green));
            noNetText.setText("Connected!");
            if(noNet.isExpanded()){
                noNet.collapse();
            }else{
                noNet.toggle(3, new Interpolator() {
                    @Override
                    public float getInterpolation(float input) {
                        float x=2.0f*input-1.0f;
                        return 0.5f*(x*x*x + 1.0f);
                    }
                });



            }


        }

        return  connection;
    }
    public static void setMainActivity(Activity activity){
        mainActivity = activity;

    }

}
