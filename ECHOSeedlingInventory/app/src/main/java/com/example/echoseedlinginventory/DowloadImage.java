package com.example.echoseedlinginventory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class DowloadImage extends AsyncTask<String,Void,Bitmap> {

    private Bitmap bmp;

    private Context mContext;



    public DowloadImage(Bitmap bitmap, Context mContext){
        this.mContext = mContext;
        this.bmp = bitmap;
    }
    @Override
    protected Bitmap doInBackground(String... url) {
        String urldisplay = url[0];
        Bitmap bitmap;
        bitmap = null;
        try {
            InputStream srt = new java.net.URL(urldisplay).openStream();
            bitmap = BitmapFactory.decodeStream(srt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);

        this.bmp = bitmap;

        if(bitmap == null){
            Toast.makeText(mContext,"Could Not Download Image", Toast.LENGTH_SHORT).show();
        }

    }

}

